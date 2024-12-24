package com.pae.pae.Algorithm.Classes;

import java.util.List;

// ProjectTemplate class -> Represents a project template with its requirements that habitually are needed by the company
public class ProjectTemplate {
    private String projectName;
    private List<Requirement> requirements;

    public ProjectTemplate(String projectName, List<Requirement> requirements) {
        this.projectName = projectName;
        this.requirements = requirements;
    }   

    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public String toString() {
        return "ProjectTemplate{" +
                "projectName='" + projectName + '\'' +
                ", requirements=" + requirements +
                '}';
    }
}
