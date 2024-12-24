package com.pae.pae.repositories;

import com.pae.pae.models.Mes;
import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.RequerimentDTO;
import com.pae.pae.models.Setmana;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Repository
public class RequerimentRepository {

    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";

    private RequerimentDTO rDTO = null;

    private void AssignarProjecteObject(ResultSet resultSet) throws SQLException {
        try {
            Date date = resultSet.getDate("day");
            LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate formattedDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());


            Time startTime = resultSet.getTime("start_time");
            LocalTime localStartTime = startTime.toLocalTime();

            // Crear LocalTime en formato LocalTime.of(hour, minute)
            LocalTime formattedStartTime = LocalTime.of(localStartTime.getHour(), localStartTime.getMinute());


            Time endTime = resultSet.getTime("end_time");
            LocalTime localEndTime = endTime.toLocalTime();

            // Crear LocalTime en formato LocalTime.of(hour, minute)
            LocalTime formattedEndTime = LocalTime.of(localEndTime.getHour(), localEndTime.getMinute());

            rDTO = new RequerimentDTO(
                    formattedDate,
                    formattedStartTime,
                    formattedEndTime,
                    resultSet.getString("technicalProfile"),
                    resultSet.getString("actName"),
                    resultSet.getString("actRoom"),
                    resultSet.getString("nomProjecte")
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException("Error al convertir datos de Mes o Setmana desde el ResultSet", e);
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    public ArrayList<RequerimentDTO> getRequeriments() {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requeriments";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarProjecteObject(resultSet);
                ja.add(rDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public RequerimentDTO getRequeriment(Integer id) {
        String query = "SELECT * FROM requeriments WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarProjecteObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rDTO;
    }

    public ArrayList<RequerimentDTO> getRequerimentsProjecte(String nom) {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requeriments WHERE projecte_nom = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarProjecteObject(resultSet);
                ja.add(rDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }
}
