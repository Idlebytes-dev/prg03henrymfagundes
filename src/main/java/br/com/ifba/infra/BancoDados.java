package br.com.ifba.infra;

import jakarta.persistence.EntityManagerFactory;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.hibernate.jpa.boot.spi.PersistenceXmlParser;

public final class BancoDados {

    private BancoDados() {
    }

    public static EntityManagerFactory inicializar() {
        return inicializar(BancoDados.class.getResource("/META-INF/persistence.xml"));
    }

    public static EntityManagerFactory inicializar(URL configuracao) {
        if (configuracao == null) {
            throw new IllegalStateException(
                    "Copie META-INF/persistence.xml.exemplo para persistence.xml e configure o banco local.");
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

        Flyway.configure()
                .dataSource(url, propriedades.getProperty("jakarta.persistence.jdbc.user"),
                        propriedades.getProperty("jakarta.persistence.jdbc.password"))
                .locations("classpath:db/migration")
                .load()
                .migrate();

        // O Hibernate só valida o schema depois que o Flyway aplica as versões pendentes.
        return Bootstrap.getEntityManagerFactoryBuilder(unidade, Map.of()).build();
    }
}
