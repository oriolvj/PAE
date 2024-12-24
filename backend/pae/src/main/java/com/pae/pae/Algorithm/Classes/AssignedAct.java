package com.pae.pae.Algorithm.Classes;

public class AssignedAct {
    private Employee employee;
    private Requirement requirement;

    public AssignedAct(Employee employee, Requirement requirement) {
        this.employee = employee;
        this.requirement = requirement;
    }

    // Getters and Setters  
    public Employee getEmployee() {
        return employee;
    }   

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }   

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public String toString() {
        return "AssignedAct{" +
                "employee=" + employee +
                ", requirement=" + requirement +
                '}';
    }
}
