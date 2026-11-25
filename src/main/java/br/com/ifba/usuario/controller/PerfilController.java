package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.entity.Perfil;
import br.com.ifba.usuario.service.PerfilIService;
import java.util.List;
import java.util.Objects;

public final class PerfilController implements PerfilIController {
    private final PerfilIService service;

    public PerfilController(PerfilIService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Perfil cadastrar(String nome, String descricao) {
        return service.cadastrar(nome, descricao);
    }

    @Override
    public Perfil atualizar(Long id, String nome, String descricao) {
        return service.atualizar(id, nome, descricao);
    }

    @Override
    public List<Perfil> listar() {
        return service.listar();
    }

    @Override
    public void excluir(Long id) {
        service.excluir(id);
    }
}
