package com.pae.pae.models;

import java.util.List;
import java.util.Map;

public class CostosDTO {
    private double cost_ma_obra;
    private double cost_material;
    private double cost_total;
    private List<CostProjecteDTO> projectes_cost;

    public CostosDTO(double cost_ma_obra, double cost_material, double cost_total, List<CostProjecteDTO> projectes_cost) {
        this.cost_ma_obra = cost_ma_obra;
        this.cost_material = cost_material;
        this.cost_total = cost_total;
        this.projectes_cost = projectes_cost;
    }

    public List<CostProjecteDTO> getProjectes_cost() {
        return projectes_cost;
    }

    public void setProjectes_cost(List<CostProjecteDTO> projectes_cost) {
        this.projectes_cost = projectes_cost;
    }

    public double getCost_ma_obra() {
        return cost_ma_obra;
    }

    public void setCost_ma_obra(double cost_ma_obra) {
        this.cost_ma_obra = cost_ma_obra;
    }

    public double getCost_material() {
        return cost_material;
    }

    public void setCost_material(double cost_material) {
        this.cost_material = cost_material;
    }

    public double getCost_total() {
        return cost_total;
    }

    public void setCost_total(double cost_total) {
        this.cost_total = cost_total;
    }
}
