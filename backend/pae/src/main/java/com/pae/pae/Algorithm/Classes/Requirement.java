package com.pae.pae.Algorithm.Classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class Requirement {
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String technicalProfile;
    private String actName;
    private String actRoom;
    private Employee assignedEmployee;

    public Requirement(LocalDate day, LocalTime startTime, LocalTime endTime, String technicalProfile, String actName, String actRoom, Employee assignedEmployee) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.technicalProfile = technicalProfile;
        this.actName = actName;
        this.actRoom = actRoom;
        this.assignedEmployee = assignedEmployee;
    }

    // Getters and Setters
    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getTechnicalProfile() {
        return technicalProfile;
    }

    public void setTechnicalProfile(String technicalProfile) {
        this.technicalProfile = technicalProfile;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActRoom() {
        return actRoom;
    }

    public void setActRoom(String actRoom) {
        this.actRoom = actRoom;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public String toString() {
        return "Requirement{" +
                "day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", technicalProfile='" + technicalProfile + '\'' +
                ", actName='" + actName + '\'' +
                ", actRoom='" + actRoom + '\'' +
                ", assignedEmployee=" + (assignedEmployee != null ? assignedEmployee.getName() : "null") + // Only show the name of the employee
                "}\n";
    }
}
