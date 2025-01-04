package com.pae.pae.models;

import org.antlr.v4.runtime.misc.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RegistreHoresProjecteDTO {
    private String projectName;
    private double totalHours;
    private List<Map<String, Double>> tecnic_hours; //llista amb cada empleat i les hores que ha treballat aquella setmana

    public RegistreHoresProjecteDTO(String projectName, double totalHours, List<Map<String, Double>> tecnic_hours) {
        this.projectName = projectName;
        this.totalHours = totalHours;
        this.tecnic_hours = tecnic_hours;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public List<Map<String, Double>> getTecnic_hours() {
        return tecnic_hours;
    }

    public void setTecnic_hours(List<Map<String, Double>> tecnic_hours) {
        this.tecnic_hours = tecnic_hours;
    }
}
