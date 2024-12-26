package com.pae.pae.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TecnicDTO {
    private int id;
    private String nom;
    private int hores_contracte;
    private int sou;
    private String posicio;

    public TecnicDTO(int id, String nom, int hores_contracte, int sou, String posicio) {
        this.id = id;
        this.nom = nom;
        this.hores_contracte = hores_contracte;
        this.sou = sou;
        this.posicio = posicio;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getHores_contracte() {
        return hores_contracte;
    }

    public void setHores_contracte(int hores_contracte) {
        this.hores_contracte = hores_contracte;
    }

    public int getSou() {
        return sou;
    }

    public void setSou(int sou) {
        this.sou = sou;
    }

    public String getPosicio() {
        return posicio;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }
}
