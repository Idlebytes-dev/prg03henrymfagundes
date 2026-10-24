-- O DEFAULT também preenche os usuários cadastrados antes desta versão.
ALTER TABLE usuarios
    ADD COLUMN criado_em TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
