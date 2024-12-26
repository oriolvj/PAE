package com.pae.pae.services;

import com.pae.pae.models.LlocTreballDTO;
import com.pae.pae.repositories.LlocTreballRepository;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class LlocTreballService {

    @Autowired
    private LlocTreballRepository lloctreballRepository;

    public ArrayList<LlocTreballDTO> getLlocsTreball() {
        return lloctreballRepository.getLlocsTreball();
    }

    public boolean addlloctreball(Map<String, String> newlloctreballRequest) {
        return lloctreballRepository.addlloctreball(newlloctreballRequest);
    }

    public boolean lloctreballremove(String posicio) {
        return lloctreballRepository.lloctreballremove(posicio);
    }

    public boolean lloctreballModify(String posicio_ant, Map<String, String> modifyRequest) {
        return lloctreballRepository.lloctreballModify(posicio_ant, modifyRequest);
    }
}
