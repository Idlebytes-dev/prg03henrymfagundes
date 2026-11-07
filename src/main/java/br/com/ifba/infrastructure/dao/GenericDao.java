package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.JPAUtil;
import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.infrastructure.exception.PersistenciaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericDao<Entity extends PersistenceEntity> implements GenericIDao<Entity> {
    private final Class<Entity> tipo;
    private final Supplier<EntityManager> managers;

    public GenericDao(Class<Entity> tipo) {
        this(tipo, JPAUtil::getEntityManager);
    }

    public GenericDao(Class<Entity> tipo, Supplier<EntityManager> managers) {
        this.tipo = Objects.requireNonNull(tipo);
        this.managers = Objects.requireNonNull(managers);
    }

    @Override
    public Entity save(Entity entity) {
        return executar(em -> {
            em.persist(entity);
            return entity;
        }, true);
    }

    @Override
    public Entity update(Entity entity) {
        return executar(em -> em.merge(entity), true);
    }

    @Override
    public void delete(Entity entity) {
        executar(em -> {
            Entity existente = em.find(tipo, entity.getId());
            if (existente == null) {
                throw new IllegalArgumentException("Registro não encontrado para exclusão.");
            }
            em.remove(existente);
            return null;
        }, true);
    }

    /** Retorna null quando não existe registro para o ID. */
    @Override
    public Entity findById(Long id) {
        return consultar(em -> em.find(tipo, id));
    }

    @Override
    public List<Entity> findAll() {
        return consultar(em -> em.createQuery(
                "select e from " + tipo.getSimpleName() + " e order by e.id", tipo).getResultList());
    }

    protected <Resultado> Resultado consultar(Function<EntityManager, Resultado> consulta) {
        return executar(consulta, false);
    }

    // Leitura e escrita compartilham o fechamento; só a escrita abre transação.
    private <Resultado> Resultado executar(Function<EntityManager, Resultado> operacao, boolean escrita) {
        EntityManager manager = null;
        EntityTransaction transacao = null;
        PersistenciaException falha = null;
        try {
            manager = managers.get();
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
