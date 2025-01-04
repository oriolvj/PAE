package com.pae.pae.services;

import com.pae.pae.models.RegistreHoresProjecteDTO;
import com.pae.pae.repositories.RegistreHoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RegistreHoresService {
    @Autowired
    private RegistreHoresRepository registreHoresRepository = new RegistreHoresRepository();

    public List<RegistreHoresProjecteDTO> getRegistreHores() throws SQLException {
        return registreHoresRepository.getRegistreHores();
    }
}
