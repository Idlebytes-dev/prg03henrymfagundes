package br.com.ifba.infra;

import br.com.ifba.usuario.entity.Status;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.PersistenceException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.flywaydb.core.Flyway;
import org.hibernate.tool.schema.spi.SchemaManagementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

class BancoDadosIT {

    @TempDir
    Path pasta;
    private String esquema;
    private String url;
    private String usuario;
    private String senha;
    private Path configuracao;

    @BeforeEach
    void prepararSchemaIsolado() throws Exception {
        String base = Objects.requireNonNull(System.getenv("APEXOPTIMA_TEST_JDBC_URL"),
                "Defina APEXOPTIMA_TEST_JDBC_URL para um PostgreSQL de testes.");
        usuario = Objects.requireNonNull(System.getenv("APEXOPTIMA_TEST_DB_USER"),
                "Defina APEXOPTIMA_TEST_DB_USER.");
        senha = System.getenv().getOrDefault("APEXOPTIMA_TEST_DB_PASSWORD", "");
        esquema = "atividade18_" + UUID.randomUUID().toString().replace("-", "");
        try (var conexao = DriverManager.getConnection(base, usuario, senha);
             var comando = conexao.createStatement()) {
            comando.execute("CREATE SCHEMA " + esquema);
        }
        url = base + (base.contains("?") ? "&" : "?") + "currentSchema=" + esquema;

        var parser = DocumentBuilderFactory.newInstance();
        parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        try (var modelo = getClass().getResourceAsStream("/META-INF/persistence.xml.exemplo")) {
            assertNotNull(modelo);
            var documento = parser.newDocumentBuilder().parse(modelo);
            var propriedades = documento.getElementsByTagName("property");
            for (int i = 0; i < propriedades.getLength(); i++) {
                var propriedade = (org.w3c.dom.Element) propriedades.item(i);
                switch (propriedade.getAttribute("name")) {
                    case "jakarta.persistence.jdbc.url" -> propriedade.setAttribute("value", url);
                    case "jakarta.persistence.jdbc.user" -> propriedade.setAttribute("value", usuario);
                    case "jakarta.persistence.jdbc.password" -> propriedade.setAttribute("value", senha);
                }
            }
            configuracao = Files.createDirectories(pasta.resolve("META-INF")).resolve("persistence.xml");
            var transformador = TransformerFactory.newInstance();
            transformador.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformador.newTransformer().transform(new DOMSource(documento),
                    new StreamResult(configuracao.toFile()));
        }
    }

    @AfterEach
    void removerSomenteSchemaDoTeste() throws SQLException {
        if (url != null) {
            try (var conexao = conectar(); var comando = conexao.createStatement()) {
                comando.execute("DROP SCHEMA " + esquema + " CASCADE");
            }
        }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }

    @Test
    void deveMigrarDoZeroValidarEPersistirUsuario() throws Exception {
        try (var factory = BancoDados.inicializar(configuracao.toUri().toURL());
             var manager = factory.createEntityManager()) {
            assertEquals("validate", factory.getProperties().get("hibernate.hbm2ddl.auto"));
            var cadastro = new Usuario("Ana", "52998224725", "F", "2000-01-01",
                    "71999999999", "ana@example.com", "ana", "senha-teste");
            manager.getTransaction().begin();
            manager.persist(cadastro);
            manager.flush();
            Long id = cadastro.getId();
            assertNotNull(id);
            manager.clear();
            var gravado = manager.find(Usuario.class, id);
            assertEquals("ana", gravado.getLogin());
            assertEquals(Status.INATIVO, gravado.getStatus());
            assertNotNull(gravado.getCriadoEm());
            manager.getTransaction().commit();
        }
        try (var conexao = conectar(); var comando = conexao.createStatement();
             var linhas = comando.executeQuery(
                     "SELECT version, success FROM flyway_schema_history ORDER BY installed_rank")) {
            for (String versao : new String[]{"1", "2", "3"}) {
                assertTrue(linhas.next());
                assertEquals(versao, linhas.getString("version"));
                assertTrue(linhas.getBoolean("success"));
            }
            assertFalse(linhas.next());
        }
        // Reiniciar não reaplica versões nem apaga o usuário persistido.
        try (var factory = BancoDados.inicializar(configuracao.toUri().toURL());
             var manager = factory.createEntityManager()) {
            assertEquals(1L, manager.createQuery("select count(u) from Usuario u", Long.class)
                    .getSingleResult());
        }
        assertEquals(3, Flyway.configure().dataSource(url, usuario, senha).load().info().applied().length);
    }

    @Test
    void devePreencherCriadoEmAoEvoluirBancoComUsuarios() throws Exception {
        Flyway.configure().dataSource(url, usuario, senha).target("1").load().migrate();
        try (var conexao = conectar(); var comando = conexao.createStatement()) {
            comando.executeUpdate("""
                    INSERT INTO usuarios (nome, cpf, email, login, senha, status)
                    VALUES ('Anterior', '52998224725', 'anterior@example.com', 'anterior', 'teste', 'ATIVO')
                    """);
        }
        try (var factory = BancoDados.inicializar(configuracao.toUri().toURL());
             var manager = factory.createEntityManager()) {
            var anterior = manager.createQuery("select u from Usuario u", Usuario.class).getSingleResult();
            assertEquals("anterior", anterior.getLogin());
            assertNotNull(anterior.getCriadoEm());
        }
    }

    @Test
    void deveRejeitarLoginDuplicadoECriadoEmNulo() throws Exception {
        try (var factory = BancoDados.inicializar(configuracao.toUri().toURL());
             var conexao = conectar(); var comando = conexao.createStatement()) {
            String insercao = """
                    INSERT INTO usuarios (nome, cpf, email, login, senha, status)
                    VALUES ('Ana', '52998224725', 'ana@example.com', 'ana', 'teste', 'ATIVO')
                    """;
            comando.executeUpdate(insercao);
            assertEquals("23505", assertThrows(SQLException.class,
                    () -> comando.executeUpdate(insercao)).getSQLState());
            assertEquals("23502", assertThrows(SQLException.class,
                    () -> comando.executeUpdate("UPDATE usuarios SET criado_em = NULL")).getSQLState());
        }
    }

    @Test
    void deveFalharQuandoSchemaDivergeDaEntidadeSemRecriarColuna() throws Exception {
        try (var factory = BancoDados.inicializar(configuracao.toUri().toURL());
             var conexao = conectar(); var comando = conexao.createStatement()) {
            comando.execute("ALTER TABLE usuarios DROP COLUMN nome");
        }
        var erro = assertThrows(PersistenceException.class,
                () -> BancoDados.inicializar(configuracao.toUri().toURL()));
        assertInstanceOf(SchemaManagementException.class, erro.getCause());
        assertTrue(erro.getCause().getMessage().contains("nome"));
        try (var conexao = conectar(); var comando = conexao.createStatement();
             var colunas = comando.executeQuery("""
                     SELECT count(*) FROM information_schema.columns
                     WHERE table_schema = current_schema() AND table_name = 'usuarios' AND column_name = 'nome'
                     """)) {
            assertTrue(colunas.next());
            assertEquals(0, colunas.getInt(1));
        }
    }
}
