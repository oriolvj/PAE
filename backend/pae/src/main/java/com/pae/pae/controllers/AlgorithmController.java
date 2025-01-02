package com.pae.pae.controllers;

import com.pae.pae.services.AlgorithmService;
import com.pae.pae.services.TecnicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    @Autowired
    private AlgorithmService algorithmService = new AlgorithmService();

    @CrossOrigin
    @GetMapping
    public boolean execute() throws SQLException {
        return algorithmService.execute();
    }
}
