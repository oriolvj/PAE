package com.pae.pae.models;

import java.util.Date;

public class ProjecteDTO {
    private String nom;
    private Mes mes;
    private Setmana setmana;
    private Date data_inici;
    private Date data_fi;
    private int num_empleats;
    private String ubicacio;

    public ProjecteDTO(String nom, Mes mes, Setmana setmana, Date data_inici, Date data_fi, int num_empleats, String ubicacio) {
        this.nom = nom;
        this.mes = mes;
        this.setmana = setmana;
        this.data_inici = data_inici;
        this.data_fi = data_fi;
        this.num_empleats = num_empleats;
        this.ubicacio = ubicacio;
    }

    public String getNom() {
        return nom;
    }

    public Mes getMes() {
        return mes;
    }

    public Setmana getSetmana() {
        return setmana;
    }

    public Date getData_inici() {
        return data_inici;
    }

    public Date getData_fi() {
        return data_fi;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public void setSetmana(Setmana setmana) {
        this.setmana = setmana;
    }

    public void setData_inici(Date data_inici) {
        this.data_inici = data_inici;
    }

    public void setData_fi(Date data_fi) {
        this.data_fi = data_fi;
    }

    public int getNum_empleats() {
        return num_empleats;
    }

    public void setNum_empleats(int num_empleats) {
        this.num_empleats = num_empleats;
    }

    public String getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(String ubicacio) {
        this.ubicacio = ubicacio;
    }
}
