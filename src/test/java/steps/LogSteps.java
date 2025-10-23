package steps;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import services.LogService;

import static org.hamcrest.Matchers.*;

import java.util.List;

public class LogSteps {

    private TestContext testContext;
    private LogService logService = new LogService();

    public LogSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Cenário 1 e 3 (com token)
     * Note o nome único "...GET de LISTAGEM DE LOGS..."
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM DE LOGS para o endpoint {string}")
    public void euEnviarUmaRequisiçãoGETDeLISTAGEMDELOGSParaOEndpoint(String endpoint) {
        String token = testContext.getToken();
        testContext.setResponse(
                logService.listarLogs(token)
        );
    }

    /**
     * Cenário 2 (sem token)
     * Note o nome único "...GET de LISTAGEM DE LOGS... sem autenticação"
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM DE LOGS para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoGETDeLISTAGEMDELOGSParaOEndpointSemAutenticação(String endpoint) {
        testContext.setResponse(
                logService.listarLogsSemToken()
        );
    }

    /**
     * Cenário 1 (Validação da lista)
     * Note o nome único "...lista \\(array\\) de logs" e o escape dos parênteses
     */
    @E("o corpo da resposta deve ser uma lista \\(array\\) de logs")
    public void oCorpoDaRespostaDeveSerUmaListaArrayDeLogs() {
        testContext.getResponse().then()
                .body("", instanceOf(List.class));
    }

    /**
     * Cenário 1 (Validação dos campos em cada item)
     * Note o nome único "...na lista de logs deve conter..."
     */
    @E("cada item na lista de logs deve conter {string}, {string}, {string} e {string}")
    public void cadaItemNaListaDeLogsDeveConterEE(String key1, String key2, String key3, String key4) {
        testContext.getResponse().then()
                .body("[0]", hasKey(key1))
                .body("[0]", hasKey(key2))
                .body("[0]", hasKey(key3))
                .body("[0]", hasKey(key4));
    }

}