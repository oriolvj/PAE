package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
    private Jornada jornada;

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
        this.jornada = jornada;
    }
}
