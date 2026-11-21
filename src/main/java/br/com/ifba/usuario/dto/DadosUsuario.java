package br.com.ifba.usuario.dto;

import br.com.ifba.usuario.entity.Status;

/** Valores digitados antes da validação. Senhas não aparecem na representação textual. */
public record DadosUsuario(String nome, String cpf, String genero, String nascimento,
                           String telefone, String email, String login, String senha,
                           String confirmacao, Status status) {
    @Override
    public String toString() {
        return "DadosUsuario";
    }
}
