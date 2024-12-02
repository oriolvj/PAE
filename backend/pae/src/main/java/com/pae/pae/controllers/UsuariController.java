package com.pae.pae.controllers;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.Rols;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.services.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/usuaris")
public class UsuariController {
    /*private static final String SECRET_KEY = "mySecretKey123"; // Clave secreta para firmar los tokens
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    @PostMapping(path = "/login")
    public String login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Autenticación (puedes usar tu servicio para esto)
        if (usuariService.login(username, password) != null) {
            // Generar JWT
            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", usuariService.getUserRole(username)) // Incluye el rol
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
            return token;
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }*/

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
    public UsuariDTO login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        //String password = new BCryptPasswordEncoder().encode(password_text); //fem el hash del password
        return usuariService.login(username, password);
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
