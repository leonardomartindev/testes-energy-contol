# language: pt
Funcionalidade: Autenticação de Usuário
  Como um usuário da API EnergyControl
  Quero me autenticar no sistema
  Para obter um token de acesso e poder utilizar as rotas protegidas

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"

  Cenário: Autenticação bem-sucedida (Caminho Feliz)
    Dado que eu tenha as credenciais de um usuário válido:
      | campo    | valor               |
      | email    | "admin@exemplo.com" |
      | password | "admin"             |
    Quando eu enviar uma requisição POST para o endpoint "/auth/login"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter um "token"
    E o corpo da resposta deve estar de acordo com o JSON Schema de "autenticacao_sucesso"

  Cenário: Tentativa de autenticação com senha incorreta (Cenário Negativo)
    Dado que eu tenha as credenciais de um usuário com senha incorreta:
      | campo    | valor               |
      | email    | "admin@exemplo.com" |
      | password | "senhaErrada123"    |
    Quando eu enviar uma requisição POST para o endpoint "/auth/login"
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve conter uma mensagem de erro de credenciais

  Cenário: Tentativa de autenticação com email inexistente (Cenário Negativo)
    Dado que eu tenha as credenciais de um usuário que não existe:
      | campo    | valor                   |
      | email    | "naoexisto@exemplo.com" |
      | password | "qualquerSenha"         |
    Quando eu enviar uma requisição POST para o endpoint "/auth/login"
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve conter uma mensagem de erro de credenciais

  Cenário: Tentativa de autenticação sem informar um campo obrigatório (Teste de Contrato/Validação)
    Dado que eu tenha um corpo de requisição sem o campo "password":
      | campo | valor               |
      | email | "admin@exemplo.com" |
    Quando eu enviar uma requisição POST para o endpoint "/auth/login"
    Então o status code da resposta deve ser 400
    E o corpo da resposta deve indicar que o campo "password" é obrigatório