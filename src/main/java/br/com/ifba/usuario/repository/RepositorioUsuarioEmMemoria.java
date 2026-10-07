/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author henrymf
 */
public class RepositorioUsuarioEmMemoria {
    
    private final List<Usuario> usuarios = new ArrayList<>();
    
    public void cadastrar(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    public List<Usuario> listarTodos() {
        return usuarios;
    }

    public Usuario buscarPorLogin(String login) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                return usuario;
            }
        }
        return null;
    }
}
