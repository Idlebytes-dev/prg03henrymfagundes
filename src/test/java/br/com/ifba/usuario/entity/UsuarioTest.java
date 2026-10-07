/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author henrymf
 */
public class UsuarioTest {

    @Test
    void deveReconhecerNaListaOutroUsuarioComMesmoLogin() {
        Usuario primeiro = new Usuario();
        primeiro.setLogin("henry");
        primeiro.setName("Henry");
        Usuario segundo = new Usuario();
        segundo.setLogin("henry");
        segundo.setName("Outro nome");
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(primeiro);

        assertTrue(primeiro != segundo);
        assertTrue(usuarios.contains(segundo));
        assertEquals(primeiro, segundo);
        assertEquals(segundo, primeiro);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
    }

    @Test
    void deveDistinguirUsuariosComLoginsDiferentes() {
        Usuario primeiro = new Usuario();
        primeiro.setLogin("henry");
        Usuario segundo = new Usuario();
        segundo.setLogin("ana");

        assertFalse(primeiro.equals(segundo));
        assertFalse(primeiro.equals(null));
        assertFalse(primeiro.equals("henry"));
    }

    @Test
    void usuariosSemLoginSoDevemSerIguaisAPropriaInstancia() {
        Usuario primeiro = new Usuario();
        Usuario segundo = new Usuario();

        assertEquals(primeiro, primeiro);
        assertFalse(primeiro.equals(segundo));
    }

    @Test
    void deveCriarUsuarioComTodosOsDadosNoConstrutor() {
        Usuario usuario = new Usuario(
                "Ana", "52998224725", "F", "2000-01-01",
                "71999999999", "ana@example.com", "ana", "senha123");

        assertEquals("Ana", usuario.getName());
        assertEquals("52998224725", usuario.getCpf());
        assertEquals("F", usuario.getGender());
        assertEquals("2000-01-01", usuario.getBirthDay());
        assertEquals("71999999999", usuario.getPhoneNumber());
        assertEquals("ana@example.com", usuario.getEmail());
        assertEquals("ana", usuario.getLogin());
        assertEquals("senha123", usuario.getPassword());
    }

    //Happy paths
    @Test
    void deveAutenticarQuandoCredenciaisCorretas() {
        
        Usuario usuario = new Usuario();
        usuario.setLogin("testLogin");
        usuario.setPassword("testPassword");
        
        boolean resultado = usuario.autenticar("testLogin", "testPassword");
        
        assertTrue(resultado);
    }
    
    @Test
    void deveRejeitarQuandoCredenciaisIncorretas() {
        
        Usuario usuario = new Usuario();
        usuario.setLogin("testLogin");
        usuario.setPassword("testPassword");
        
        boolean resultado = usuario.autenticar("testLogin", "wrongPassword");
        
        assertFalse(resultado);
    
    }
}   
