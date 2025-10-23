package steps;

import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import model.AlertaModel;
import services.AlertaService;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AlertaSteps {

    private TestContext testContext;
    private AlertaService alertaService = new AlertaService();

    private AlertaModel alertaPayload;
    private String jsonPayload;

    public AlertaSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Carrega o payload VÁLIDO a partir da DataTable (Cenários 1 e 2)
     * Note o escape \\( e \\) e o nome único "...para CADASTRAR o alerta"
     */
    @Dado("que eu tenha um payload \\(corpo) válido para CADASTRAR o alerta:")
    public void queEuTenhaUmPayloadCorpoValidoParaCADASTRAROAlerta(DataTable dataTable) {
        this.alertaPayload = carregarAlertaDoDataTable(dataTable);
        this.jsonPayload = null;
    }

    /**
     * Carrega o payload INVÁLIDO a partir da DataTable (Cenário 3)
     * Note o escape \\( e \\) e o nome único "...inválido...sem {string}"
     */
    @Dado("que eu tenha um payload \\(corpo) inválido para CADASTRAR o alerta sem {string}:")
    public void queEuTenhaUmPayloadCorpoInválidoParaCADASTRAROAlertaSem(String campoIgnorado, DataTable dataTable) {
        this.alertaPayload = null;

        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().equals("limiteId")) {
                jsonObject.addProperty(entry.getKey(), Integer.parseInt(entry.getValue()));
            } else {
                jsonObject.addProperty(entry.getKey(), entry.getValue().replace("\"", ""));
            }
        }
        this.jsonPayload = jsonObject.toString();
    }


    /**
     * Envia a requisição POST (Cenários 1 e 3 - com token)
     * Note o nome único "...POST de CADASTRO DE ALERTA..."
     */
    @Quando("eu enviar uma requisição POST de CADASTRO DE ALERTA para o endpoint {string}")
    public void euEnviarUmaRequisiçãoPOSTDeCADASTRODEALERTAParaOEndpoint(String endpoint) {
        String token = testContext.getToken();

        if (this.alertaPayload != null) {
            testContext.setResponse(alertaService.cadastrarAlerta(this.alertaPayload, token));
        } else if (this.jsonPayload != null) {
            testContext.setResponse(alertaService.cadastrarAlertaComJson(this.jsonPayload, token));
        } else {
            throw new IllegalStateException("Nenhum payload de alerta foi configurado no step Dado");
        }
    }

    /**
     * Envia a requisição POST (Cenário 2 - sem token)
     * Note o nome único "...POST de CADASTRO DE ALERTA... sem autenticação"
     */
    @Quando("eu enviar uma requisição POST de CADASTRO DE ALERTA para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoPOSTDeCADASTRODEALERTAParaOEndpointSemAutenticação(String endpoint) {
        testContext.setResponse(alertaService.cadastrarAlertaSemToken(this.alertaPayload));
    }

    /**
     * Valida a presença de um campo na resposta (Cenário 1)
     * Note o nome único "... {string} do alerta"
     */
    @E("o corpo da resposta deve conter o {string} do alerta")
    public void oCorpoDaRespostaDeveConterODoAlerta(String campo) {
        testContext.getResponse().then().body(campo, notNullValue());
    }

    /**
     * Valida campo de String na resposta (Cenário 1)
     * Note o nome único "... a {string} igual a {string} do alerta"
     */
    @E("o corpo da resposta deve conter a {string} igual a {string} do alerta")
    public void oCorpoDaRespostaDeveConterAIgualADoAlerta(String campo, String valor) {
        testContext.getResponse().then().body(campo, equalTo(valor));
    }

    /**
     * Valida campo numérico (int) na resposta (Cenário 1)
     * Note o nome único "... o {string} igual a {int} do alerta"
     */
    @E("o corpo da resposta deve conter o {string} igual a {int} do alerta")
    public void oCorpoDaRespostaDeveConterOIgualAIntDoAlerta(String campo, int valor) {
        testContext.getResponse().then().body(campo, equalTo(valor));
    }


    private AlertaModel carregarAlertaDoDataTable(DataTable dataTable) {
        AlertaModel model = new AlertaModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        if (data.containsKey("dataHora")) {
            model.setDataHora(data.get("dataHora").replace("\"", ""));
        }
        if (data.containsKey("descricaoAlerta")) {
            model.setDescricaoAlerta(data.get("descricaoAlerta").replace("\"", ""));
        }
        if (data.containsKey("limiteId")) {
            model.setLimiteId(Integer.parseInt(data.get("limiteId")));
        }
        return model;
    }

    /**
     * Valida a mensagem específica de erro quando o Limite ID não é encontrado
     * durante o cadastro de Alerta (Cenário 3)
     */
    @E("o corpo da resposta deve conter a mensagem de erro de limite não encontrado com ID {int}")
    public void oCorpoDaRespostaDeveConterAMensagemDeErroDeLimiteNaoEncontradoComID(int limiteId) {
        String responseBody = testContext.getResponse().getBody().asString();

        assertThat(responseBody, containsString("Limite"));
        assertThat(responseBody, containsString("encontrado"));
        assertThat(responseBody, containsString("ID: " + limiteId));
    }

}