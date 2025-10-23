package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class LimiteService {

    private String baseUrl = "http://localhost:8080";
    private String endpointBase = "/api/limites"; // O endpoint base

    /**
     * Helper privado para montar a requisição base
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl);
        // Nota: Não precisamos de Content-Type para requisições GET
    }

    /**
     * Método 1: Consulta um limite (Cenário de Sucesso ou 404, com Token)
     * Recebe o ID do limite e o Token
     */
    public Response consultarLimite(String id, String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token);

        return request.get(endpointBase + "/" + id);
    }

    /**
     * Método 2: Tenta consultar um limite SEM Token (Cenário 403)
     * Recebe apenas o ID
     */
    public Response consultarLimiteSemToken(String id) {
        RequestSpecification request = buildRequest();

        return request.get(endpointBase + "/" + id);
    }
}