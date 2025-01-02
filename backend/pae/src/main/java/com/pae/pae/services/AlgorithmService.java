package com.pae.pae.services;


import com.pae.pae.repositories.AlgorithmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AlgorithmService {
    @Autowired
    private AlgorithmRepository algorithmRepository;

    public boolean execute() throws SQLException {
        return algorithmRepository.execute();
    }
}
