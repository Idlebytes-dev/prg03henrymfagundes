# Retrospectiva - Atividade 14

`password`, `cpf`, `birthDay`, `gender` e `phoneNumber` não devem aparecer na listagem: a senha é confidencial e esses dados pessoais não são necessários para identificar o usuário nessa tela.
`id`, `perfilAtivo` e `perfis` também ficam fora; o futuro DTO de listagem deve transportar somente `name`, `login`, `email` e `status`, sem expor a Entity inteira.
