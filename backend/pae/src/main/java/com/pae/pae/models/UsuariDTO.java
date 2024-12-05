package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuariDTO {

    private String username;
    private String nom;
    private Integer edat;
    private Integer tlf;
    private String email;
    private String pwd;
    private Rols rol;
    private String preferencia;
    private boolean actiu;
    private boolean contractat;//True si es de l'empresa, false altrement
    private Jornada jornda;

    public UsuariDTO(String username, String nom, Integer edat, Integer tlf, String email, String pwd, Rols rol, String preferencia, boolean actiu, boolean contractat, Jornada jornada) {
        this.username = username;
        this.nom = nom;
        this.edat = edat;
        this.tlf = tlf;
        this.email = email;
        this.pwd = pwd;
        this.rol = rol;
        this.preferencia = preferencia;
        this.actiu = actiu;
        this.contractat = contractat;
        this.jornda = jornada;
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

    public Rols getRol() {
        return rol;
    }

    public void setRol(Rols rol) {
        this.rol = rol;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }
    public boolean getActiu() {
        return actiu;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public boolean isActiu() {
        return actiu;
    }

    public boolean isContractat() {
        return contractat;
    }

    public Jornada getJornda() {
        return jornda;
    }

    public void setContractat(boolean contractat) {
        this.contractat = contractat;
    }

    public void setJornda(Jornada jornda) {
        this.jornda = jornda;
    }
}
