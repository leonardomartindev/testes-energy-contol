# language: pt
Funcionalidade: Listagem de Equipamentos
  Como um usuário autenticado da API EnergyControl
  Quero consultar os equipamentos cadastrados
  Para poder gerenciá-los

  Contexto:
    Dado que a API esteja acessível na URL base "http://localhost:8080"
    E que eu possua credenciais de um usuário válido para autenticação

  @ListagemSucesso
  Cenário: Listagem de equipamentos bem-sucedida
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM para o endpoint "/api/equipamentos"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve ser uma lista (array)
    E cada item na lista deve conter "idEquip", "nmEquipamento", "tipo" e "setor"

  @ListagemSemAutorizacao
  Cenário: Tentativa de listar equipamentos sem autenticação (Cenário Negativo)
    Quando eu enviar uma requisição GET de LISTAGEM para o endpoint "/api/equipamentos" sem autenticação
    Então o status code da resposta deve ser 403
    E o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado

  @ListagemContrato
  Cenário: Validação do contrato da listagem de equipamentos (JSON Schema)
    Dado que eu obtenha um token de autenticação
    Quando eu enviar uma requisição GET de LISTAGEM para o endpoint "/api/equipamentos"
    Então o status code da resposta deve ser 200
    E o corpo da resposta deve estar de acordo com o JSON Schema de "lista_equipamentos_sucesso"