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
    public boolean usuariModify (@PathVariable("username") String username, @RequestBody Map<String, String> modifyRequest){
        return tecnicService.TecnicModify(username,modifyRequest);
    }

}
