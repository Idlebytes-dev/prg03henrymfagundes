/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 *
 * @author henrymf
 */
public class UsuarioTest {
    
    
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
