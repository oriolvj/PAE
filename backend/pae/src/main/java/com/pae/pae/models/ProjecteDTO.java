package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Date getDataInici() {
        return dataInici;
    }

    public void setDataInici(Date dataInici) {
        this.dataInici = dataInici;
    }

    public Date getDataFi() {
        return dataFi;
    }

    public void setDataFi(Date dataFi) {
        this.dataFi = dataFi;
    }

    public int getNumeroEmpleats() {
        return numeroEmpleats;
    }

    public void setNumeroEmpleats(int numeroEmpleats) {
        this.numeroEmpleats = numeroEmpleats;
    }

    public String getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(String ubicacio) {
        this.ubicacio = ubicacio;
    }
}
