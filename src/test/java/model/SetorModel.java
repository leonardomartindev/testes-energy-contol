package model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class SetorModel {

    @Expose
    private String nmSetor;

    @Expose
    private String nrAndar;

}