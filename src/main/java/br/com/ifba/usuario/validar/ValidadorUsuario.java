/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.validar;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author henrymf
 */
public class ValidadorUsuario {
    private static final List<String> PALAVRAS_PROIBIDAS = List.of(
            ResourceBundle.getBundle("br.com.ifba.usuario.validar.login", Locale.ROOT)
                    .getString("palavrasProibidas").split("\\s*,\\s*"));

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

    public static boolean senhasConferem(String senha, String confirmacaoSenha) {
        return senha != null && senha.equals(confirmacaoSenha);
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cpfSemPontuacao = cpf.trim().replace(".", "").replace("-", "");
        return cpfSemPontuacao.length() == 11
                && cpfSemPontuacao.chars().noneMatch(Character::isLetter);
    }

    public static boolean contemPalavraProibida(String login) {
        if (login == null || login.isBlank()) {
            return false;
        }
        String loginEmMinusculas = login.toLowerCase();

        for (String palavraProibida : PALAVRAS_PROIBIDAS) {
            if (loginEmMinusculas.contains(palavraProibida)) {
                return true;
            }
        }
        return false;
    }
}
