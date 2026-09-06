/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.validar;

/**
 *
 * @author henrymf
 */
public class ValidadorUsuario {
    public static boolean contemPalavraProibida(String text){
        if (text == null || text.isBlank()){
            return false;
        }
        String[] forbiddenWords = {"admin", "teste", "root", "senha"};
        String normalizedText = text.toLowerCase();
        
        for (String word : forbiddenWords){
            if (normalizedText.contains(word)){
                return true;
            }
        }
        return false;
    }
}
