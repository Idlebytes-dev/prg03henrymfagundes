package br.com.ifba.infrastructure.exception;

/** Erro de acesso aos dados apresentado pelo contrato dos DAOs. */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
