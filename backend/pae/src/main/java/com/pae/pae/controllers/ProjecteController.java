package com.pae.pae.controllers;


import com.pae.pae.models.Mes;
import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.Setmana;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.ProjecteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projectes")
public class ProjecteController {
    @Autowired
    private ProjecteService projecteService = new ProjecteService();

    @CrossOrigin
    @GetMapping
    public ArrayList<ProjecteDTO> getProjectes() {
        return projecteService.getProjectes();
    }

    @CrossOrigin
    @GetMapping(path = "/{nom}")
    public ProjecteDTO getProjecte(@PathVariable("nom") String nom) {
        return projecteService.getProjecte(nom);
    }

    @CrossOrigin
    @PostMapping
    public boolean addProject(@RequestBody Map<String, String> newprojectRequest) throws SQLException {
        return projecteService.addProject(newprojectRequest);
    }

    @CrossOrigin
    @GetMapping(path = "/nomsprojectes")
    public List<String> getNomProjectes() {
        return projecteService.getNomProjectes();
    }

    @CrossOrigin
    @GetMapping(path = "/desde/{date}")
    public List<ProjecteDTO> getProjectesDesdeData(@PathVariable("date") String date) throws ParseException {
        return projecteService.getProjectesDesdeData(date);
    }

     
}
