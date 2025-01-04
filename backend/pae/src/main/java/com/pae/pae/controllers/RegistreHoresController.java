package com.pae.pae.controllers;


import com.pae.pae.models.RegistreHoresProjecteDTO;
import com.pae.pae.services.RegistreHoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/registrehores")
public class RegistreHoresController {
    @Autowired
    private RegistreHoresService registreHoresService = new RegistreHoresService();

    @CrossOrigin
    @GetMapping
    private List<RegistreHoresProjecteDTO> getRegistreHores() throws SQLException {
        return registreHoresService.getRegistreHores();
    }
}
