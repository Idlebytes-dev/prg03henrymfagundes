package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.dao.GenericIDao;
import br.com.ifba.infrastructure.entity.PersistenceEntity;
import br.com.ifba.usuario.dao.PerfilIDao;
import br.com.ifba.usuario.dao.UsuarioIDao;
import br.com.ifba.usuario.entity.Perfil;
import br.com.ifba.usuario.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract class FakeDao<T extends PersistenceEntity> implements GenericIDao<T> {
    private final List<T> registros = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public T save(T entidade) {
        entidade.setId(proximoId++);
        registros.add(entidade);
        return entidade;
    }

    @Override
    public T update(T entidade) {
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getId().equals(entidade.getId())) {
                registros.set(i, entidade);
                return entidade;
            }
        }
        throw new IllegalStateException("Registro ausente.");
    }

    @Override
    public void delete(T entidade) {
        registros.removeIf(item -> item.getId().equals(entidade.getId()));
    }

    @Override
    public T findById(Long id) {
        return registros.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(registros);
    }

    static final class Usuarios extends FakeDao<Usuario> implements UsuarioIDao {
        @Override
        public Usuario findByLogin(String login) {
            return findAll().stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null);
        }

        @Override
        public List<Usuario> findByNomeOuLogin(String termo) {
            String busca = termo.toLowerCase(Locale.ROOT);
            return findAll().stream().filter(u -> u.getName().toLowerCase(Locale.ROOT).contains(busca)
                    || u.getLogin().toLowerCase(Locale.ROOT).contains(busca)).toList();
        }
    }

    static final class Perfis extends FakeDao<Perfil> implements PerfilIDao {
        @Override
        public Perfil findByNome(String nome) {
            return findAll().stream().filter(p -> p.getNome().equals(nome)).findFirst().orElse(null);
        }
    }
}
