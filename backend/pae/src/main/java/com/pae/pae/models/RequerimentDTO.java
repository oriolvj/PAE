package com.pae.pae.models;

import com.pae.pae.Algorithm.Classes.Employee;

import java.time.LocalDate;
import java.time.LocalTime;

public class RequerimentDTO {
    private Integer id;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String technicalProfile;
    private String actName;
    private String actRoom;

    private String nomProjecte;


    public RequerimentDTO(LocalDate day, LocalTime startTime, LocalTime endTime, String technicalProfile, String actName, String actRoom, String nomProjecte) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.technicalProfile = technicalProfile;
        this.actName = actName;
        this.actRoom = actRoom;
        this.nomProjecte = nomProjecte;
    }

    // Getters and Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getNomProjecte() {
        return nomProjecte;
    }

    public void setNomProjecte(String nomProjecte) {
        this.nomProjecte = nomProjecte;
    }

    public String toString() {
        return "Requirement{" +
                "day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", technicalProfile='" + technicalProfile + '\'' +
                ", actName='" + actName + '\'' +
                ", actRoom='" + actRoom + '\'' +
                ", nomProjecte=" + nomProjecte +
                '}';
    }
}
