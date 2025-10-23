# language: pt
Funcionalidade: Atualização de Consumo
  Como um usuário autenticado da API EnergyControl
  Quero atualizar os registros de consumo de um equipamento
  Para corrigir ou inserir dados

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @AtualizacaoConsumoSucesso
  Cenário: Atualização de consumo bem-sucedida
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) válido para ATUALIZAR o consumo:
      | id        | 21                     |
      | dataHora  | "2025-05-08 21:31:00"  |
      | kwConsumo | 500.0                  |
      | idEquip   | 1                      |
    Quando eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint "/api/consumo/21"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve conter o inteiro "id" igual a 21 do consumo
    E o corpo da resposta deve conter o decimal "kwConsumo" igual a "500.0" do consumo
    E o corpo da resposta deve conter o inteiro "equipamento.idEquip" igual a 1 do consumo
    E o corpo da resposta deve estar de acordo com o JSON Schema de "consumo_sucesso"

  @AtualizacaoConsumoSemAutorizacao
  Cenário: Tentativa de atualizar consumo sem autenticação (Cenário Negativo)
    Dado que eu tenha um payload (corpo) válido para ATUALIZAR o consumo:
      | id        | 21                     |
      | dataHora  | "2025-05-08 21:31:00"  |
      | kwConsumo | 550.0                  |
      | idEquip   | 1                      |
    Quando eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint "/api/consumo/21" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @AtualizacaoConsumoInvalido @ComportamentoIncorretoAPI
  Cenário: Tentativa de atualizar consumo com valor inválido (Teste documentando comportamento atual)
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) inválido para ATUALIZAR o consumo (kw negativo):
      | id        | 21                     |
      | dataHora  | "2025-05-08 21:31:00"  |
      | kwConsumo | -100.0                 |
      | idEquip   | 1                      |
    Quando eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint "/api/consumo/21"
    Então o status code da resposta deve ser 200

  @AtualizacaoConsumoNaoEncontrado
  Cenário: Tentativa de atualizar um consumo que não existe (Cenário Negativo)
    Dado que eu obtenha um token de autenticação
    E que eu tenha um payload (corpo) válido para ATUALIZAR o consumo:
      | id        | 999                    |
      | dataHora  | "2025-05-08 21:31:00"  |
      | kwConsumo | 600.0                  |
      | idEquip   | 1                      |
    Quando eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint "/api/consumo/999"
    Então o status code da resposta deve ser 404
    E o corpo da resposta deve conter uma mensagem de "não encontrado"