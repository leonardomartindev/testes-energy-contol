package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class LogService {

    private String baseUrl = "http://localhost:8080";
    private String endpoint = "/api/logs";

    /**
     * Helper privado para montar a requisição base
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl);
    }

    /**
     * Método 1: Lista os logs (Cenário de Sucesso, com Token)
     * Recebe o Token
     */
    public Response listarLogs(String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token);

        return request.get(endpoint);
    }

    /**
     * Método 2: Tenta listar os logs SEM Token (Cenário 403)
     */
    public Response listarLogsSemToken() {
        RequestSpecification request = buildRequest();

        return request.get(endpoint);
    }
}