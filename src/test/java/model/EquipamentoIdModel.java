package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class EquipamentoIdModel {

    @Expose
    private int idEquip;
}