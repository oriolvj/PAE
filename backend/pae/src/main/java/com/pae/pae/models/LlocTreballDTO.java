package com.pae.pae.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LlocTreballDTO {
    private String posicio;

    public LlocTreballDTO(String posicio) {
        this.posicio = posicio;
    }

    public String getPosicio() {
        return posicio;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }

    //TO DO: Fer tot el CRUD de LlocTreballDTO (create modify, delete) i llistar les posicions
}
