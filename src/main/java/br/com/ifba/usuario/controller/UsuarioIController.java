package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.DadosUsuario;
import br.com.ifba.usuario.entity.Usuario;
import java.util.List;

public interface UsuarioIController {
    Usuario cadastrar(DadosUsuario dados);
    Usuario atualizar(Long id, DadosUsuario dados);
    List<Usuario> listar(String termo);
    Usuario buscar(Long id);
    void excluir(Long id);
    boolean autenticar(String login, String senha);
}
