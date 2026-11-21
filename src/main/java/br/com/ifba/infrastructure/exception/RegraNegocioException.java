package br.com.ifba.infrastructure.exception;

/** Regra de negócio que o usuário pode corrigir na interface. */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
