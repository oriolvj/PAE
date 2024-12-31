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



    public UsuariDTO(String username, String nom, Integer edat, Integer tlf, String email, String pwd, Rols rol) {
        this.username = username;
        this.nom = nom;
        this.edat = edat;
        this.tlf = tlf;
        this.email = email;
        this.pwd = pwd;
        this.rol = rol;
    }
}
