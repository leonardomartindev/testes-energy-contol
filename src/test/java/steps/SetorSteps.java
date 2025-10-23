package steps;

import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import model.AutenticacaoModel;
import model.SetorModel;
import services.AutenticacaoService;
import services.SetorService;

import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SetorSteps {

    private AutenticacaoService autenticacaoService = new AutenticacaoService();
    private SetorService setorService = new SetorService();

    private TestContext testContext;

    private AutenticacaoModel credenciaisLogin;
    private SetorModel setorPayload;
    private String jsonPayload;

    public SetorSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Dado("que eu possua credenciais de um usuário válido para autenticação")
    public void queEuPossuaCredenciaisDeUmUsuárioVálidoParaAutenticação() {
        this.credenciaisLogin = new AutenticacaoModel();
        this.credenciaisLogin.setEmail("admin@exemplo.com");
        this.credenciaisLogin.setPassword("admin");
    }

    @Dado("que eu obtenha um token de autenticação")
    public void queEuObtenhaUmTokenDeAutenticação() {
        testContext.setToken(
                autenticacaoService.fazerLogin(this.credenciaisLogin).jsonPath().getString("token")
        );
        assertThat(testContext.getToken(), notNullValue());
    }

    @E("que eu tenha um payload \\(corpo) válido para cadastrar o setor:")
    public void queEuTenhaUmPayloadCorpoVálidoParaCadastrarOSetor(DataTable dataTable) {
        this.setorPayload = carregarSetorDoDataTable(dataTable);
        this.jsonPayload = null;
    }

    @Quando("eu enviar uma requisição POST de CADASTRO DE SETOR para o endpoint {string}")
    public void euEnviarUmaRequisiçãoPOSTParaOEndpoint(String endpoint) {
        String token = testContext.getToken();

        if (this.setorPayload != null) {
            testContext.setResponse(setorService.cadastrarSetor(this.setorPayload, token));
        } else if (this.jsonPayload != null) {
            testContext.setResponse(setorService.cadastrarSetorComJson(this.jsonPayload, token));
        } else {
            throw new IllegalStateException("Nenhum payload foi configurado no step Dado");
        }
    }

    @E("o corpo da resposta deve conter o {string} do setor")
    public void oCorpoDaRespostaDeveConterO(String campo) {
        testContext.getResponse().then().body(campo, notNullValue());
    }

    @E("o corpo da resposta deve conter o {string} igual a {string}")
    public void oCorpoDaRespostaDeveConterOIgualA(String campo, String valor) {
        testContext.getResponse().then().body(campo, equalTo(valor));
    }

    @E("o corpo da resposta deve conter o {string} igual a {int} do setor")
    public void oCorpoDaRespostaDeveConterOIgualA(String campo, int valor) {
        testContext.getResponse().then().body(campo, equalTo(valor));
    }

    @Quando("eu enviar uma requisição POST para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoPOSTParaOEndpointSemAutenticação(String endpoint) {
        testContext.setResponse(setorService.cadastrarSetorSemToken(this.setorPayload));
    }

    @E("o corpo da resposta deve estar vazio ou conter uma mensagem de acesso negado")
    public void oCorpoDaRespostaDeveEstarVazioOuConterUmaMensagemDeAcessoNegado() {
        String responseBody = testContext.getResponse().getBody().asString();

        if (responseBody == null || responseBody.trim().isEmpty()) {
            return;
        }

        testContext.getResponse().then().body(
                either(containsString("Forbidden"))
                        .or(containsString("Access Denied"))
        );
    }

    @E("que eu tenha um payload \\(corpo) inválido sem o campo {string}:")
    public void queEuTenhaUmPayloadCorpoInválidoSemOCampo(String campoIgnorado, DataTable dataTable) {
        this.setorPayload = null;

        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue().replace("\"", ""));
        }
        this.jsonPayload = jsonObject.toString();
    }

    private SetorModel carregarSetorDoDataTable(DataTable dataTable) {
        SetorModel model = new SetorModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        if (data.containsKey("nmSetor")) {
            model.setNmSetor(data.get("nmSetor").replace("\"", ""));
        }
        if (data.containsKey("nrAndar")) {
            model.setNrAndar(data.get("nrAndar").replace("\"", ""));
        }
        return model;
    }
}