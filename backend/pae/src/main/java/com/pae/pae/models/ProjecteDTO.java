package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProjecteDTO {
    private String nom;
    private Mes mes;
    private Date data_inici;
    private Date data_fi;
    private int num_empleats;
    private String ubicacio;

    public ProjecteDTO(String nom, Mes mes, Date data_inici, Date data_fi, int num_empleats, String ubicacio) {
        this.nom = nom;
        this.mes = mes;
        this.data_inici = data_inici;
        this.data_fi = data_fi;
        this.num_empleats = num_empleats;
        this.ubicacio = ubicacio;
    }
}
