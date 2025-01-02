package com.pae.pae.services;

import com.pae.pae.models.TecnicDTO;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.TecnicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TecnicService {
    @Autowired
    private TecnicRepository tecnicRepository = new TecnicRepository();

    public ArrayList<TecnicDTO> getTecnics() {return tecnicRepository.getTecnics();}

    public TecnicDTO getTecnic(int id) {return tecnicRepository.getTecnic(id);}


    public boolean registerTecnic(Map<String, String> newTecnicRequest) {
        return tecnicRepository.registerTecnic(newTecnicRequest);
    }

    public boolean Tecnicremove(String username) {
        return tecnicRepository.Tecnicremove(username);
    }

    public boolean TecnicModify(String username, Map<String, String> modifyRequest) {
        return tecnicRepository.TecnicModify(username, modifyRequest);
    }

    public ArrayList<TecnicDTO> getTecnicsByModalitat(String modalitat) {
        if(modalitat.equals("POOL")){
            return tecnicRepository.getTecnicsByModalitat(true);
        } else return tecnicRepository.getTecnicsByModalitat(false);

    }

    public ArrayList<TecnicDTO> getTecnicsByPreferencia(String preferencia) {
        return tecnicRepository.getTecnicsByPreferencia(preferencia);
    }

    public ArrayList<TecnicDTO> getTecnicsByJornada(String jornada) {
        return tecnicRepository.getTecnicsByJornada(jornada);
    }

    public ArrayList<TecnicDTO> getTecnicsByModalitatAndPreferencia(String modalitat, String preferencia) {
        if(modalitat.equals("POOL")){
            return tecnicRepository.getTecnicsByModalitatAndPreferencia(true, preferencia);
        } else return tecnicRepository.getTecnicsByModalitatAndPreferencia(false, preferencia);
    }

    public String getNomTecnic(String username) {
        return tecnicRepository.getNomTecnic(username);
    }

    public List<TecnicDTO> getTecnicsByLlocDeTreball(String llocTreball) {
        return tecnicRepository.getTecnicsByLlocDeTreball(llocTreball);
    }
}
