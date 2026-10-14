package br.com.ifba.usuario.entity;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UsuarioJpaTest {

    @Test
    void deveInicializarHibernateEPrepararConsultaSemBancoLocal() {
        var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.boot.allow_jdbc_metadata_access", false)
                .applySetting("hibernate.connection.provider_class",
                        "org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl")
                .build();

        try (var factory = new MetadataSources(registry)
                .addAnnotatedClass(Usuario.class)
                .buildMetadata()
                .buildSessionFactory();
             var session = factory.openSession()) {
            // Compila a consulta sem executá-la nem depender de credenciais locais.
            assertDoesNotThrow(() -> session.createSelectionQuery(
                    "select u from Usuario u where u.login = :login and u.status = :status",
                    Usuario.class)
                    .setParameter("login", "engenheiro")
                    .setParameter("status", Status.ATIVO));
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
