package com.pae.pae.controllers;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/usuaris")
public class UsuariController {

    @Autowired
    private UsuariService usuariService = new UsuariService();

    @GetMapping
    public ArrayList<UsuariDTO> getUsuaris() {
        return usuariService.getUsuaris();
    }

    @GetMapping(path = "/{username}")
    public UsuariDTO getUsuari(@PathVariable("username") String username) {
        return usuariService.getUsuari(username);
    }

    @PostMapping(path = "/login")
    public UsuariDTO login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        return usuariService.login(username, password);
    }

    @DeleteMapping(path = "/{username}")
    public boolean remove(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        return usuariService.usuariRemove(username);
    }
}
