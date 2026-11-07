package br.com.ifba.usuario.dao;

import br.com.ifba.infrastructure.dao.GenericIDao;
import br.com.ifba.usuario.entity.Perfil;

public interface PerfilIDao extends GenericIDao<Perfil> {
    Perfil findByNome(String nome);
}
