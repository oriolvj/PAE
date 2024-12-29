package com.pae.pae.Algorithm.Classes;

import java.util.List;

public class Employee {
    private String name;
    private String technicalProfile;
    private String modality;    // POOL or ALTAS
    private String contractType;    // FULL_TIME or PART_TIME
    private List<String> preferenceActs;
    private List<Requirement> assignedRequirements;


    public Employee(String name, String technicalProfile, String modality, String contractType, List<String> preferenceActs, List<Requirement> assignedRequirements) {
        this.name = name;
        this.technicalProfile = technicalProfile;
        this.modality = modality;
        this.contractType = contractType;
        this.preferenceActs = preferenceActs;
        this.assignedRequirements = assignedRequirements;
    }   

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalProfile() {
        return technicalProfile;
    }

    public void setTechnicalProfile(String technicalProfile) {
        this.technicalProfile = technicalProfile;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public List<String> getPreferenceActs() {
        return preferenceActs;
    }

    public void setPreferenceActs(List<String> preferenceActs) {
        this.preferenceActs = preferenceActs;
    }

    public List<Requirement> getAssignedRequirements() {
        return assignedRequirements;
    }

    public void setAssignedRequirements(List<Requirement> assignedRequirements) {
        this.assignedRequirements = assignedRequirements;
    }

    public void assignRequirement(Requirement requirement) {
        assignedRequirements.add(requirement);
    }

    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", technicalProfile='" + technicalProfile + '\'' +
                ", modality='" + modality + '\'' +
                ", contractType='" + contractType + '\'' +
                ", preferenceActs=" + preferenceActs +
                ", assignedActs=" + assignedRequirements.stream()
                .map(req -> req.getActName()) // Only show the name of the act
                .collect(Collectors.toList()) +
                '}';
    }
}
