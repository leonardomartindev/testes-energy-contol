package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class UsuarioService {

    private String baseUrl = "http://localhost:8080";
    private String endpoint = "/api/users";

    /**
     * Helper privado para montar a requisição base
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl);
    }

    /**
     * Método 1: Lista os usuários (Cenário de Sucesso, com Token)
     * Recebe o Token
     */
    public Response listarUsuarios(String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token);

        return request.get(endpoint);
    }

    /**
     * Método 2: Tenta listar os usuários SEM Token (Cenário 403)
     */
    public Response listarUsuariosSemToken() {
        RequestSpecification request = buildRequest();

        return request.get(endpoint);
    }
}