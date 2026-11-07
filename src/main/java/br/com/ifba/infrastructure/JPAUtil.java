package br.com.ifba.infrastructure;

import br.com.ifba.infra.BancoDados;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/** Mantém um factory por execução; cada operação DAO recebe seu próprio manager. */
public final class JPAUtil {
    private static EntityManagerFactory factory;

    private JPAUtil() {
    }

    public static synchronized void inicializar() {
        if (factory == null || !factory.isOpen()) {
            factory = BancoDados.inicializar();
        }
    }

    public static synchronized EntityManager getEntityManager() {
        inicializar();
        return factory.createEntityManager();
    }

    public static synchronized void fechar() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}
