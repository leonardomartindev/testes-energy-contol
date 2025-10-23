package steps;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import services.UsuarioService;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class UsuarioSteps {

    private TestContext testContext;
    private UsuarioService usuarioService = new UsuarioService();

    public UsuarioSteps(TestContext testContext) {
        this.testContext = testContext;
    }


    /**
     * Cenário 1 e 3 (com token)
     * Note o nome único "...GET de LISTAGEM DE USUARIOS..."
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM DE USUARIOS para o endpoint {string}")
    public void euEnviarUmaRequisiçãoGETDeLISTAGEMDEUSUARIOSParaOEndpoint(String endpoint) {
        String token = testContext.getToken();
        testContext.setResponse(
                usuarioService.listarUsuarios(token)
        );
    }

    /**
     * Cenário 2 (sem token)
     * Note o nome único "...GET de LISTAGEM DE USUARIOS... sem autenticação"
     */
    @Quando("eu enviar uma requisição GET de LISTAGEM DE USUARIOS para o endpoint {string} sem autenticação")
    public void euEnviarUmaRequisiçãoGETDeLISTAGEMDEUSUARIOSParaOEndpointSemAutenticação(String endpoint) {
        testContext.setResponse(
                usuarioService.listarUsuariosSemToken()
        );
    }

    /**
     * Cenário 1 (Validação da lista)
     * Note o nome único "...lista \\(array\\) de usuários" e o escape dos parênteses
     */
    @E("o corpo da resposta deve ser uma lista \\(array\\) de usuários")
    public void oCorpoDaRespostaDeveSerUmaListaArrayDeUsuarios() {
        testContext.getResponse().then()
                .body("", instanceOf(List.class));
    }

    /**
     * Cenário 1 (Validação dos campos em cada item)
     * Note o nome único "...na lista de usuários deve conter..."
     */
    @E("cada item na lista de usuários deve conter {string}, {string}, {string} e {string}")
    public void cadaItemNaListaDeUsuariosDeveConterEEE(String key1, String key2, String key3, String key4) {
        testContext.getResponse().then()
                .body("[0]", hasKey(key1))
                .body("[0]", hasKey(key2))
                .body("[0]", hasKey(key3))
                .body("[0]", hasKey(key4));
    }

}