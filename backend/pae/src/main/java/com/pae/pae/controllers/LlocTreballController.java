package com.pae.pae.controllers;


import com.pae.pae.models.LlocTreballDTO;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.LlocTreballService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/lloctreball")
public class LlocTreballController {

    @Autowired
    private LlocTreballService llocTreballService = new LlocTreballService();

    @CrossOrigin
    @GetMapping
    public ArrayList<LlocTreballDTO> getLlocsTreball() {
        return llocTreballService.getLlocsTreball();
    }

    @CrossOrigin
    @PostMapping
    public boolean addlloctreball (@RequestBody Map<String, String> newlloctreballRequest) throws SQLException {
        return llocTreballService.addlloctreball(newlloctreballRequest);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{posicio}")
    public boolean lloctreballremove(@PathVariable("posicio") String posicio) {
        return llocTreballService.lloctreballremove(posicio);
    }

    @CrossOrigin
    @PutMapping(path = "/{posicio}")
    public boolean lloctreballModify (@PathVariable("posicio") String username, @RequestBody Map<String, String> modifyRequest){
        return llocTreballService.lloctreballModify(username,modifyRequest);
    }

}
