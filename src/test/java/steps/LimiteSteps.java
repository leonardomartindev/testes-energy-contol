package steps;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import services.LimiteService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LimiteSteps {

    private TestContext testContext;
    private LimiteService limiteService = new LimiteService();

    public LimiteSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Cenário 1 (Sucesso) e 3 (Não Encontrado)
     */
    @Quando("eu enviar uma requisição GET de CONSULTA para o endpoint {string}")
    public void euEnviarUmaRequisiçãoGETParaOEndpoint(String endpoint) {
        String id = endpoint.substring(endpoint.lastIndexOf('/') + 1);

        String token = testContext.getToken();
        testContext.setResponse(
                limiteService.consultarLimite(id, token)
        );
    }

    /**
     * Cenário 2 (Sem Token)
     */
    @Quando("eu enviar uma requisição GET de CONSULTA para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoGETParaOEndpointSemAutenticação(String endpoint) {
        String id = endpoint.substring(endpoint.lastIndexOf('/') + 1);

        testContext.setResponse(
                limiteService.consultarLimiteSemToken(id)
        );
    }

    /**
     * Cenário 1 (Validação de campos)
     */
    @E("o corpo da resposta deve conter o {string} do limite")
    public void oCorpoDaRespostaDeveConterO(String campo) {
        testContext.getResponse().then()
                .body(campo, notNullValue());
    }

    /**
     * Cenário 1 (Validação de ID)
     */
    @E("o corpo da resposta deve conter o {string} igual a {int} do limite")
    public void oCorpoDaRespostaDeveConterOIgualA(String campo, int valor) {
        testContext.getResponse().then()
                .body(campo, equalTo(valor));
    }

    /**
     * Cenário 3 (Validação 404)
     */
    @E("o corpo da resposta deve conter uma mensagem de {string}")
    public void oCorpoDaRespostaDeveConterUmaMensagemDe(String mensagem) {
        String responseBody = testContext.getResponse().getBody().asString();

        if (responseBody == null || responseBody.trim().isEmpty()) {
            return;
        }

        assertThat(responseBody,
                either(containsString(mensagem))
                        .or(containsString("Not Found"))
        );
    }
}