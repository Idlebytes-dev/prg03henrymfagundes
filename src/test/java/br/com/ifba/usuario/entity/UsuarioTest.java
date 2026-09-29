/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 *
 * @author henrymf
 */
public class UsuarioTest {

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
