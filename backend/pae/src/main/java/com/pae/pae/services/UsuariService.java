package com.pae.pae.services;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.Rols;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import com.pae.pae.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UsuariService {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuariRepository usuariRepository;


    public ArrayList<UsuariDTO> getUsuaris() {
        return usuariRepository.getUsuaris();
    }

    public ArrayList<String> getUsernames() {
        return usuariRepository.getUsernames();
    }

    public UsuariDTO getUsuari(String username) {
        return usuariRepository.getUsuari(username);
    }

    public Map<String, Object> login(Map<String, String> loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.get("username"), loginRequest.get("password"));
            System.out.println("authInputToken: " + authInputToken);
            authManager.authenticate(authInputToken);
            System.out.println("authInputToken: " + authInputToken);
            String token = jwtUtil.generateToken(loginRequest.get("username"));

            return Collections.singletonMap("token", token);
        }catch (AuthenticationException authExc){
            authExc.printStackTrace();
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

    public boolean RegisterUser(Map<String, String> newUserRequest) throws SQLException {

        //s'ha de mirar per la session si l'usuari es admin

        return usuariRepository.RegisterUser(newUserRequest);
    }

    public boolean usuariRemove(String username) {
        //s'ha de mirar per la session si l'usuari es admin
        return usuariRepository.removeUser(username);
    }

    public boolean usuariModify(String username, Map<String, String> modifyRequest) {
        //s'ha de mirar per la session si l'usuari es admin
        return usuariRepository.usuariModify(username, modifyRequest);
    }

    public List<UsuariDTO> getUsuarisByRol(String rol) {
        return usuariRepository.getUsuarisByRol(rol);
    }
}