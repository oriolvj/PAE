package com.pae.pae.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class FeinaAssignadaLlocTreballDTO {

    private String nomProjecte;
    private String username;
    private Integer id;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String technicalProfile;

    public FeinaAssignadaLlocTreballDTO(String nomProjecte, String username, Integer id, LocalDate day, LocalTime startTime, LocalTime endTime, String technicalProfile) {
        this.nomProjecte = nomProjecte;
        this.username = username;
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.technicalProfile = technicalProfile;
    }
    // Getters and setters
    public String getNomProjecte() {
        return nomProjecte;
    }

    public void setNomProjecte(String nomProjecte) {
        this.nomProjecte = nomProjecte;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
}
