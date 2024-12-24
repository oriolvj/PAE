package com.pae.pae.controllers;

import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.RequerimentDTO;
import com.pae.pae.services.ProjecteService;
import com.pae.pae.services.RequerimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/requeriments")
public class RequerimentController {
        @Autowired
        private RequerimentService requerimentSercice = new RequerimentService();

        @CrossOrigin
        @GetMapping
        public ArrayList<RequerimentDTO> getRequeriments() {
            return requerimentSercice.getRequeriments();
        }

        @CrossOrigin
        @GetMapping(path = "/{id}")
        public RequerimentDTO getRequeriment(@PathVariable("id") Integer id) {
            return requerimentSercice.getRequeriment(id);
        }

        @CrossOrigin
        @GetMapping(path = "/nomProjecte/{nom}")
        public ArrayList<RequerimentDTO> getRequerimentsProjecte(@PathVariable("nom") String nom) {
            return requerimentSercice.getRequerimentsProjecte(nom);
        }
}
