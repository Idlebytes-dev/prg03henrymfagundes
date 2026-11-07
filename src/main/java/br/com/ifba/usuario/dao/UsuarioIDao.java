package br.com.ifba.usuario.dao;

import br.com.ifba.infrastructure.dao.GenericIDao;
import br.com.ifba.usuario.entity.Usuario;
import java.util.List;

public interface UsuarioIDao extends GenericIDao<Usuario> {
    Usuario findByLogin(String login);
    List<Usuario> findByNomeOuLogin(String termo);
}
