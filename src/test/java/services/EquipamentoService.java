package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class EquipamentoService {

    private String baseUrl = "http://localhost:8080";
    private String endpoint = "/api/equipamentos";

    /**
     * Helper privado para montar a requisição base
     * (Não definimos Content-Type aqui, pois é um GET)
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl);
    }

    /**
     * Método 1: Lista os equipamentos (Cenário de Sucesso, com Token)
     */
    public Response listarEquipamentos(String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token); // Adiciona o token

        return request.get(endpoint);
    }

    /**
     * Método 2: Tenta listar os equipamentos SEM Token (Cenário 403)
     */
    public Response listarEquipamentosSemToken() {
        RequestSpecification request = buildRequest();


        return request.get(endpoint);
    }
}