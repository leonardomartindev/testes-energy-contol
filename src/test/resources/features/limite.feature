# language: pt
Funcionalidade: Consulta de Limite Específico
  Como um usuário autenticado da API EnergyControl
  Quero consultar os detalhes de um limite específico
  Para verificar sua configuração

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @ConsultaLimiteSucesso
  Cenário: Consulta de limite específico bem-sucedida
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de CONSULTA para o endpoint "/api/limites/1"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter o "idLmt" igual a 1 do limite
    E o corpo da resposta deve conter o "ltConsumo" do limite
    E o corpo da resposta deve conter o "equipamento" do limite
    E o corpo da resposta deve estar de acordo com o JSON Schema de "limite_sucesso"

  @ConsultaLimiteSemAutorizacao
  Cenário: Tentativa de consultar limite sem autenticação (Cenário Negativo)
    Quando eu enviar uma requisição GET de CONSULTA para o endpoint "/api/limites/1" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @ConsultaLimiteNaoEncontrado
  Cenário: Tentativa de consultar um limite que não existe (Cenário Negativo)
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de CONSULTA para o endpoint "/api/limites/999"
    Então o status code da resposta deve ser 404
    E o corpo da resposta deve conter uma mensagem de "não encontrado"