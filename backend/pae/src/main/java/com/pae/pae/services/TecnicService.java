package com.pae.pae.services;

import com.pae.pae.models.TecnicDTO;

import com.pae.pae.repositories.TecnicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class TecnicService {
    @Autowired
    private TecnicRepository tecnicRepository;

    public ArrayList<TecnicDTO> getTecnics() {return tecnicRepository.getTecnics();}

    public TecnicDTO getTecnic(int id) {return tecnicRepository.getTecnic(id);}


    public boolean registerTecnic(Map<String, String> newTecnicRequest) {
        return tecnicRepository.registerTecnic(newTecnicRequest);
    }
}
