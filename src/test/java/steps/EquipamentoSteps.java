package steps;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import services.EquipamentoService;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class EquipamentoSteps {

    private TestContext testContext;
    private EquipamentoService equipamentoService = new EquipamentoService();

    public EquipamentoSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Cenários 1 e 3 (com token)
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM para o endpoint {string}")
    public void euEnviarUmaRequisiçãoGETParaOEndpoint(String endpoint) {
        // Busca o token do contexto
        String token = testContext.getToken();
        // Salva a resposta no contexto
        testContext.setResponse(
                equipamentoService.listarEquipamentos(token)
        );
    }

    /**
     * Cenário 2 (sem token)
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoGETParaOEndpointSemAutenticação(String endpoint) {
        // Salva a resposta no contexto
        testContext.setResponse(
                equipamentoService.listarEquipamentosSemToken()
        );
    }

    /**
     * Cenário 1 (Validação da lista)
     */
    @E("o corpo da resposta deve ser uma lista \\(array)")
    public void oCorpoDaRespostaDeveSerUmaListaArray() {
        testContext.getResponse().then()
                .body("", instanceOf(List.class));
    }

    /**
     * Cenário 1 (Validação dos campos)
     */
    @E("cada item na lista deve conter {string}, {string}, {string} e {string}")
    public void cadaItemNaListaDeveConterEE(String key1, String key2, String key3, String key4) {
        testContext.getResponse().then()
                .body("[0]", hasKey(key1))
                .body("[0]", hasKey(key2))
                .body("[0]", hasKey(key3))
                .body("[0]", hasKey(key4));
    }

}