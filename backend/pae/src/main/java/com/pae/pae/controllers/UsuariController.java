package com.pae.pae.controllers;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.Rols;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import com.pae.pae.services.UsuariService;
import com.pae.pae.utils.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    @GetMapping(path = "/{username}")
    public UsuariDTO getUsuari(@PathVariable("username") String username) {
        return usuariService.getUsuari(username);
    }
    @CrossOrigin
    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        return usuariService.login(loginRequest);
    }

    @CrossOrigin
    @GetMapping(path = "/modalitat/{modalitat}")
    public ArrayList<UsuariDTO> getUsuarisByModalitat(@PathVariable("modalitat") String modalitat) {
        return usuariService.getUsuarisByModalitat(modalitat);
    }

    @CrossOrigin
    @GetMapping(path = "/preferencia/{preferencia}")
    public ArrayList<UsuariDTO> getUsuarisByPreferencia(@PathVariable("preferencia") String preferencia) {
        return usuariService.getUsuarisByPreferencia(preferencia);
    }

    @CrossOrigin
    @GetMapping(path = "/jornada/{jornada}")
    public ArrayList<UsuariDTO> getUsuarisByJornada(@PathVariable("jornada") String jornada) {
        return usuariService.getUsuarisByJornada(jornada);
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
