/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author henrymf
 */
public class RepositorioUsuarioEmMemoria {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final Map<String, Usuario> porLogin = new HashMap<>();

    public void cadastrar(Usuario usuario) {
        if (usuario == null || usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            throw new IllegalArgumentException("Informe um usuário com login preenchido.");
        }
        String login = usuario.getLogin();
        if (porLogin.containsKey(login)) {
            throw new IllegalArgumentException("Login já cadastrado.");
        }
        usuarios.add(usuario);
        porLogin.put(login, usuario);
    }

    /** Retorna uma cópia não modificável, preservando a lista e o índice internos. */
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios);
    }

    /** Busca pelo login exato; retorna null quando não houver cadastro. */
    public Usuario buscarPorLogin(String login) {
        return porLogin.get(login);
    }
}
