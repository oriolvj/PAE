package com.pae.pae.models;

import java.util.List;
import java.util.Map;

public class CostProjecteDTO {
    private String projectName;
    private double totalCost;
    private List<Map<String, Double>> tecnic_cost;

    public CostProjecteDTO(String projectName, double totalCost, List<Map<String, Double>> tecnic_cost) {
        this.projectName = projectName;
        this.totalCost = totalCost;
        this.tecnic_cost = tecnic_cost;
    }

    // Getters and setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Map<String, Double>> getTecnic_cost() {
        return tecnic_cost;
    }
    public void setTecnic_cost(List<Map<String, Double>> tecnic_cost) {
        this.tecnic_cost = tecnic_cost;
    }
}
