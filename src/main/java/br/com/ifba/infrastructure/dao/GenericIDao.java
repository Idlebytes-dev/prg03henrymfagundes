package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import java.util.List;

public interface GenericIDao<Entity extends PersistenceEntity> {
    Entity save(Entity entity);
    Entity update(Entity entity);
    void delete(Entity entity);
    Entity findById(Long id);
    List<Entity> findAll();
}
