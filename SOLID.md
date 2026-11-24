# Atividade 21 — Auditoria SOLID do ApexOptima

## Os cinco princípios no projeto

| Princípio | Exemplo concreto | Onde conferir |
| --- | --- | --- |
| **S — Responsabilidade única** | A superclasse `PersistenceEntity` define como o JPA gera e expõe o identificador comum a usuários e perfis. Uma alteração na estratégia de geração da chave fica concentrada nesse mapeamento. | [PersistenceEntity.java, linha 11](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/infrastructure/entity/PersistenceEntity.java#L11) |
| **O — Aberto/fechado** | `UsuarioDao` acrescenta a busca por login usando a operação protegida de consulta. Outro DAO pode acrescentar sua própria consulta e reutilizar o CRUD e o fechamento do manager. | [UsuarioDao.java, linha 19](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/usuario/dao/UsuarioDao.java#L19) |
| **L — Substituição de Liskov** | O mesmo cálculo recebe qualquer `AbstractSessionState` e devolve o tempo base acrescido da perda no pit lane. Nos testes, 100 s tornam-se 121,5 s com bandeira verde e 112 s com Safety Car físico ou virtual. As três implementações cumprem o mesmo contrato de unidade e de cálculo. | [SessionStateDemo.java, linha 24](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/sessao/demo/SessionStateDemo.java#L24) e [SessionStateDemoTest.java, linha 23](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/test/java/br/com/ifba/sessao/demo/SessionStateDemoTest.java#L23) |
| **I — Segregação de interfaces** | `Autenticavel` expõe apenas a verificação de login e senha. Quem implementa esse contrato fornece uma única operação, independentemente das funções de cadastro e edição de um usuário. | [Autenticavel.java, linha 5](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/usuario/interfaces/Autenticavel.java#L5) |
| **D — Inversão de dependência** | `Session` recebe a interface `SessionState` no construtor e consulta o estado atual por meio dela. O chamador escolhe a implementação que representa a condição de pista. | [Session.java, linha 25](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/sessao/entity/Session.java#L25) e [Session.java, linha 45](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/sessao/entity/Session.java#L45) |

## Três violações encontradas na revisão de `main`

1. **SRP — CRUD e transações no mesmo DAO.** Alterar uma consulta e alterar o tratamento de uma falha no rollback exigiam editar `GenericDao`. Extraí o ciclo de vida do manager e da transação para `ExecutorJpa`. Os DAOs continuam definindo as operações sobre as entidades. Os testes exercitam o commit, a reversão e a preservação da causa original quando o fechamento também falha. Antes em [GenericDao.java, linha 68](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/66f77a298d5cff14a11f99969eff518893a79436/src/main/java/br/com/ifba/infrastructure/dao/GenericDao.java#L68). Correção em [ExecutorJpa.java, linha 26](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/infrastructure/dao/ExecutorJpa.java#L26).
2. **OCP — Lista de termos embutida na busca.** Acrescentar uma palavra reservada ao login exigia editar o método que compara as substrings. Os quatro termos passaram para `login.properties`. Para acrescentar outro termo em minúsculas, basta incluí-lo na propriedade `palavrasProibidas`. A busca continua aceitando o termo em qualquer posição do login. Antes em [ValidadorUsuario.java, linha 45](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/66f77a298d5cff14a11f99969eff518893a79436/src/main/java/br/com/ifba/usuario/validar/ValidadorUsuario.java#L45). Correção em [login.properties, linha 1](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/resources/br/com/ifba/usuario/validar/login.properties#L1) e [ValidadorUsuario.java, linha 54](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/main/java/br/com/ifba/usuario/validar/ValidadorUsuario.java#L54).
3. **DIP — A tela de edição escolhe a persistência.** O construtor sem argumentos de `TelaEdicaoUsuario` instancia `UsuarioDao`. Embora exista um construtor que aceite a interface, a tela ainda tem uma dependência de compilação da implementação JPA. A montagem dessa dependência pertence ao ponto de inicialização da aplicação. Este é o achado registrado para acompanhamento. [TelaEdicaoUsuario.java, linha 45](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/66f77a298d5cff14a11f99969eff518893a79436/src/main/java/br/com/ifba/usuario/view/TelaEdicaoUsuario.java#L45).

## Nomes e comentários

Usei a operação **Rename** do NetBeans para atualizar declarações e referências. Os nomes finais distinguem o valor recebido, a transformação feita e o dado verificado.

| Nome anterior | Nome atual |
| --- | --- |
| `cpfLimpo` | `cpfSemPontuacao` |
| `text` | `login` |
| `normalizedText` | `loginEmMinusculas` |
| `word` | `palavraProibida` |
| `confirmacao` | `confirmacaoSenha` |
| `deveAceitarCamposPreenchidos` | `deveAceitarCadastroComTodosOsCamposPreenchidos` |

Removi o comentário `// Happy paths` do teste de validação. O nome do teste agora identifica o cenário de cadastro e a condição de sucesso. [ValidadorUsuarioTest.java, linha 11](https://github.com/Idlebytes-dev/prg03henrymfagundes/blob/374126a4d47fa16f71459a2f62f6ee7017811ad3/src/test/java/br/com/ifba/usuario/validar/ValidadorUsuarioTest.java#L11).

## Verificação

Executei `./mvnw -B -o -Pintegracao clean verify` antes e depois da refatoração, com PostgreSQL descartável. Nas duas execuções, os **55 testes** passaram, incluindo os sete de integração, com zero falhas, erros ou testes ignorados. A captura da execução final está em [testes.png](ATIVIDADES/ATIVIDADE%2021%20-%20SOLID/testes.png).
