package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class AlertaModel {

    @Expose
    private String dataHora;

    @Expose
    private String descricaoAlerta;

    @Expose
    private int limiteId;

}