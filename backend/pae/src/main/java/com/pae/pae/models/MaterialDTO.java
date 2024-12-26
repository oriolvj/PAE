package com.pae.pae.models;

public class MaterialDTO {
    private int id;
    private String marca;
    private String model;
    private int quantitat;
    private  int preu_lloguer;
    private boolean propietari; //true si Lavinia es propietari, false si no es de l'empresa

    public MaterialDTO(int id, String marca, String model, int quantitat, int preu_lloguer, boolean propietari) {
        this.id = id;
        this.marca = marca;
        this.model = model;
        this.quantitat = quantitat;
        this.preu_lloguer = preu_lloguer;
        this.propietari = propietari;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }

    public int getPreu_lloguer() {
        return preu_lloguer;
    }

    public void setPreu_lloguer(int preu_lloguer) {
        this.preu_lloguer = preu_lloguer;
    }

    public boolean isPropietari() {
        return propietari;
    }

    public void setPropietari(boolean propietari) {
        this.propietari = propietari;
    }
}
