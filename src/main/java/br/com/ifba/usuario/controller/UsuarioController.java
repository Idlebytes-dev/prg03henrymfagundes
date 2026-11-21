package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.DadosUsuario;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioIService;
import java.util.List;
import java.util.Objects;

public final class UsuarioController implements UsuarioIController {
    private final UsuarioIService service;

    public UsuarioController(UsuarioIService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Usuario cadastrar(DadosUsuario dados) {
        return service.cadastrar(dados);
    }

    @Override
    public Usuario atualizar(Long id, DadosUsuario dados) {
        return service.atualizar(id, dados);
    }

    @Override
    public List<Usuario> listar(String termo) {
        return service.listar(termo);
    }

    @Override
    public Usuario buscar(Long id) {
        return service.buscar(id);
    }

    @Override
    public void excluir(Long id) {
        service.excluir(id);
    }

    @Override
    public boolean autenticar(String login, String senha) {
        return service.autenticar(login, senha);
    }
}
