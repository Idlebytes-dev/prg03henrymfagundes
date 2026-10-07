# Atividade 15 - Collections

![Suíte completa: 35 testes passaram, sem falhas, erros ou testes ignorados](greentests15.png)

O print do terminal mostra os resultados da suíte completa: 35 testes passaram, incluindo os das atividades anteriores, sem falhas, erros ou testes ignorados, com `BUILD SUCCESS`.

Verificação: `./mvnw -B package` compilou o projeto, executou a suíte completa e gerou o JAR com sucesso usando Java 25. Para repetir localmente, execute `./mvnw clean test`.

A verificação da interface Swing também confirmou cadastro com sucesso, rejeição de login duplicado sem substituir o usuário original e preservação da mesma instância do repositório ao navegar cadastro → login → cadastro e cadastrar um segundo usuário.
