package steps;

import io.restassured.response.Response;
import lombok.Data;

@Data
public class TestContext {

    private String token;
    private Response response;

}