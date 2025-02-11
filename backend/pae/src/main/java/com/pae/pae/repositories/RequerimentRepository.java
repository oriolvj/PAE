package com.pae.pae.repositories;

import com.pae.pae.models.Mes;
import com.pae.pae.models.ProjecteDTO;
import com.pae.pae.models.RequerimentDTO;
import com.pae.pae.models.Setmana;
import com.pae.pae.utils.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Repository
public class RequerimentRepository {

    private RequerimentDTO rDTO = null;

    private void AssignarProjecteObject(ResultSet resultSet) throws SQLException {
        try {
            int id = resultSet.getInt("id");
            Date date = resultSet.getDate("day");
            LocalDate localDate = date.toLocalDate();

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
                    id,
                    formattedDate,
                    formattedStartTime,
                    formattedEndTime,
                    resultSet.getString("technical_profile"),
                    resultSet.getString("act_name"),
                    resultSet.getString("act_room"),
                    resultSet.getString("projecte_nom")
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException("Error al convertir datos de Mes o Setmana desde el ResultSet", e);
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USER, Config.PWD);
    }

    public ArrayList<RequerimentDTO> getRequeriments() {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requirements";
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
        String query = "SELECT * FROM requirements WHERE id = ?";
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
        String query = "SELECT * FROM requirements WHERE projecte_nom = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
             statement.setString(1, nom);
             try (ResultSet resultSet = statement.executeQuery()) {
                 while (resultSet.next()) {
                     AssignarProjecteObject(resultSet);
                     ja.add(rDTO);
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public RequerimentDTO registerRequeriment(RequerimentDTO newRequerimentRequest) {
        String query = "INSERT INTO requirements (day, start_time, end_time, technical_profile, act_name, act_room, projecte_nom) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(newRequerimentRequest.getDay()));
            statement.setTime(2, Time.valueOf(newRequerimentRequest.getStartTime()));
            statement.setTime(3, Time.valueOf(newRequerimentRequest.getEndTime()));
            statement.setString(4, newRequerimentRequest.getTechnicalProfile());
            statement.setString(5, newRequerimentRequest.getActName());
            statement.setString(6, newRequerimentRequest.getActRoom());
            statement.setString(7, newRequerimentRequest.getNomProjecte());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newRequerimentRequest;
    }

    public boolean removeRequeriment(Integer id) {
        String query = "DELETE FROM requirements WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<RequerimentDTO> getRequerimentsProjecteSetmana(String nom, java.util.Date dataini, java.util.Date datafi) {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requirements WHERE projecte_nom = ? AND day >= ? AND day <= ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, nom);
            statement.setDate(2, new java.sql.Date(dataini.getTime()));
            statement.setDate(3, new java.sql.Date(datafi.getTime()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarProjecteObject(resultSet);
                    ja.add(rDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public ArrayList<RequerimentDTO> getRequerimentsProjecteSetmanaNoAssignats(String nom, java.util.Date dataini, java.util.Date datafi) {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requirements r WHERE r.id NOT IN (SELECT requeriment_id FROM feinaassignada) AND projecte_nom = ? AND day >= ? AND day <= ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, nom);
            statement.setDate(2, new java.sql.Date(dataini.getTime()));
            statement.setDate(3, new java.sql.Date(datafi.getTime()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarProjecteObject(resultSet);
                    ja.add(rDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public ArrayList<RequerimentDTO> getRequerimentsSetmana(java.util.Date dataini, java.util.Date datafi) {
        ArrayList<RequerimentDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM requirements WHERE day >= ? AND day <= ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setDate(1, new java.sql.Date(dataini.getTime()));
            statement.setDate(2, new java.sql.Date(datafi.getTime()));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarProjecteObject(resultSet);
                    ja.add(rDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }
}
