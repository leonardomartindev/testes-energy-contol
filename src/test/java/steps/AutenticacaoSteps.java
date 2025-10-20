package steps;

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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.isA;

public class AutenticacaoSteps {

    private AutenticacaoService autenticacaoService = new AutenticacaoService();
    private AutenticacaoModel credenciais;
    private Response response;

    @Dado("que a API esteja acessível na URL base {string}")
    public void queAAPIEstejaAcessivelNaURLBase(String baseUrl) {
    }

    @Dado("que eu tenha as credenciais de um usuário válido:")
    public void queEuTenhaAsCredenciaisDeUmUsuárioVálido(DataTable dataTable) {

        this.credenciais = new AutenticacaoModel();

        Map<String, String> data = dataTable.asMap(String.class, String.class);

        String email = data.get("email").replace("\"", "" );
        String password = data.get("password").replace("\"", "" );

        this.credenciais.setEmail(email);
        this.credenciais.setPassword(password);
    }

    @Quando("eu enviar uma requisição POST para o endpoint {string}")
    public void euEnviarUmaRequisiçãoPOSTParaOEndpoint(String endpoint) {
        this.response = autenticacaoService.fazerLogin(this.credenciais);
    }
    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @E("o corpo da resposta deve conter um {string}")
    public void oCorpoDaRespostaDeveConterUm(String campo) {
        response.then().body(campo, notNullValue());
        response.then().body(campo, isA(String.class));
    }
    @E("o corpo da resposta deve estar de acordo com o JSON Schema de {string}")
    public void oCorpoDaRespostaDeveEstarDeAcordoComOJSONSchemaDe(String schemaName) {
        response.then().body(matchesJsonSchemaInClasspath("schemas/" + schemaName + ".json"));
    }



}
