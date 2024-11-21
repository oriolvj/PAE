package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuariDTO {

    enum Rols {
        ADMINISTRADOR,
        GESTOR_PROJECTE,
        TREBALLADOR
    }

    private String username;
    private String nom;
    private Integer edat;
    private Integer tlf;
    private String email;
    private String pwd;
    private Boolean administrador;
    private Rols rol;

    public UsuariDTO(String username, String nom, Integer edat, Integer tlf, String email, String pwd, Boolean administrador, Rols rol) {
        this.username = username;
        this.nom = nom;
        this.edat = edat;
        this.tlf = tlf;
        this.email = email;
        this.pwd = pwd;
        this.administrador = administrador;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getEdat() {
        return edat;
    }

    public void setEdat(Integer edat) {
        this.edat = edat;
    }

    public Integer getTlf() {
        return tlf;
    }

    public void setTlf(Integer tlf) {
        this.tlf = tlf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public Rols getRol() {
        return rol;
    }

    public void setRol(Rols rol) {
        this.rol = rol;
    }
}
