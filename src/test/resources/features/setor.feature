# language: pt
Funcionalidade: Cadastro de Setores
  Como um usuário autenticado da API EnergyControl
  Quero cadastrar, consultar, atualizar e deletar setores
  Para gerenciar a estrutura organizacional

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @CadastroSucesso
  Cenário: Cadastro de setor bem-sucedido (Caminho Feliz)
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) válido para cadastrar o setor:
      | campo     | valor           |
      | nmSetor   | "Administração" |
      | nrAndar   | "1"             |
    Quando eu enviar uma requisição POST de CADASTRO DE SETOR para o endpoint "/api/setores"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter o "idSet" do setor
    E o corpo da resposta deve conter o "nmSetor" igual a "Administração"
    E o corpo da resposta deve conter o "nrAndar" igual a 1 do setor

  @CadastroSemAutorizacao
  Cenário: Tentativa de cadastro de setor sem autenticação (Cenário Negativo)
    Dado que eu tenha um payload (corpo) válido para cadastrar o setor:
      | campo     | valor      |
      | nmSetor   | "Engenharia" |
      | nrAndar   | "2"        |
    Quando eu enviar uma requisição POST para o endpoint "/api/setores" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @CadastroInvalido
  Cenário: Tentativa de cadastro de setor com campo obrigatório faltando (Teste de Validação)
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) inválido sem o campo "nmSetor":
      | campo   | valor |
      | nrAndar | "3"   |
    Quando eu enviar uma requisição POST de CADASTRO DE SETOR para o endpoint "/api/setores"
    Então o status code da resposta deve ser 401
    E o corpo da resposta deve indicar que o campo "nmSetor" é obrigatório