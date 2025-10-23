# language: pt
Funcionalidade: Listagem de Logs
  Como um usuário autenticado da API EnergyControl
  Quero consultar os logs do sistema
  Para auditoria e acompanhamento de eventos

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @ListagemLogSucesso
  Cenário: Listagem de logs bem-sucedida
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM DE LOGS para o endpoint "/api/logs"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve ser uma lista (array) de logs
    E cada item na lista de logs deve conter "idSet", "logMessage", "dateInc" e "tempoInc"

  @ListagemLogSemAutorizacao
  Cenário: Tentativa de listar logs sem autenticação (Cenário Negativo)
    Quando eu enviar uma requisição GET de LISTAGEM DE LOGS para o endpoint "/api/logs" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @ListagemLogContrato
  Cenário: Validação do contrato da listagem de logs (JSON Schema)
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM DE LOGS para o endpoint "/api/logs"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve estar de acordo com o JSON Schema de "lista_logs_sucesso"