package com.pae.pae.services;


import com.pae.pae.models.RequerimentDTO;
import com.pae.pae.repositories.ProjecteRepository;
import com.pae.pae.repositories.RequerimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RequerimentService {

    @Autowired
    private RequerimentRepository requerimentRepository = new RequerimentRepository();

    public ArrayList<RequerimentDTO> getRequeriments() {
        return requerimentRepository.getRequeriments();
    }

    public RequerimentDTO getRequeriment(Integer id) {
        return requerimentRepository.getRequeriment(id);
    }

    public ArrayList<RequerimentDTO> getRequerimentsProjecte(String nom) {
        return requerimentRepository.getRequerimentsProjecte(nom);
    }

    public RequerimentDTO registerRequeriment(RequerimentDTO newRequerimentRequest) {
        return requerimentRepository.registerRequeriment(newRequerimentRequest);
    }

    public boolean removeRequeriment(Integer id) {
        return requerimentRepository.removeRequeriment(id);
    }
}
