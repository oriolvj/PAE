package com.pae.pae.Algorithm.Classes;

import java.time.LocalDate;
import java.util.List;

public class Project {
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Requirement> assignedRequirements;
    
    public Project(String projectName, LocalDate startDate, LocalDate endDate, List<Requirement> assignedRequirements) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignedRequirements = assignedRequirements;
    }   

    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Requirement> getAssignedRequirements() {
        return assignedRequirements;
    }

    public void setAssignedRequirements(List<Requirement> assignedRequirements) {
        this.assignedRequirements = assignedRequirements;
    }

    public void addRequirement(Requirement requirement) {
        this.assignedRequirements.add(requirement);
    }

    public String toString() {
        return "Project{" +
                "projectName='" + projectName + '\'' +
                ", assignedRequirements=" + requirements +
                "}\n";
    }
}
