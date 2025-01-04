package com.pae.pae.controllers;

import com.pae.pae.models.CostosDTO;
import com.pae.pae.models.TecnicDTO;
import com.pae.pae.services.CostosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/costos")
public class CostosController {
    @Autowired
    private CostosService costosService = new CostosService();

    @CrossOrigin
    @GetMapping
    public CostosDTO getCostProjecte() throws SQLException {
        return costosService.getCostProjecte();
    }




}
