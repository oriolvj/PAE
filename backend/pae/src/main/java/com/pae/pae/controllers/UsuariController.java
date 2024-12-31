package com.pae.pae.controllers;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.UsuariService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuaris")
public class UsuariController {

    @Autowired
    private UsuariService usuariService = new UsuariService();

    @CrossOrigin
    @GetMapping
    public ArrayList<UsuariDTO> getUsuaris() {
        return usuariService.getUsuaris();
    }

    @CrossOrigin
    @GetMapping(path = "/usernames")
    public ArrayList<String> getUsernames() {
        return usuariService.getUsernames();
    }
    @CrossOrigin
    @GetMapping(path = "/{username}")
    //TODO: Remove password from username GET request
    public UsuariDTO getUsuari(@PathVariable("username") String username) {
        return usuariService.getUsuari(username);
    }
    @CrossOrigin
    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        return usuariService.login(loginRequest);
    }

    @CrossOrigin
    @PutMapping(path = "/rol/{rol}")
    public List<UsuariDTO> getUsuarisByRol(String rol) {
        return usuariService.getUsuarisByRol(rol);
    }

    @CrossOrigin
    @PostMapping
    public boolean registerUser (@RequestBody Map<String, String> newUserRequest) throws SQLException {
        return usuariService.RegisterUser(newUserRequest);
    }

    @CrossOrigin
    @DeleteMapping(path = "/{username}")
    public boolean remove(@PathVariable("username") String username) {
        return usuariService.usuariRemove(username);
    }

    @CrossOrigin
    @PutMapping(path = "/{username}")
    public boolean usuariModify (@PathVariable("username") String username, @RequestBody Map<String, String> modifyRequest){
        return usuariService.usuariModify(username,modifyRequest);
    }
}
