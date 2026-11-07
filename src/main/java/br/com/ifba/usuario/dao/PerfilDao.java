package br.com.ifba.usuario.dao;

import br.com.ifba.infrastructure.dao.GenericDao;
import br.com.ifba.usuario.entity.Perfil;
import jakarta.persistence.EntityManager;
import java.util.function.Supplier;

public class PerfilDao extends GenericDao<Perfil> implements PerfilIDao {
    public PerfilDao() {
        super(Perfil.class);
    }

    public PerfilDao(Supplier<EntityManager> managers) {
        super(Perfil.class, managers);
    }

    @Override
    public Perfil findByNome(String nome) {
        return consultar(em -> em.createQuery(
                "select p from Perfil p where p.nome = :nome", Perfil.class)
                .setParameter("nome", nome).setMaxResults(1).getResultList()
                .stream().findFirst().orElse(null));
    }
}
