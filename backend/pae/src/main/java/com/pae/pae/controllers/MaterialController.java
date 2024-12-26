package com.pae.pae.controllers;

import com.pae.pae.models.LlocTreballDTO;
import com.pae.pae.models.MaterialDTO;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/materials")
public class MaterialController {
    @Autowired
    private MaterialService materialService = new MaterialService();


    @CrossOrigin
    @GetMapping
    public ArrayList<MaterialDTO> getMaterials() {
        return materialService.getMterials();
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public MaterialDTO getMaterial(@PathVariable("id") int id) {
        return materialService.getMaterial(id);
    }

    @CrossOrigin
    @PostMapping
    public boolean addMaterial (@RequestBody Map<String, String> newmaterialRequest) throws SQLException {
        return materialService.addMaterial(newmaterialRequest);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{id}")
    public boolean Materialremove(@PathVariable("id") int id) {
        return materialService.Materialremove(id);
    }

    @CrossOrigin
    @PutMapping(path = "/{id}")
    public boolean MaterialModify (@PathVariable("id") int id, @RequestBody Map<String, String> modifyRequest){
        return materialService.MaterialModify(id,modifyRequest);
    }
}
