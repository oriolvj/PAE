package com.pae.pae.controllers;

import com.pae.pae.models.Rols;
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

    @CrossOrigin
    @PostMapping(path = "/login")
    public UsuariDTO login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        //String password = new BCryptPasswordEncoder().encode(password_text); //fem el hash del password
        return usuariService.login(username, password);
    }

    @CrossOrigin
    @PostMapping(path = "/register")
    public boolean registerUser (@RequestBody Map<String, String> newuserRequest){
        String adminUer = newuserRequest.get("adminUser");

        String username = newuserRequest.get("username");
        String nom = newuserRequest.get("nom");
        String edat = newuserRequest.get("edat");
        String tlf = newuserRequest.get("tlf");
        String email = newuserRequest.get("email");
        String pwd = newuserRequest.get("pwd");
        boolean administrador;
        administrador = Boolean.parseBoolean(newuserRequest.get("admin").toString());
        Rols rol = Rols.valueOf(newuserRequest.get("rol"));
        String preferencia = newuserRequest.get("preferencia");
        boolean actiu = Boolean.parseBoolean(newuserRequest.get("actiu").toString());

        UsuariDTO newUser = new UsuariDTO();
        newUser.setUsername(username);
        newUser.setNom(nom);
        newUser.setEdat(Integer.valueOf(edat));
        newUser.setTlf(Integer.valueOf(tlf));
        newUser.setEmail(email);
        newUser.setPwd(pwd);
        newUser.setAdministrador(administrador);
        newUser.setRol(rol);
        newUser.setPreferencia(preferencia);
        newUser.setActiu(actiu);
        return usuariService.RegisterUser(adminUer, newUser);
    }

    @DeleteMapping(path = "/remove")
    public boolean remove(@RequestBody Map<String, String> deleteRequest) {
        String adminUer = newuserRequest.get("adminUser");
        String username = loginRequest.get("username");
        return usuariService.usuariRemove(username, adminUer);
    }
}
