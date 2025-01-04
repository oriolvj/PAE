package com.pae.pae.services;


import com.pae.pae.repositories.AlgorithmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AlgorithmService {
    @Autowired
    private AlgorithmRepository algorithmRepository;

    public boolean execute(String date) throws SQLException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = dateFormat.parse(date);
        return algorithmRepository.execute(data);
    }
}
