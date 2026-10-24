package br.com.ifba.usuario.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioJpaTest {

    @Test
    void deveInicializarHibernateEPrepararConsultaSemBancoLocal(@TempDir Path pasta) throws IOException {
        Path configuracao = Files.createDirectories(pasta.resolve("META-INF")).resolve("persistence.xml");
        try (var exemplo = getClass().getResourceAsStream("/META-INF/persistence.xml.exemplo")) {
            assertNotNull(exemplo);
            Files.copy(exemplo, configuracao);
        }
        Map<String, Object> propriedades = Map.of(
                "hibernate.hbm2ddl.auto", "none",
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
                "hibernate.boot.allow_jdbc_metadata_access", false,
                "hibernate.connection.provider_class",
                "org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl");

        try (var factory = Bootstrap.getEntityManagerFactoryBuilder(
                configuracao.toUri().toURL(), "apexoptimaPU", propriedades).build();
             var manager = factory.createEntityManager()) {
            // Compila a consulta sem executá-la nem depender de credenciais locais.
            assertDoesNotThrow(() -> manager.createQuery(
                    "select u from Usuario u where u.login = :login and u.status = :status",
                    Usuario.class)
                    .setParameter("login", "engenheiro")
                    .setParameter("status", Status.ATIVO));
        }
    }
}
