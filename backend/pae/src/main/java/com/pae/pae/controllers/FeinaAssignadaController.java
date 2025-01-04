package com.pae.pae.controllers;

import com.pae.pae.models.FeinaAssignadaDTO;
import com.pae.pae.services.FeinaAssignadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/feinaassignada")
public class FeinaAssignadaController {

    @Autowired
    private FeinaAssignadaService feinaAssignadaService = new FeinaAssignadaService();

    @CrossOrigin
    @GetMapping
    public ArrayList<FeinaAssignadaDTO> getfeinaAssignades() {
        return feinaAssignadaService.getfeinaAssignades();
    }

    @CrossOrigin
    @GetMapping(path = "/horari")
    public ArrayList<FeinaAssignadaDTO> getfeinaAssignadesHorari() {
        return feinaAssignadaService.getfeinaAssignadesHorari();
    }

    @CrossOrigin
    @GetMapping(path = "/{nomProjecte}/{username}/{id}")
    public FeinaAssignadaDTO getfeinaAssignada(@PathVariable("nomProjecte") String nomProjecte, @PathVariable("username") String username, @PathVariable("id") Integer id) {
        return feinaAssignadaService.getfeinaAssignada(nomProjecte, username, id);
    }

    @CrossOrigin
    @PostMapping
    public boolean addFeinaAssignada(@RequestBody Map<String, String> newfeinaRequest) throws SQLException {
        return feinaAssignadaService.addFeinaAssignada(newfeinaRequest);


    }

    @CrossOrigin
    @GetMapping(path = "/{username}")
    public ArrayList<FeinaAssignadaDTO> getfeinesAssignadaUsuari(@PathVariable("username") String username) {
        return feinaAssignadaService.getfeinesAssignadaUsuari(username);
    }

}
