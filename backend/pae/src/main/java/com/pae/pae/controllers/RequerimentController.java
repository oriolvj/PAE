package com.pae.pae.controllers;

import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.RequerimentDTO;
import com.pae.pae.services.ProjecteService;
import com.pae.pae.services.RequerimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
        @PostMapping
        public RequerimentDTO registerRequeriment (@RequestBody RequerimentDTO newRequerimentRequest) {
            return requerimentSercice.registerRequeriment(newRequerimentRequest);
        }

        @CrossOrigin
        @GetMapping(path = "/nomProjecte/{nom}")
        public ArrayList<RequerimentDTO> getRequerimentsProjecte(@PathVariable("nom") String nom) {
            return requerimentSercice.getRequerimentsProjecte(nom);
        }

        @CrossOrigin
        @DeleteMapping(path = "/{id}")
        public boolean removeRequeriment(@PathVariable("id") Integer id) {
            return requerimentSercice.removeRequeriment(id);
        }

        @CrossOrigin
        @GetMapping(path = "/nomProjecte/{nom}/setmana/{data_inici}/{data_fi}")
        public ArrayList<RequerimentDTO> getRequerimentsProjecteSetmana(@PathVariable("nom") String nom, @PathVariable("data_inici") String data_inici,
                                                                        @PathVariable("data_fi") String data_fi) throws ParseException {
                return requerimentSercice.getRequerimentsProjecteSetmana(nom, data_inici, data_fi);
        }

        @CrossOrigin
        @GetMapping(path = "/setmana/{data_inici}/{data_fi}")
        public ArrayList<RequerimentDTO> getRequerimentsSetmana( @PathVariable("data_inici") String data_inici,
                                                                        @PathVariable("data_fi") String data_fi) throws ParseException {
                return requerimentSercice.getRequerimentsSetmana(data_inici, data_fi);
        }
}
