package com.pae.pae.controllers;


import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.ProjecteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/projectes")
public class ProjecteController {
    @Autowired
    private ProjecteService projecteService = new ProjecteService();

    @GetMapping
    public ArrayList<ProjecteDTO> getProjectes() {
        return projecteService.getProjectes();
    }

    @GetMapping(path = "/{projecte}")
    public ProjecteDTO getProjecte(@PathVariable("nom") String nom) {
        return projecteService.getProjecte(nom);
    }
}
