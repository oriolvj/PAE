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
    @GetMapping(path = "/{projecte}")
    public ProjecteDTO getProjecte(@PathVariable("nom") String nom) {
        return projecteService.getProjecte(nom);
    }

    @CrossOrigin
    @PostMapping(path = "/register")
    public boolean addProject(@RequestBody Map<String, String> newprojectRequest) throws SQLException {
        String nom = newprojectRequest.get("nom");
        String data_ini =   newprojectRequest.get("data_inici");
        String data = newprojectRequest.get("data_fi");
        Date data_inici = null;
        Date data_fi = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            data_inici = formatter.parse(data_ini);
            data_fi = formatter.parse(data);
        } catch (ParseException e) {
            System.out.println("Error al convertir data_inici: " + e.getMessage());
        }
        String num_empl = newprojectRequest.get("num_empl");
        String ubicacio = newprojectRequest.get("ubicacio");
        Mes mes = Mes.valueOf(newprojectRequest.get("mes"));
        Setmana setmana = Setmana.valueOf(newprojectRequest.get("setmana"));

        ProjecteDTO proj = new ProjecteDTO(
                nom,
                mes,
                setmana,
                data_inici,
                data_fi,
                Integer.valueOf(num_empl),
                ubicacio
        );
        return projecteService.addProject(proj);

    }
}
