package model;
import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class AutenticacaoModel {

    @Expose
    private String email;
    @Expose
    private String password;

}
