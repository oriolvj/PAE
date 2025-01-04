package com.pae.pae.controllers;

import com.pae.pae.services.AlgorithmService;
import com.pae.pae.services.TecnicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    @Autowired
    private AlgorithmService algorithmService = new AlgorithmService();

    @CrossOrigin
    @GetMapping(path = "/{date}")
    public boolean execute(@PathVariable("date") String date) throws SQLException, ParseException {
        return algorithmService.execute(date);
    }
}
