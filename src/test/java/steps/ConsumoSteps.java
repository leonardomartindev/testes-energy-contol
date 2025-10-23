package steps;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import model.ConsumoModel;
import model.EquipamentoIdModel;
import services.ConsumoService;

import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConsumoSteps {

    private TestContext testContext;
    private ConsumoService consumoService = new ConsumoService();

    private ConsumoModel consumoPayload;

    public ConsumoSteps(TestContext testContext) {
        this.testContext = testContext;
    }


    @Dado("que eu tenha um payload \\(corpo) válido para ATUALIZAR o consumo:")
    public void queEuTenhaUmPayloadCorpoValidoParaATUALIZAROConsumo(DataTable dataTable) {
        this.consumoPayload = carregarConsumoDoDataTable(dataTable);
    }

    @Dado("que eu tenha um payload \\(corpo) inválido para ATUALIZAR o consumo \\(kw negativo):")
    public void queEuTenhaUmPayloadCorpoInvalidoParaATUALIZAROConsumoKwNegativo(DataTable dataTable) {
        this.consumoPayload = carregarConsumoDoDataTable(dataTable);
    }

    @Quando("eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint {string}")
    public void euEnviarUmaRequisiçãoPUTDeATUALIZACAODECONSUMOParaOEndpoint(String endpoint) {
        String id = endpoint.substring(endpoint.lastIndexOf('/') + 1);
        String token = testContext.getToken();
        testContext.setResponse(
                consumoService.atualizarConsumo(id, this.consumoPayload, token)
        );
    }

    @Quando("eu enviar uma requisição PUT de ATUALIZACAO DE CONSUMO para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoPUTDeATUALIZACAODECONSUMOParaOEndpointSemAutenticação(String endpoint) {
        String id = endpoint.substring(endpoint.lastIndexOf('/') + 1);
        testContext.setResponse(
                consumoService.atualizarConsumoSemToken(id, this.consumoPayload)
        );
    }

    /**
     * ADICIONADO DE VOLTA: Valida campo numérico (double) na resposta (Cenário 1)
     * Anotação ATUALIZADA para "...o decimal..."
     */
    @E("o corpo da resposta deve conter o decimal {string} igual a {string} do consumo")
    public void oCorpoDaRespostaDeveConterODecimalIgualAStringDoConsumo(String campo, String valorEsperadoString) {


        float valorEsperadoFloat = Float.parseFloat(valorEsperadoString);
        testContext.getResponse().then()
                .body(campo, equalTo(valorEsperadoFloat));
    }

    /**
     * Valida campo numérico (int) na resposta (Cenário 1)
     * Anotação ATUALIZADA para "...o inteiro..."
     */
    @E("o corpo da resposta deve conter o inteiro {string} igual a {int} do consumo")
    public void oCorpoDaRespostaDeveConterOInteiroIgualAIntDoConsumo(String campo, int valor) {
        testContext.getResponse().then()
                .body(campo, equalTo(valor));
    }

    /**
     * Valida mensagem de erro para campo inválido (Cenário 3)
     */
    @E("o corpo da resposta deve indicar que o campo {string} é inválido")
    public void oCorpoDaRespostaDeveIndicarQueOCampoÉInválido(String campo) {
        String responseBody = testContext.getResponse().getBody().asString();

        assertThat(responseBody, containsString(campo));
        assertThat(responseBody,
                either(containsString("inválido"))
                        .or(containsString("negativo"))
                        .or(containsString("positive"))
                        .or(containsString("maior que zero"))
        );
    }


    private ConsumoModel carregarConsumoDoDataTable(DataTable dataTable) {
        ConsumoModel model = new ConsumoModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        model.setId(Integer.parseInt(data.get("id")));
        model.setDataHora(data.get("dataHora").replace("\"", ""));
        model.setKwConsumo(Double.parseDouble(data.get("kwConsumo")));
        int idEquip = Integer.parseInt(data.get("idEquip"));
        model.setEquipamento(new EquipamentoIdModel(idEquip));
        return model;
    }
}