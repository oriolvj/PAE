package com.pae.pae.controllers;


import com.pae.pae.models.TecnicDTO;
import com.pae.pae.services.TecnicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
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
    @GetMapping(path = "/{tecnic}")
    public TecnicDTO getTecnic(@PathVariable("tecnic") int id) {
        return tecnicService.getTecnic(id);
    }

    @CrossOrigin
    @PostMapping
    public boolean registerTecnic (@RequestBody Map<String, String> newTecnicRequest) throws SQLException {
        return tecnicService.registerTecnic(newTecnicRequest);
    }


    //falten fer tots els afegir, modificar i eliminar tecnics a més de el tema de les posicions fer-les dinàmiques i obtenir les

}
