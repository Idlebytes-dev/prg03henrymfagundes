package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.JPAUtil;
import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericDao<Entity extends PersistenceEntity> implements GenericIDao<Entity> {
    private final Class<Entity> tipo;
    private final ExecutorJpa executor;

    public GenericDao(Class<Entity> tipo) {
        this(tipo, JPAUtil::getEntityManager);
    }

    public GenericDao(Class<Entity> tipo, Supplier<EntityManager> managers) {
        this.tipo = Objects.requireNonNull(tipo);
        this.executor = new ExecutorJpa(managers);
    }

    @Override
    public Entity save(Entity entity) {
        return executor.escrever(em -> {
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public Entity update(Entity entity) {
        return executor.escrever(em -> em.merge(entity));
    }

    @Override
    public void delete(Entity entity) {
        executor.escrever(em -> {
            Entity existente = em.find(tipo, entity.getId());
            if (existente == null) {
                throw new IllegalArgumentException("Registro não encontrado para exclusão.");
            }
            em.remove(existente);
            return null;
        });
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
        return executor.consultar(consulta);
    }
}
