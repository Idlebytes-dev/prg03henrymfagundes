package br.com.ifba.usuario.service;

import br.com.ifba.usuario.entity.Perfil;
import java.util.List;

public interface PerfilIService {
    Perfil cadastrar(String nome, String descricao);
    Perfil atualizar(Long id, String nome, String descricao);
    List<Perfil> listar();
    void excluir(Long id);
}
