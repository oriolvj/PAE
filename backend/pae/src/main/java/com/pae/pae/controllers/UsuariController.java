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
    public boolean registerUser (@RequestBody Map<String, String> newuserRequest) throws SQLException {
        String adminUer = newuserRequest.get("adminUser");

        String username = newuserRequest.get("username");
        String nom = newuserRequest.get("nom");
        String edat = newuserRequest.get("edat");
        String tlf = newuserRequest.get("tlf");
        String email = newuserRequest.get("email");
        String pwd = newuserRequest.get("pwd");
        boolean administrador;
        administrador = Boolean.parseBoolean(newuserRequest.get("admin"));
        Rols rol = Rols.valueOf(newuserRequest.get("rol"));
        String preferencia = newuserRequest.get("preferencia");
        boolean actiu = Boolean.parseBoolean(newuserRequest.get("actiu"));
        boolean contractat = Boolean.parseBoolean(newuserRequest.get("contractat"));
        Jornada jornada = Jornada.valueOf(newuserRequest.get("jornada"));

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
        newUser.setActiu(contractat);
        newUser.setJornda(jornada);
        return usuariService.RegisterUser(adminUer, newUser);
    }

    @DeleteMapping(path = "/remove")
    public boolean remove(@RequestBody Map<String, String> deleteRequest) {
        String adminUser = deleteRequest.get("adminUser");
        String username = deleteRequest.get("username");
        return usuariService.usuariRemove(username, adminUser);
    }

    @CrossOrigin
    @PostMapping(path = "/modify")
    public boolean usuariModify (@RequestBody Map<String, String> modifyRequest){
        String adminUser = modifyRequest.get("adminUser");

        String username = modifyRequest.get("username"); //el username no sera modificable de moment
        String nom = modifyRequest.get("nom");
        String edat = modifyRequest.get("edat");
        String tlf = modifyRequest.get("tlf");
        String email = modifyRequest.get("email");
        String pwd = modifyRequest.get("pwd");
        boolean administrador;
        administrador = Boolean.parseBoolean(modifyRequest.get("admin"));
        Rols rol = Rols.valueOf(modifyRequest.get("rol"));
        String preferencia = modifyRequest.get("preferencia");
        boolean actiu = Boolean.parseBoolean(modifyRequest.get("actiu"));
        boolean contractat = Boolean.parseBoolean(modifyRequest.get("contractat"));
        Jornada jornada = Jornada.valueOf(modifyRequest.get("jornada"));

        UsuariDTO modifyUser = new UsuariDTO();
        modifyUser.setUsername(username);
        modifyUser.setNom(nom);
        modifyUser.setEdat(Integer.valueOf(edat));
        modifyUser.setTlf(Integer.valueOf(tlf));
        modifyUser.setEmail(email);
        modifyUser.setPwd(pwd);
        modifyUser.setAdministrador(administrador);
        modifyUser.setRol(rol);
        modifyUser.setPreferencia(preferencia);
        modifyUser.setActiu(actiu);
        modifyUser.setActiu(contractat);
        modifyUser.setJornda(jornada);
        return usuariService.usuariModify(adminUser, modifyUser);
    }
}
