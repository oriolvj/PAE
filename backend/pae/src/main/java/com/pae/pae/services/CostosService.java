package com.pae.pae.services;

import com.pae.pae.models.CostosDTO;
import com.pae.pae.repositories.CostosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CostosService {
    @Autowired
    private CostosRepository costosRepository = new CostosRepository();
    public CostosDTO getCostProjecte() throws SQLException {
        return costosRepository.getCostProjecte();
    }
}
