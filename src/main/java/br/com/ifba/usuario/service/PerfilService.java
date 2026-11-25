package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.RegraNegocioException;
import br.com.ifba.usuario.dao.PerfilIDao;
import br.com.ifba.usuario.entity.Perfil;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public final class PerfilService implements PerfilIService {
    private final PerfilIDao dao;

    public PerfilService(PerfilIDao dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    @Override
    public Perfil cadastrar(String nome, String descricao) {
        validar(nome, descricao, null);
        return dao.save(new Perfil(nome.trim(), descricao));
    }

    @Override
    public Perfil atualizar(Long id, String nome, String descricao) {
        Perfil perfil = buscar(id);
        validar(nome, descricao, id);
        perfil.setNome(nome.trim());
        perfil.setDescricao(descricao);
        return dao.update(perfil);
    }

    private void validar(String nome, String descricao, Long id) {
        if (nome == null || nome.isBlank() || nome.trim().length() > 50) {
            throw new RegraNegocioException("Informe um nome de perfil de até 50 caracteres.");
        }
        if (descricao != null && descricao.length() > 255) {
            throw new RegraNegocioException("A descrição pode ter até 255 caracteres.");
        }
        Perfil existente = dao.findByNome(nome.trim());
        if (existente != null && !existente.getId().equals(id)) {
            throw new RegraNegocioException("Nome de perfil já cadastrado.");
        }
    }

    private Perfil buscar(Long id) {
        Perfil perfil = id == null ? null : dao.findById(id);
        if (perfil == null) throw new RegraNegocioException("Perfil não encontrado.");
        return perfil;
    }

    @Override
    public List<Perfil> listar() {
        return dao.findAll();
    }

    @Override
    public void excluir(Long id) {
        dao.delete(buscar(id));
    }
}
