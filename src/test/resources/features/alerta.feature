# language: pt
Funcionalidade: Cadastro de Alertas
  Como um usuário autenticado da API EnergyControl
  Quero cadastrar alertas relacionados a limites de consumo
  Para registrar eventos importantes

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @CadastroAlertaSucesso
  Cenário: Cadastro de alerta bem-sucedido
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) válido para CADASTRAR o alerta:
      | dataHora        | "2025-05-08 21:42:00" |
      | descricaoAlerta | "Teste Alerta 1"      |
      | limiteId        | 1                     |
    Quando eu enviar uma requisição POST de CADASTRO DE ALERTA para o endpoint "/api/alertas"
    Então o status code da resposta deve ser 201
    E o corpo da resposta deve conter o "id" do alerta
    E o corpo da resposta deve conter a "descricaoAlerta" igual a "Teste Alerta 1" do alerta
    E o corpo da resposta deve conter o "limiteId" igual a 1 do alerta
    E o corpo da resposta deve estar de acordo com o JSON Schema de "alerta_sucesso"

  @CadastroAlertaSemAutorizacao
  Cenário: Tentativa de cadastrar alerta sem autenticação (Cenário Negativo)
    Dado que eu tenha um payload (corpo) válido para CADASTRAR o alerta:
      | dataHora        | "2025-05-08 21:50:00" |
      | descricaoAlerta | "Teste Alerta 2"      |
      | limiteId        | 1                     |
    Quando eu enviar uma requisição POST de CADASTRO DE ALERTA para o endpoint "/api/alertas" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @CadastroAlertaInvalido
  Cenário: Tentativa de cadastrar alerta com campo obrigatório faltando (API retorna erro de Limite)
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) inválido para CADASTRAR o alerta sem "descricaoAlerta":
      | dataHora | "2025-05-08 21:55:00" |
      | limiteId | 2                     |
    Quando eu enviar uma requisição POST de CADASTRO DE ALERTA para o endpoint "/api/alertas"
    Então o status code da resposta deve ser 401
    E o corpo da resposta deve conter a mensagem de erro de limite não encontrado com ID 2