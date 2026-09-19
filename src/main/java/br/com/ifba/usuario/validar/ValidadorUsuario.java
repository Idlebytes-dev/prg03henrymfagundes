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

    public static boolean camposPreenchidos(String... campos) {
        if (campos == null || campos.length == 0) {
            return false;
        }

        for (String campo : campos) {
            if (campo == null || campo.isBlank()) {
                return false;
            }
        }

        return true;
    }

    public static boolean senhasConferem(String senha, String confirmacao) {
        return senha != null && senha.equals(confirmacao);
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cpfLimpo = cpf.trim().replace(".", "").replace("-", "");
        return cpfLimpo.length() == 11
                && cpfLimpo.chars().noneMatch(Character::isLetter);
    }

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
