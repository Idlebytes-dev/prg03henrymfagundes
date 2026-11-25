package br.com.ifba.infra;

import br.com.ifba.usuario.entity.Perfil;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.PersistenceUnitTransactionType;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.hibernate.jpa.boot.spi.PersistenceXmlParser;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

public final class BancoDados {

    private BancoDados() {
    }

    public static EntityManagerFactory inicializar() {
        var ambiente = new StandardEnvironment();
        ConfigDataEnvironmentPostProcessor.applyTo(ambiente);
        return inicializar(ambiente);
    }

    static EntityManagerFactory inicializar(Environment ambiente) {
        String url = ambiente.getRequiredProperty("spring.datasource.url");
        String usuario = ambiente.getRequiredProperty("spring.datasource.username");
        String senha = ambiente.getRequiredProperty("spring.datasource.password");
        migrar(url, usuario, senha);
        return new PersistenceConfiguration("apexoptimaPU")
                .transactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
                .managedClass(Usuario.class)
                .managedClass(Perfil.class)
                .property(PersistenceConfiguration.JDBC_DRIVER, ambiente.getRequiredProperty("spring.datasource.driver-class-name"))
                .property(PersistenceConfiguration.JDBC_URL, url)
                .property(PersistenceConfiguration.JDBC_USER, usuario)
                .property(PersistenceConfiguration.JDBC_PASSWORD, senha)
                .property("hibernate.hbm2ddl.auto", ambiente.getRequiredProperty("spring.jpa.hibernate.ddl-auto"))
                .property("hibernate.show_sql", ambiente.getRequiredProperty("spring.jpa.show-sql"))
                .property("hibernate.format_sql", ambiente.getRequiredProperty("spring.jpa.properties.hibernate.format_sql"))
                .createEntityManagerFactory();
    }

    /** Aceita uma unidade JPA explícita, utilizada pelos testes com schemas isolados. */
    public static EntityManagerFactory inicializar(URL configuracao) {
        if (configuracao == null) {
            throw new IllegalStateException(
                    "Informe a URL da configuração JPA.");
        }
        var unidade = PersistenceXmlParser.create().parse(List.of(configuracao)).get("apexoptimaPU");
        if (unidade == null) {
            throw new IllegalStateException("A unidade apexoptimaPU não foi encontrada em persistence.xml.");
        }
        var propriedades = unidade.getProperties();
        String url = propriedades.getProperty("jakarta.persistence.jdbc.url");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Configure jakarta.persistence.jdbc.url em persistence.xml.");
        }

        migrar(url, propriedades.getProperty("jakarta.persistence.jdbc.user"),
                propriedades.getProperty("jakarta.persistence.jdbc.password"));
        return Bootstrap.getEntityManagerFactoryBuilder(unidade, Map.of()).build();
    }

    private static void migrar(String url, String usuario, String senha) {
        Flyway.configure().dataSource(url, usuario, senha)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}
