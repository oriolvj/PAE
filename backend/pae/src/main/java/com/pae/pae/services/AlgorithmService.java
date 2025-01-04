package com.pae.pae.services;


import com.pae.pae.repositories.AlgorithmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;

@Service
public class AlgorithmService {
    @Autowired
    private AlgorithmRepository algorithmRepository;

    public boolean execute(Date date) throws SQLException {
        return algorithmRepository.execute(date);
    }
}
