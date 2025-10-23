package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.AlertaModel;

import static io.restassured.RestAssured.given;

public class AlertaService {

    private String baseUrl = "http://localhost:8080";
    private String endpoint = "/api/alertas";

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
     * Método 1: Cadastra um alerta (Cenário de Sucesso, com Token)
     * Recebe o Model do payload e o Token
     */
    public Response cadastrarAlerta(AlertaModel alertaPayload, String token) {
        String jsonBody = gson.toJson(alertaPayload);

        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token)
                .body(jsonBody);

        return request.post(endpoint);
    }

    /**
     * Método 2: Tenta cadastrar um alerta SEM Token (Cenário 403)
     * Recebe apenas o Model do payload
     */
    public Response cadastrarAlertaSemToken(AlertaModel alertaPayload) {
        String jsonBody = gson.toJson(alertaPayload);

        RequestSpecification request = buildRequest()
                .body(jsonBody);


        return request.post(endpoint);
    }

    /**
     * Método 3: Tenta cadastrar com JSON inválido (Cenário 400 - campo faltando)
     * Recebe uma String JSON (preparada nos Steps) e o Token
     */
    public Response cadastrarAlertaComJson(String jsonPayload, String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token)
                .body(jsonPayload);

        return request.post(endpoint);
    }
}