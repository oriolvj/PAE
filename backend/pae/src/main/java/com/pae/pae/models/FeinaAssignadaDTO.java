package com.pae.pae.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FeinaAssignadaDTO {

    private String nomProjecte;
    private String username;
    private Integer id;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String llocTreball;

    public FeinaAssignadaDTO(String nomProjecte, String username, Integer id, LocalDate day, LocalTime start_time, LocalTime end_time) {
        this.nomProjecte = nomProjecte;
        this.username = username;
        this.id = id;
        this.day = day;
        this.startTime = start_time;
        this.endTime = end_time;
    }

    public FeinaAssignadaDTO(String nomProjecte, String username, Integer id, LocalDate day, LocalTime start_time, LocalTime end_time, String llocTreball) {
        this.nomProjecte = nomProjecte;
        this.username = username;
        this.id = id;
        this.day = day;
        this.startTime = start_time;
        this.endTime = end_time;
        this.llocTreball = llocTreball;
    }

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

    public String getLlocTreball() {
        return llocTreball;
    }

    public void setLlocTreball(String llocTreball) {
        this.llocTreball = llocTreball;
    }
}
