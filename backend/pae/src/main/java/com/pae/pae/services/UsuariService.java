package com.pae.pae.services;

import com.pae.pae.models.UsuariDTO;
import com.pae.pae.repositories.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        System.out.println("No admin to create new users");
        return false;

    }

    public boolean usuariRemove(String username, String administrador) {
        UsuariDTO admin = usuariRepository.getUsuari(administrador);
        UsuariDTO user = usuariRepository.getUsuari(username);
        if (admin == null || !"ADMINISTRADOR".equals(admin.getRol())) {
            System.out.println("No admin to create new users");
            return false;
        }
        if (user == null) {
            System.out.println("No admin to create new users");
            return false;
        }
        try {
            return usuariRepository.removeUser(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean usuariModify(String administrador, UsuariDTO userToModify) {
        UsuariDTO user = usuariRepository.getUsuari(userToModify.getUsername());
        UsuariDTO admin = usuariRepository.getUsuari(administrador);
        if (admin == null || !"ADMINISTRADOR".equals(admin.getRol())) {
            System.out.println("No admin to modify new users");
            return false;
        }
        if (user == null) {
            System.out.println("No user");
            return false;
        }
        try {
            return usuariRepository.usuariModify(userToModify);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}