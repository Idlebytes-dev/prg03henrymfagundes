package br.com.ifba.infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration(proxyBeanMethods = false)
class ConfiguracaoJpa {
    @Bean(destroyMethod = "close")
    EntityManagerFactory entityManagerFactory(Environment ambiente) {
        return BancoDados.inicializar(ambiente);
    }

    @Bean
    Supplier<EntityManager> abrirEntityManager(EntityManagerFactory factory) {
        return factory::createEntityManager;
    }
}
