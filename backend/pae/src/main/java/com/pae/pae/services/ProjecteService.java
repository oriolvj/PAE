package com.pae.pae.services;

import com.pae.pae.models.*;
import com.pae.pae.repositories.ProjecteRepository;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProjecteService {
    @Autowired
    private ProjecteRepository projecteRepository = new ProjecteRepository();
    public ArrayList<ProjecteDTO> getProjectes() {
        return projecteRepository.getProjectes();
    }

    public ProjecteDTO getProjecte(String nom) {
        return projecteRepository.getProjecte(nom);
    }


    public boolean addProject(Map<String, String> newprojectRequest) throws SQLException {
        //comprovar rol del administrador del jwt aqui
        return projecteRepository.addProject(newprojectRequest);
    }

    public List<String> getNomProjectes() {
        return projecteRepository.getNomProjectes();
    }

    public List<ProjecteDTO> getProjectesDesdeData(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = dateFormat.parse(date);
        return projecteRepository.getProjectesDesdeData(data);
    }
}
