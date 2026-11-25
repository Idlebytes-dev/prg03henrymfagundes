package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.entity.Perfil;
import java.util.List;

public interface PerfilIController {
    Perfil cadastrar(String nome, String descricao);
    Perfil atualizar(Long id, String nome, String descricao);
    List<Perfil> listar();
    void excluir(Long id);
}
