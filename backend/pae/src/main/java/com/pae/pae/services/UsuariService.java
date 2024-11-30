package com.pae.pae.services;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

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

    public boolean RegisterUser(String administrador, UsuariDTO newuser) {
        UsuariDTO admin = usuariRepository.getUsuari(administrador);
        if (admin == null || !"ADMINISTRADOR".equals(admin.getRol())) {
            System.out.println("No admin to create new users");
            return false;
        }
        UsuariDTO user = usuariRepository.getUsuari(newuser.getUsername());
        if (user != null) {
            System.out.println("User already exsists");
            return false;
        }
        //newUsuari.setPassword(new BCryptPasswordEncoder().encode(newUsuari.getPassword())); --> encru¡iptar password
        try {
            usuariRepository.addUser(newuser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    public boolean usuariRemove(String username) {
        return usuariRepository.usuariRemove(username);
    }
}