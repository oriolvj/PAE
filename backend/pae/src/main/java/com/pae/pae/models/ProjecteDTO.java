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
    private Date dataInici;
    private Date dataFi;
    private int numeroEmpleats;
    private String ubicacio;

    public ProjecteDTO(String nom, Mes mes, Date dataInici, Date dataFi, int numeroEmpleats, String ubicacio) {
        this.nom = nom;
        this.mes = mes;
        this.dataInici = dataInici;
        this.dataFi = dataFi;
        this.numeroEmpleats = numeroEmpleats;
        this.ubicacio = ubicacio;
    }
}
