package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.RegraNegocioException;
import br.com.ifba.usuario.dao.UsuarioIDao;
import br.com.ifba.usuario.dto.DadosUsuario;
import br.com.ifba.usuario.entity.Status;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.validar.ValidadorUsuario;
import java.util.List;
import java.util.Objects;

public final class UsuarioService implements UsuarioIService {
    private final UsuarioIDao dao;

    public UsuarioService(UsuarioIDao dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    @Override
    public Usuario cadastrar(DadosUsuario dados) {
        validar(dados, null);
        Usuario usuario = new Usuario();
        preencher(usuario, dados);
        usuario.setStatus(Status.INATIVO);
        return dao.save(usuario);
    }

    @Override
    public Usuario atualizar(Long id, DadosUsuario dados) {
        Usuario usuario = buscar(id);
        validar(dados, id);
        preencher(usuario, dados);
        usuario.setStatus(dados.status());
        return dao.update(usuario);
    }

    private void validar(DadosUsuario dados, Long id) {
        if (dados == null || !ValidadorUsuario.camposPreenchidos(dados.nome(), dados.cpf(),
                dados.genero(), dados.nascimento(), dados.telefone(), dados.email(), dados.login())) {
            throw new RegraNegocioException("Preencha os campos obrigatórios.");
        }
        if (!ValidadorUsuario.cpfValido(dados.cpf())) {
            throw new RegraNegocioException("CPF inválido.");
        }
        if (ValidadorUsuario.contemPalavraProibida(dados.login())) {
            throw new RegraNegocioException("O login contém uma palavra não permitida.");
        }
        if (!ValidadorUsuario.senhasConferem(dados.senha(), dados.confirmacao())) {
            throw new RegraNegocioException("As senhas não coincidem.");
        }
        boolean manterSenha = id != null && dados.senha().isEmpty();
        if (!manterSenha && dados.senha().isBlank()) {
            throw new RegraNegocioException("Informe uma senha.");
        }
        if (id != null && dados.status() == null) {
            throw new RegraNegocioException("Selecione um status.");
        }
        Usuario existente = dao.findByLogin(dados.login().trim());
        if (existente != null && !existente.getId().equals(id)) {
            throw new RegraNegocioException("Login já cadastrado.");
        }
    }

    private void preencher(Usuario usuario, DadosUsuario dados) {
        usuario.setName(dados.nome().trim());
        usuario.setCpf(dados.cpf().trim());
        usuario.setGender(dados.genero().trim());
        usuario.setBirthDay(dados.nascimento().trim());
        usuario.setPhoneNumber(dados.telefone().trim());
        usuario.setEmail(dados.email().trim());
        usuario.setLogin(dados.login().trim());
        if (!dados.senha().isEmpty()) usuario.setPassword(dados.senha());
    }

    @Override
    public List<Usuario> listar(String termo) {
        return termo == null || termo.isBlank() ? dao.findAll() : dao.findByNomeOuLogin(termo.trim());
    }

    @Override
    public Usuario buscar(Long id) {
        Usuario usuario = id == null ? null : dao.findById(id);
        if (usuario == null) throw new RegraNegocioException("Usuário não encontrado.");
        return usuario;
    }

    @Override
    public void excluir(Long id) {
        dao.delete(buscar(id));
    }

    @Override
    public boolean autenticar(String login, String senha) {
        if (!ValidadorUsuario.camposPreenchidos(login, senha)) return false;
        Usuario usuario = dao.findByLogin(login);
        return usuario != null && usuario.autenticar(login, senha);
    }
}
