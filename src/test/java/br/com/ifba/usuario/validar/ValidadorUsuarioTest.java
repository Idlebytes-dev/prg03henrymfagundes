package br.com.ifba.usuario.validar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidadorUsuarioTest {

    // Happy paths

    @Test
    void deveAceitarCamposPreenchidos() {
        assertTrue(ValidadorUsuario.camposPreenchidos(
                "Maria", "12345678909", "Feminino", "01/01/2000",
                "71999999999", "maria@email.com", "maria",
                "Senha123", "Senha123"));
    }

    @Test
    void deveAceitarSenhasIguais() {
        assertTrue(ValidadorUsuario.senhasConferem("Senha123", "Senha123"));
    }

    @Test
    void deveAceitarCpfFormatadoComOnzeDigitos() {
        assertTrue(ValidadorUsuario.cpfValido(" 123.456.789-10 "));
    }

    @Test
    void deveIdentificarTextoSemPalavraProibida() {
        assertFalse(ValidadorUsuario.contemPalavraProibida("maria"));
    }

    // Limit and invalid-input edge cases

    @Test
    void deveRejeitarCamposNulosVaziosOuEmBranco() {
        assertFalse(ValidadorUsuario.camposPreenchidos((String[]) null));
        assertFalse(ValidadorUsuario.camposPreenchidos());
        assertFalse(ValidadorUsuario.camposPreenchidos("Maria", null));
        assertFalse(ValidadorUsuario.camposPreenchidos("Maria", ""));
        assertFalse(ValidadorUsuario.camposPreenchidos("Maria", "   "));
    }

    @Test
    void deveRejeitarSenhasDiferentesOuNulas() {
        assertFalse(ValidadorUsuario.senhasConferem("Senha123", "OutraSenha"));
        assertFalse(ValidadorUsuario.senhasConferem(null, "Senha123"));
        assertFalse(ValidadorUsuario.senhasConferem("Senha123", null));
    }

    @Test
    void deveRejeitarCpfNuloOuComQuantidadeDiferenteDeOnzeCaracteresAposLimpeza() {
        assertFalse(ValidadorUsuario.cpfValido(null));
        assertFalse(ValidadorUsuario.cpfValido(""));
        assertFalse(ValidadorUsuario.cpfValido("123.456.789-0"));
        assertFalse(ValidadorUsuario.cpfValido("123.456.789-090"));
    }

    @Test
    void deveRejeitarCpfComLetras() {
        assertFalse(ValidadorUsuario.cpfValido("123.456.78A-10"));
    }

    @Test
    void deveEncontrarPalavraProibidaSemDiferenciarMaiusculas() {
        assertTrue(ValidadorUsuario.contemPalavraProibida("ADMINMaria"));
        assertTrue(ValidadorUsuario.contemPalavraProibida("usuario_teste"));
        assertTrue(ValidadorUsuario.contemPalavraProibida("superroot"));
        assertTrue(ValidadorUsuario.contemPalavraProibida("minhasenha"));
    }

    @Test
    void deveIgnorarTextoNuloOuEmBrancoAoProcurarPalavraProibida() {
        assertFalse(ValidadorUsuario.contemPalavraProibida(null));
        assertFalse(ValidadorUsuario.contemPalavraProibida(""));
        assertFalse(ValidadorUsuario.contemPalavraProibida("   "));
    }
}
