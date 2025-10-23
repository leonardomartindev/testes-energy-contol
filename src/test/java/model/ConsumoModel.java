package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class ConsumoModel {

    @Expose
    private int id;

    @Expose
    private String dataHora;

    @Expose
    private double kwConsumo;

    @Expose
    private EquipamentoIdModel equipamento;

}