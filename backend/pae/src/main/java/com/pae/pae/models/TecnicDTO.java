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
    private int sou;
    private String posicio;
    private String preferencia;
    private boolean actiu;
    private boolean contractat;//True si es de l'empresa, false altrement
    private Jornada jornada;

    public TecnicDTO(int id, String nom, int sou, String posicio, String preferencia, boolean actiu, boolean contractat, Jornada jornada) {
        this.id = id;
        this.nom = nom;
        this.sou = sou;
        this.posicio = posicio;
        this.preferencia = preferencia;
        this.actiu = actiu;
        this.contractat = contractat;
        this.jornada = jornada;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public boolean isContractat() {
        return contractat;
    }

    public void setContractat(boolean contractat) {
        this.contractat = contractat;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
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
