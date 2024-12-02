package com.pae.pae.services;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.Rols;
import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class UsuariService {

    @Autowired
    private UsuariRepository usuariRepository;


    public ArrayList<UsuariDTO> getUsuaris() {
        return usuariRepository.getUsuaris();
    }

    public UsuariDTO getUsuari(String username) {
        return usuariRepository.getUsuari(username);
    }

    public UsuariDTO login(String username, String password) {

        UsuariDTO user = getUsuari(username);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return null;
        } else {

            if (user.getPwd().equals(password)) {
                System.out.println("Correct login");
                return user;
            }
            System.out.println("Invalid username or password.");
            return null;
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
}