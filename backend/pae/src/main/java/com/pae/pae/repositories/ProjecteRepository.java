package com.pae.pae.repositories;

import com.pae.pae.models.Mes;
import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.Setmana;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProjecteRepository {
    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";

    private ProjecteDTO pDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    private void AssignarProjecteObject(ResultSet resultSet) throws SQLException {
        try {
            String mesString = resultSet.getString("mes");
            Mes mes = (mesString != null) ? Mes.valueOf(mesString) : null;
            pDTO = new ProjecteDTO(
                    resultSet.getString("nom"),
                    mes,
                    resultSet.getDate("dataInici"),
                    resultSet.getDate("dataFi"),
                    resultSet.getInt("numeroEmpleats"),
                    resultSet.getString("ubicacio")
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException("Error al convertir datos de Mes o Setmana desde el ResultSet", e);
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }

    public ArrayList<ProjecteDTO> getProjectes() {
        ArrayList<ProjecteDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM projectes";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarProjecteObject(resultSet);
                ja.add(pDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }


    public ProjecteDTO getProjecte(String nom) {
        String query = "SELECT * FROM projectes WHERE nom = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nom);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarProjecteObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pDTO;
    }

    public boolean addProject(Map<String, String> newprojectRequest) throws SQLException {
        String query = "INSERT INTO projectes(nom,mes,dataInici,dataFi,numeroEmpleats,ubicacio) VALUES(?,CAST(? AS mes),?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newprojectRequest.get("nom"));
            stmt.setString(2, newprojectRequest.get("mes").toString());
            stmt.setDate(3, Date.valueOf(newprojectRequest.get("dataInici")));
            stmt.setDate(4, Date.valueOf(newprojectRequest.get("dataFi")));
            stmt.setInt(5, Integer.valueOf(newprojectRequest.get("numeroEmpleats")));
            stmt.setString(6, newprojectRequest.get("ubicacio"));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Projecte insertat correctament");
                return true;
            }
        }
        return false;
    }

    public List<String> getNomProjectes() {
        List<String> noms = new ArrayList<>();
        String query = "SELECT DISTINCT nom FROM projectes";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    noms.add(resultSet.getString("nom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noms;
    }
}
