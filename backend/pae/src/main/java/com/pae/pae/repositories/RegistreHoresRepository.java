package com.pae.pae.repositories;


import com.pae.pae.models.RegistreHoresProjecteDTO;
import com.pae.pae.utils.Config;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RegistreHoresRepository {
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USER, Config.PWD);
    }

    public List<RegistreHoresProjecteDTO> getRegistreHores() throws SQLException {
        String query = "SELECT DISTINCT projecte_nom FROM feinaassignada;";
        List<String> projectNames = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projectNames.add(rs.getString("projecte_nom"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<RegistreHoresProjecteDTO> projectes_hores = new ArrayList<>();
        for (String projectName : projectNames) {
            List<Map<String, Double>> tecnic_hores = new ArrayList<>();
            query = "SELECT f.nom_empleat, f.projecte_nom, r.day, r.start_time, r.end_time " +
                    "FROM feinaassignada f " +
                    "JOIN requirements r " +
                    "ON f.requeriment_id = r.id " +
                    "WHERE f.projecte_nom = ?;";
            try (Connection connection = getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, projectName);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String nom = rs.getString("nom_empleat");

                        Timestamp startTime = rs.getTimestamp("start_time");
                        Timestamp endTime = rs.getTimestamp("end_time");

                        LocalDateTime startDateTime = startTime.toLocalDateTime();
                        LocalDateTime endDateTime = endTime.toLocalDateTime();

                        Duration duration = Duration.between(startDateTime, endDateTime);
                        long totalMinutes = duration.toMinutes();
                        double hoursWorked = (totalMinutes / 30) * 0.5;
                        hoursWorked = Math.floor(hoursWorked * 60) / 60; // Redondear a minutos
                        Map<String, Double> tecnicCostMap = Map.of(nom, hoursWorked);
                        tecnic_hores.add(tecnicCostMap);

                    }
                }
            }
            double totalHours = tecnic_hores.stream()
                    .mapToDouble(map -> map.values().stream().mapToDouble(Double::doubleValue).sum())
                    .sum();

            projectes_hores.add(new RegistreHoresProjecteDTO(projectName, totalHours, tecnic_hores));
        }
        return projectes_hores;
    }

}
