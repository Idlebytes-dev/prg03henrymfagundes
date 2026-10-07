# ApexOptima — Real-Time Motorsport Strategy & Telemetry Engine

Projeto desenvolvido para as disciplinas de **Programação Orientada a Objetos** (3º Semestre) e **Programação Web** (4º Semestre).

## Atividades do Curso
- [Atividade 01: Documento de Especificação de Requisitos (DER)](ATIVIDADES/ATIVIDADE%2001%20-%20DOCUMENTO%20REQUISITOS/DER.md)
- [Atividade 02: Exercícios LearnGit (Local)](ATIVIDADES/ATIVIDADE%2002%20-%20LEARNGIT)
- [Atividade 03: Exercícios LearnGit (Remote) e Pull Request](ATIVIDADES/ATIVIDADE%2003%20-%20LEARNGIT)
- [Atividade 04: Projeto e Tela de Login](ATIVIDADES/ATIVIDADE%2004%20-%20PROJETO%20E%20TELA%20LOGIN)
- [Atividade 05: Tela de Cadastro](ATIVIDADES/ATIVIDADE%2005%20-%20TELA%20CADASTRO)
- [Atividade 06: Validação de Cadastro](ATIVIDADES/ATIVIDADE%2006%20-%20VALIDA%C3%87%C3%83O%20CADASTRO)
- [Atividade 07: Classe Usuário](ATIVIDADES/ATIVIDADE%2007%20-%20CLASSE%20USUARIO)
- [Atividade 08: Diagrama de Usuário](ATIVIDADES/ATIVIDADE%2008%20-%20DIAGRAMA%20DE%20USUARIO)
- [Atividade 09: Interface](ATIVIDADES/ATIVIDADE%2009%20-%20INTERFACE)
- [Atividade 10: Testes](ATIVIDADES/ATIVIDADE%2010%20-%20TESTES)
- [Atividade 11: Relacionamento](ATIVIDADES/ATIVIDADE%2011%20-%20RELACIONAMENTO)
- [Atividade 12: Herança](ATIVIDADES/ATIVIDADE%2012%20-%20HERAN%C3%87A)
- [Atividade 13: Polimorfismo](ATIVIDADES/ATIVIDADE%2013%20-%20POLIMORFISMO)
- [Atividade 15: Collections](ATIVIDADES/ATIVIDADE%2015%20-%20COLLECTIONS)

## Atividade 15: Collections, Generics e Map

`RepositorioUsuarioEmMemoria` mantém os usuários em uma `List<Usuario>` e um índice `Map<String, Usuario>` por login. O generics limita os elementos aos tipos declarados. Cada cadastro válido alimenta as duas coleções; um login duplicado lança `IllegalArgumentException` antes de alterar qualquer uma delas. Usuários nulos e logins nulos ou em branco também são rejeitados. `buscarPorLogin` usa o login exato, diferencia maiúsculas de minúsculas e devolve `null` quando não há cadastro. `listarTodos` devolve uma cópia não modificável da lista para proteger a consistência do índice.

`Usuario.equals` e `hashCode` usam o login, pois ele identifica cada cadastro. Dois objetos distintos com o mesmo login são reconhecidos por `List.contains`, mesmo que tenham outros dados diferentes. Antes da implementação de `equals`, o experimento retornou `false`; depois, retornou `true`, comportamento também coberto por `UsuarioTest`. Usuários sem login só são iguais à própria instância. Defina o login antes de cadastrar e mantenha-o estável depois do cadastro, pois ele é a identidade do usuário e a chave do índice.

A busca foi implementada primeiro com um `for` na lista e depois substituída por `Map.get`; as duas versões estão nos commits desta atividade. Comparação:

Com 10 usuários, o `for` pode fazer até 10 comparações (O(n)); o `HashMap` faz a consulta em tempo médio O(1).<br>
Com 10.000 usuários, o `for` pode fazer até 10.000 comparações; o tempo médio da consulta no `HashMap` continua O(1), usando memória adicional para o índice.

A tela de cadastro chama `cadastrar` após validar e preencher o usuário, e só mostra sucesso quando o repositório aceita o cadastro. O login duplicado mostra uma mensagem de erro. A mesma instância do repositório é passada entre as telas de login e cadastro, preservando os registros ao voltar e reabrir o formulário durante a execução. Os dados são mantidos apenas em memória e são perdidos ao encerrar a aplicação.

`RepositorioUsuarioEmMemoriaTest` cobre cadastro, busca entre dois usuários, login inexistente, duplicidade sem alteração dos registros, repositório vazio, proteção da lista e entradas inválidas. `UsuarioTest` cobre igualdade pela lista e consistência do hash. A evidência da suíte completa, incluindo os testes das atividades anteriores, fica em `ATIVIDADES/ATIVIDADE 15 - COLLECTIONS`.

## Atividade 13: Polimorfismo

Os testes de `Session` já verificavam que a sessão delega a perda de tempo ao estado atual e passa a usar outro resultado quando o estado muda. A atividade de polimorfismo torna explícito o outro lado dessa relação: `SessionStateDemo.calculateStrategyTime(double, AbstractSessionState)` recebe um tipo geral e é chamado no `main` com `GreenFlagState`, `SafetyCarState` e `VirtualSafetyCarState`. O método herdado `addPitLaneLossTo` chama `getPitLaneLossSeconds`, que cada estado sobrescreve; por isso a mesma chamada devolve 121,5 s sob bandeira verde e 112,0 s sob Safety Car para um tempo base de 100 s, sem verificar a classe do objeto.

`SessionTest` cobre a delegação e a troca de estado; `SessionStateTest` cobre as implementações dos estados; `SessionStateDemoTest` verifica cada estado pela nova chamada que recebe o tipo geral. Os valores se repetem nos testes porque o objetivo aqui é mostrar por qual caminho o polimorfismo acontece, não introduzir outra regra de cálculo.

### Sobrecarga dos construtores de `Usuario`

- `Usuario()` é usado no cadastro Swing, que cria o objeto após validar as entradas e preenche seus dados com setters.
- `Usuario(nome, cpf, genero, dataNascimento, telefone, email, login, senha)` permite criar um usuário quando todos os dados já estão disponíveis, sem uma sequência de setters.

## Como executar

Requisito: **JDK 25**.

### Executar os testes

Linux/macOS:

```bash
./mvnw clean test
```

Windows:

```bat
mvnw.cmd clean test
```

### Compilar o projeto

```bash
./mvnw package
```

A aplicação gráfica pode ser iniciada pela classe
`br.com.ifba.login.view.TelaLogin` na IDE.


## Stack Tecnológica
- **Java SE 25** (JDK 25)
- **Maven** (Gerenciamento de dependências)
- **Spring Boot 3** & **PostgreSQL** (Persistência e APIs)
