package com.pae.pae.services;

import com.pae.pae.models.MaterialDTO;
import com.pae.pae.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public ArrayList<MaterialDTO> getMterials() {
        return materialRepository.getMaterials();
    }


    public MaterialDTO getMaterial(int id) {
        return materialRepository.getMaterial(id);
    }

    public boolean addMaterial(Map<String, String> newmaterialRequest) {
        return materialRepository.addMaterial(newmaterialRequest);
    }

    public boolean Materialremove(int id) {
        return materialRepository.Materialremove(id);
    }

    public boolean MaterialModify(int id, Map<String, String> modifyRequest) {
        return materialRepository.MaterialModify(id, modifyRequest);
    }
}
