package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.ConsumoModel;

import static io.restassured.RestAssured.given;

public class ConsumoService {

    private String baseUrl = "http://localhost:8080";
    private String endpointBase = "/api/consumo";

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    /**
     * Helper privado para montar a requisição base
     * com contentType JSON
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json");
    }

    /**
     * Método 1: Atualiza um registro de consumo (Cenários de Sucesso, 400, 404 - com Token)
     * Recebe o ID do consumo, o Model do payload e o Token
     */
    public Response atualizarConsumo(String id, ConsumoModel consumoPayload, String token) {
        String jsonBody = gson.toJson(consumoPayload);

        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token)
                .body(jsonBody);

        return request.put(endpointBase + "/" + id);
    }

    /**
     * Método 2: Tenta atualizar um consumo SEM Token (Cenário 403)
     * Recebe o ID e o Model do payload
     */
    public Response atualizarConsumoSemToken(String id, ConsumoModel consumoPayload) {
        String jsonBody = gson.toJson(consumoPayload);

        RequestSpecification request = buildRequest()
                .body(jsonBody);


        return request.put(endpointBase + "/" + id);
    }
}