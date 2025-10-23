package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.SetorModel;

import static io.restassured.RestAssured.given;

public class SetorService {

    private String baseUrl = "http://localhost:8080";
    private String endpoint = "/api/setores";

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
     * Método 1: Cadastra um setor (Cenário de Sucesso, com Token)
     * Recebe o Model e o Token
     */
    public Response cadastrarSetor(SetorModel setorPayload, String token) {
        String jsonBody = gson.toJson(setorPayload);

        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token)
                .body(jsonBody);

        return request.post(endpoint);
    }

    /**
     * Método 2: Tenta cadastrar um setor SEM Token (Cenário 403)
     * Recebe apenas o Model
     */
    public Response cadastrarSetorSemToken(SetorModel setorPayload) {
        String jsonBody = gson.toJson(setorPayload);

        RequestSpecification request = buildRequest()
                .body(jsonBody);

        return request.post(endpoint);
    }

    /**
     * Método 3: Tenta cadastrar com JSON inválido (Cenário 400)
     * Recebe uma String JSON e o Token
     */
    public Response cadastrarSetorComJson(String jsonPayload, String token) {
        RequestSpecification request = buildRequest()
                .header("Authorization", "Bearer " + token)
                .body(jsonPayload);

        return request.post(endpoint);
    }
}