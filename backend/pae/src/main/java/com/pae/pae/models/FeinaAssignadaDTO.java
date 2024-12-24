package com.pae.pae.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FeinaAssignadaDTO {

    private String nomProjecte;
    private String username;
    private Integer id;
    private LocalDate day;
    private LocalTime start_time;
    private LocalTime end_time;

    public FeinaAssignadaDTO(String nomProjecte, String username, Integer id, LocalDate day, LocalTime start_time, LocalTime end_time) {
        this.nomProjecte = nomProjecte;
        this.username = username;
        this.id = id;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
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

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }
}
