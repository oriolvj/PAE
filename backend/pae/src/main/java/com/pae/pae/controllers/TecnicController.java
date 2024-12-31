package com.pae.pae.controllers;


import com.pae.pae.models.LlocTreballDTO;
import com.pae.pae.models.TecnicDTO;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.TecnicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tecnics")
public class TecnicController {
    @Autowired
    private TecnicService tecnicService = new TecnicService();

    @CrossOrigin
    @GetMapping
    public ArrayList<TecnicDTO> getTecnics() {
        return tecnicService.getTecnics();
    }
    @CrossOrigin
    @GetMapping(path = "/{id}")
    public TecnicDTO getTecnic(@PathVariable("id") Integer id) {
        return tecnicService.getTecnic(id);
    }

    @CrossOrigin
    @PostMapping
    public boolean registerTecnic (@RequestBody Map<String, String> newTecnicRequest) throws SQLException {
        return tecnicService.registerTecnic(newTecnicRequest);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{username}")
    public boolean Tecnicremove(@PathVariable("username") String username) {
        return tecnicService.Tecnicremove(username);
    }

    @CrossOrigin
    @PutMapping(path = "/{username}")
    public boolean TecnicModify (@PathVariable("username") String username, @RequestBody Map<String, String> modifyRequest){
        return tecnicService.TecnicModify(username,modifyRequest);
    }
    @CrossOrigin
    @GetMapping(path = "/modalitat/{modalitat}")
    public ArrayList<TecnicDTO> getTecnicsByModalitat(@PathVariable("modalitat") String modalitat) {
        return tecnicService.getTecnicsByModalitat(modalitat);
    }

    @CrossOrigin
    @GetMapping(path = "/preferencia/{preferencia}")
    public ArrayList<TecnicDTO> getTecnicsByPreferencia(@PathVariable("preferencia") String preferencia) {
        return tecnicService.getTecnicsByPreferencia(preferencia);
    }

    @CrossOrigin
    @GetMapping(path = "/jornada/{jornada}")
    public ArrayList<TecnicDTO> getTecnicsByJornada(@PathVariable("jornada") String jornada) {
        return tecnicService.getTecnicsByJornada(jornada);
    }

    @CrossOrigin
    @GetMapping(path = "/modalitat/{modalitat}/preferencia/{preferencia}")
    public ArrayList<TecnicDTO> getTecnicsByModalitatAndPreferencia(@PathVariable("preferencia") String modalitat, @PathVariable("preferencia") String preferencia) {
        return tecnicService.getTecnicsByModalitatAndPreferencia(modalitat, preferencia);
    }

    @CrossOrigin
    @GetMapping(path = "/usuaris/{username}")
    public String getNomTecnic(@PathVariable("username") String username) {
        return tecnicService.getNomTecnic(username);
    }

    @CrossOrigin
    @PutMapping(path = "/llocTreball/{llocTreball}")
    public List<TecnicDTO> getTecnicsByLlocDeTreball(@PathVariable("llocTreball")String llocTreball) {
        return tecnicService.getTecnicsByLlocDeTreball(llocTreball);
    }
    //funcio per obtenir el nom del tecnic anat a usuari

}
