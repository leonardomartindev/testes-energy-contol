# language: pt
Funcionalidade: Listagem de Usuários
  Como um usuário autenticado (preferencialmente ADMIN) da API EnergyControl
  Quero consultar os usuários cadastrados no sistema
  Para gerenciamento de acessos

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @ListagemUsuarioSucesso
  Cenário: Listagem de usuários bem-sucedida
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM DE USUARIOS para o endpoint "/api/users"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve ser uma lista (array) de usuários
    E cada item na lista de usuários deve conter "user_id", "user_name", "email" e "user_role"

  @ListagemUsuarioSemAutorizacao
  Cenário: Tentativa de listar usuários sem autenticação (Cenário Negativo)
    Quando eu enviar uma requisição GET de LISTAGEM DE USUARIOS para o endpoint "/api/users" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @ListagemUsuarioContrato
  Cenário: Validação do contrato da listagem de usuários (JSON Schema)
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM DE USUARIOS para o endpoint "/api/users"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve estar de acordo com o JSON Schema de "lista_usuarios_sucesso"