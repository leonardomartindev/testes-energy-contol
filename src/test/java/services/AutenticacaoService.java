package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.AutenticacaoModel;

import static io.restassured.RestAssured.given;

public class AutenticacaoService {

    private String baseUrl = "http://localhost:8080";

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    /**
     * Helper privado para montar a requisição base
     */
    private RequestSpecification buildRequest() {
        return given()
                .baseUri(baseUrl)
                .contentType("application/json");
    }

    /**
     * Método 1: Faz login usando o Model (para cenários válidos)
     */
    public Response fazerLogin(AutenticacaoModel credenciais) {
        String jsonBody = gson.toJson(credenciais);
        RequestSpecification request = buildRequest().body(jsonBody);

        return request.post("/auth/login");
    }

    /**
     * Método 2: Faz login usando uma String JSON (para cenários negativos/inválidos)
     */
    public Response fazerLoginComJson(String jsonPayload) {
        RequestSpecification request = buildRequest().body(jsonPayload);

        return request.post("/auth/login");
    }
}