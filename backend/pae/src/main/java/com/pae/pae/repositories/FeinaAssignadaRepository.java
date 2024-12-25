package com.pae.pae.repositories;

import com.pae.pae.models.FeinaAssignadaDTO;
import com.pae.pae.models.ProjecteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Repository
public class FeinaAssignadaRepository {

    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";

    private FeinaAssignadaDTO fDTO = null;
    public ArrayList<FeinaAssignadaDTO> getfeinaAssignades() {
        //TODO
        return null;
    }

    public FeinaAssignadaDTO getfeinaAssignada(String nomProjecte, String username, Integer id) {
        //TODO
        return null;
    }

    public boolean getfeinaAssignadea(Map<String, String> newfeinaRequest) {
        //TODO
        return false;
    }
}
