package com.pae.pae.Algorithm.Classes;

import java.time.LocalDate;
import java.util.List; 

public class ProjectForm {
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Requirement> requirements;
    
    public ProjectForm(String projectName, LocalDate startDate, LocalDate endDate, List<Requirement> requirements) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requirements = requirements;
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

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public String toString() {
        return "ProjectForm{" +
                "projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", requirements=" + requirements +
                '}';
    }

}
