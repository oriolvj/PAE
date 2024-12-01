package com.pae.pae.services;

import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.ProjecteRepository;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjecteService {
    @Autowired
    private ProjecteRepository projecteRepository;
    public ArrayList<ProjecteDTO> getProjectes() {
        return projecteRepository.getProjectes();
    }

    public ProjecteDTO getProjecte(String nom) {
        return projecteRepository.getProjecte(nom);
    }

    //public boolean addProject()
}
