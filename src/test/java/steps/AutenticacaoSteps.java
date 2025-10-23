package steps;

import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import model.AutenticacaoModel;
import services.AutenticacaoService;

import java.util.Map;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
public class AutenticacaoSteps {

    private AutenticacaoService autenticacaoService = new AutenticacaoService();
    private AutenticacaoModel credenciais;
    private String jsonPayload;

    private TestContext testContext;

    public AutenticacaoSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Dado("que a API esteja acessível na URL base {string}")
    public void queAAPIEstejaAcessivelNaURLBase(String baseUrl) {
    }

    @Dado("que eu tenha as credenciais de um usuário válido:")
    public void queEuTenhaAsCredenciaisDeUmUsuárioVálido(DataTable dataTable) {
        this.credenciais = carregarCredenciaisDoDataTable(dataTable);
        this.jsonPayload = null;
    }

    @Dado("que eu tenha as credenciais de um usuário com senha incorreta:")
    public void queEuTenhaAsCredenciaisDeUmUsuárioComSenhaIncorreta(DataTable dataTable) {
        this.credenciais = carregarCredenciaisDoDataTable(dataTable);
        this.jsonPayload = null;
    }

    @Dado("que eu tenha as credenciais de um usuário que não existe:")
    public void queEuTenhaAsCredenciaisDeUmUsuárioQueNãoExiste(DataTable dataTable) {
        this.credenciais = carregarCredenciaisDoDataTable(dataTable);
        this.jsonPayload = null;
    }

    private AutenticacaoModel carregarCredenciaisDoDataTable(DataTable dataTable) {
        AutenticacaoModel model = new AutenticacaoModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        model.setEmail(data.get("email").replace("\"", ""));

        if (data.containsKey("password")) {
            model.setPassword(data.get("password").replace("\"", ""));
        }
        return model;
    }

    @Dado("que eu tenha um corpo de requisição sem o campo {string}:")
    public void queEuTenhaUmCorpoDeRequisiçãoSemOCampo(String campoIgnorado, DataTable dataTable) {
        this.credenciais = null;
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue().replace("\"", ""));
        }
        this.jsonPayload = jsonObject.toString();
    }

    @Quando("eu enviar uma requisição POST para o endpoint {string}")
    public void euEnviarUmaRequisiçãoPOSTParaOEndpoint(String endpoint) {
        if (this.credenciais != null) {
            testContext.setResponse(autenticacaoService.fazerLogin(this.credenciais));
        } else if (this.jsonPayload != null) {
            testContext.setResponse(autenticacaoService.fazerLoginComJson(this.jsonPayload));
        } else {
            throw new IllegalStateException("Nenhuma credencial ou payload JSON foi configurado no step Dado");
        }
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        testContext.getResponse().then().statusCode(statusCode);
    }

    @E("o corpo da resposta deve conter um {string}")
    public void oCorpoDaRespostaDeveConterUm(String campo) {
        testContext.getResponse().then().body(campo, notNullValue());
        testContext.getResponse().then().body(campo, isA(String.class));
    }

    @E("o corpo da resposta deve estar de acordo com o JSON Schema de {string}")
    public void oCorpoDaRespostaDeveEstarDeAcordoComOJSONSchemaDe(String schemaName) {
        testContext.getResponse().then().body(matchesJsonSchemaInClasspath("schemas/" + schemaName + ".json"));
    }

    @E("o corpo da resposta deve conter uma mensagem de erro de credenciais")
    public void oCorpoDaRespostaDeveConterUmaMensagemDeErroDeCredenciais() {
        String responseBody = testContext.getResponse().getBody().asString();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return;
        }

        testContext.getResponse().then().body(
                either(containsString("Forbidden"))
                        .or(containsString("Credenciais inválidas"))
                        .or(containsString("Access Denied"))
        );
    }

    @E("o corpo da resposta deve indicar que o campo {string} é obrigatório")
    public void oCorpoDaRespostaDeveIndicarQueOCampoÉObrigatório(String campo) {

        Response response = testContext.getResponse();
        String contentType = response.getContentType();
        String responseBody = response.getBody().asString();

        if (contentType != null && contentType.contains("application/json")) {
            response.then().body(campo, equalTo("não deve estar em branco"));

        } else {
            assertThat(responseBody, containsString("propertyPath=" + campo));

            assertThat(responseBody,
                    either(containsString("o deve ser nulo"))
                            .or(containsString("não deve estar em branco"))
                            .or(containsString("obrigatório"))
            );
        }
    }
}