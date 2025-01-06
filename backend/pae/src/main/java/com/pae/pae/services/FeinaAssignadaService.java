package com.pae.pae.services;

import com.pae.pae.models.FeinaAssignadaDTO;
import com.pae.pae.repositories.FeinaAssignadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class FeinaAssignadaService {

    @Autowired
    FeinaAssignadaRepository feinaAssignadaRepository = new FeinaAssignadaRepository();
    public ArrayList<FeinaAssignadaDTO> getfeinaAssignades() {
        return feinaAssignadaRepository.getfeinaAssignades();
    }

    public FeinaAssignadaDTO getfeinaAssignada(String nomProjecte, String username, Integer id) {
        return feinaAssignadaRepository.getfeinaAssignada(nomProjecte,username, id);
    }

    public boolean addFeinaAssignada(Map<String, String> newfeinaRequest) throws SQLException {
        return feinaAssignadaRepository.addfeinaAssignada(newfeinaRequest);
    }

    public ArrayList<FeinaAssignadaDTO> getfeinesAssignadaUsuari(String username) {
        return feinaAssignadaRepository.getfeinesAssignadaUsuari(username);
    }

    public ArrayList<FeinaAssignadaDTO> getfeinaAssignadesHorari() {
        return feinaAssignadaRepository.getfeinaAssignadesHorari();
    }

    public Boolean deletefeinaAssignada(String nomProjecte, String username, Integer id) {
        return feinaAssignadaRepository.deletefeinaAssignada(nomProjecte,username, id);
    }

    public Boolean deleteSetmana(String dataIni, String dataFi) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dataini = dateFormat.parse(dataIni);
        Date datafi = dateFormat.parse(dataFi);
        return feinaAssignadaRepository.deleteSetmana(dataini, datafi);
    }
}
