/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 *
 * @author henrymf
 */
public class UsuarioRelacionamentoTest {

    @Test
    void deveAdicionarPerfilEIncrementarLista() {
        Usuario usuario = new Usuario();
        Perfil perfil = new Perfil("ADMIN", "Admin");

        usuario.adicionarPerfil(perfil);

        assertNotNull(usuario.getPerfis());
        assertEquals(1, usuario.getPerfis().size());
        assertEquals("ADMIN", usuario.getPerfis().get(0).getNome());
        assertEquals("Admin", usuario.getPerfis().get(0).getDescricao());
    }

    @Test
    void usuarioNovoDeveNascerComStatusInativo() {
        Usuario usuario = new Usuario();
        assertEquals(Status.INATIVO, usuario.getStatus());
    }

    @Test
    void deveDefinirERecuperarPerfilAtivo() {
        Usuario usuario = new Usuario();
        Perfil perfil = new Perfil("ADMIN", "Admin");

        usuario.setPerfilAtivo(perfil);

        assertEquals(perfil, usuario.getPerfilAtivo());
    }
}
