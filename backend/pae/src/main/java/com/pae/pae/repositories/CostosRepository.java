package com.pae.pae.repositories;

import com.pae.pae.models.CostProjecteDTO;
import com.pae.pae.models.CostosDTO;
import com.pae.pae.models.Jornada;
import com.pae.pae.models.TecnicDTO;
import com.pae.pae.utils.Config;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CostosRepository {
    //private CostosDTO cDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USER, Config.PWD);
    }
    //falta una funcio


    public CostosDTO getCostProjecte() throws SQLException {
        String query = "SELECT DISTINCT projecte_nom FROM feinaassignada;";
        List<String> projectNames = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projectNames.add(rs.getString("projecte_nom"));
            }
        }

        List<CostProjecteDTO> projectes_cost = new ArrayList<>();

        for (String projectName : projectNames) {
            List<Map<String, Double>> tecnic_cost = new ArrayList<>();
            query = "SELECT nom_empleat FROM feinaassignada WHERE projecte_nom = ?;";
            try (Connection connection = getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, projectName);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String nom = rs.getString("nom_empleat");

                        String employeeQuery = "SELECT sou, jornada FROM tecnics WHERE username = ?;";
                        try (PreparedStatement empStmt = connection.prepareStatement(employeeQuery)) {
                            empStmt.setString(1, nom);
                            try (ResultSet empRs = empStmt.executeQuery()) {
                                if (empRs.next()) {
                                    double salary = empRs.getDouble("sou");
                                    String jornadaStr = empRs.getString("jornada");
                                    Jornada jornada = Jornada.valueOf(jornadaStr);
                                    int j = (jornada == Jornada.TOTAL) ? 40 : 20;
                                    double costPerHour = salary / (4*j);

                                    // Obtener id_requeriment de feina_assignada
                                    String requerimentQuery = "SELECT requeriment_id FROM feinaassignada WHERE nom_empleat = ?";
                                    try (PreparedStatement reqStmt = connection.prepareStatement(requerimentQuery)) {
                                        reqStmt.setString(1, nom);
                                        try (ResultSet reqRs = reqStmt.executeQuery()) {
                                            if (reqRs.next()) {
                                                int idRequeriment = reqRs.getInt("requeriment_id");

                                                // Obtener start_time y end_time de requeriments
                                                String timeQuery = "SELECT start_time, end_time FROM requirements WHERE id = ?;";
                                                try (PreparedStatement timeStmt = connection.prepareStatement(timeQuery)) {
                                                    timeStmt.setInt(1, idRequeriment);
                                                    try (ResultSet timeRs = timeStmt.executeQuery()) {
                                                        if (timeRs.next()) {
                                                            Timestamp startTime = timeRs.getTimestamp("start_time");
                                                            Timestamp endTime = timeRs.getTimestamp("end_time");

                                                            // Convertir a LocalDateTime
                                                            LocalDateTime startDateTime = startTime.toLocalDateTime();
                                                            LocalDateTime endDateTime = endTime.toLocalDateTime();

                                                            Duration duration = Duration.between(startDateTime, endDateTime);
                                                            long totalMinutes = duration.toMinutes();
                                                            double hoursWorked = (totalMinutes / 30) * 0.5;
                                                            hoursWorked = Math.floor(hoursWorked * 60) / 60; // Redondear a minutos

                                                            // Calcular coste total
                                                            double totalCost = hoursWorked * costPerHour;

                                                            Map<String, Double> tecnicCostMap = Map.of(nom, totalCost);
                                                            tecnic_cost.add(tecnicCostMap);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            double totalCost = tecnic_cost.stream()
                    .mapToDouble(map -> map.values().stream().mapToDouble(Double::doubleValue).sum())
                    .sum();

            projectes_cost.add(new CostProjecteDTO(projectName, totalCost, tecnic_cost));
        }

        // Assuming you have some logic to calculate cost_ma_obra, cost_material, and cost_total
        double cost_ma_obra = projectes_cost.stream()
                .mapToDouble(CostProjecteDTO::getTotalCost)
                .sum();
        double cost_material = 0.0;
        double cost_total = cost_ma_obra + cost_material;

        return new CostosDTO(cost_ma_obra, cost_material, cost_total, projectes_cost);
    }
}
