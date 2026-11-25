package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.exception.PersistenciaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

final class ExecutorJpa {
    private final Supplier<EntityManager> abrirManager;

    ExecutorJpa(Supplier<EntityManager> abrirManager) {
        this.abrirManager = Objects.requireNonNull(abrirManager);
    }

    <R> R consultar(Function<EntityManager, R> consulta) {
        return executar(consulta, false);
    }

    <R> R escrever(Function<EntityManager, R> escrita) {
        return executar(escrita, true);
    }

    // Leitura e escrita compartilham o fechamento; só a escrita abre transação.
    private <Resultado> Resultado executar(Function<EntityManager, Resultado> operacao, boolean escrita) {
        EntityManager manager = null;
        EntityTransaction transacao = null;
        PersistenciaException falha = null;
        try {
            manager = abrirManager.get();
            if (escrita) {
                transacao = manager.getTransaction();
                transacao.begin();
            }
            Resultado resultado = operacao.apply(manager);
            if (escrita) {
                transacao.commit();
            }
            return resultado;
        } catch (RuntimeException causa) {
            falha = new PersistenciaException("Não foi possível concluir a operação no banco de dados.", causa);
            if (transacao != null) {
                try {
                    if (transacao.isActive()) {
                        transacao.rollback();
                    }
                } catch (RuntimeException erroRollback) {
                    falha.addSuppressed(erroRollback);
                }
            }
            throw falha;
        } finally {
            if (manager != null) {
                try {
                    manager.close();
                } catch (RuntimeException erroClose) {
                    if (falha != null) {
                        falha.addSuppressed(erroClose);
                    } else {
                        throw new PersistenciaException("Não foi possível fechar o acesso ao banco de dados.", erroClose);
                    }
                }
            }
        }
    }
}
