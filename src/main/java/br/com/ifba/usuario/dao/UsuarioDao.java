package br.com.ifba.usuario.dao;

import br.com.ifba.infrastructure.dao.GenericDao;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.Supplier;

public class UsuarioDao extends GenericDao<Usuario> implements UsuarioIDao {
    public UsuarioDao() {
        super(Usuario.class);
    }

    public UsuarioDao(Supplier<EntityManager> managers) {
        super(Usuario.class, managers);
    }

    @Override
    public Usuario findByLogin(String login) {
        return consultar(em -> em.createQuery(
                "select u from Usuario u where u.login = :login", Usuario.class)
                .setParameter("login", login).setMaxResults(1).getResultList()
                .stream().findFirst().orElse(null));
    }

    @Override
    public List<Usuario> findByNomeOuLogin(String termo) {
        return consultar(em -> em.createQuery("""
                select u from Usuario u
                where locate(lower(:termo), lower(u.name)) > 0
                   or locate(lower(:termo), lower(u.login)) > 0
                order by u.id
                """, Usuario.class).setParameter("termo", termo).getResultList());
    }
}
