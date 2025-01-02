package com.pae.pae.repositories;

import com.pae.pae.models.*;
import com.pae.pae.utils.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;

@Repository
public class FeinaAssignadaRepository {
    private FeinaAssignadaDTO fDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USER, Config.PWD);
    }

    private void AssignarFeinaObject(ResultSet resultSet) throws SQLException {
        try {
            fDTO = new FeinaAssignadaDTO(
                    resultSet.getString("projecte_nom"),
                    resultSet.getString("nom_empleat"),
                    resultSet.getInt("requeriment_id"),
                    null,
                    null,
                    null
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException("Error al convertir datos de Mes o Setmana desde el ResultSet", e);
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }

    public ArrayList<FeinaAssignadaDTO> getfeinaAssignades() {

        ArrayList<FeinaAssignadaDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM feinaassignada";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarFeinaObject(resultSet);
                ja.add(fDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public FeinaAssignadaDTO getfeinaAssignada(String nomProjecte, String username, Integer id) {
        String query = "SELECT * FROM feinaassignada WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarFeinaObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fDTO;
    }

    public boolean addfeinaAssignada(Map<String, String> newfeinaRequest) throws SQLException{
        String query = "INSERT INTO feinaassignada(projecte_nom, nom_empleat, requeriment_id) VALUES(?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newfeinaRequest.get("nom_projecte"));
            stmt.setString(2, newfeinaRequest.get("username"));
            stmt.setInt(3, Integer.parseInt(newfeinaRequest.get("id")));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Projecte insertat correctament");
                return true;
            }
        }
        return false;
    }

    public ArrayList<FeinaAssignadaDTO> getfeinesAssignadaUsuari(String username) {
        ArrayList<FeinaAssignadaDTO> ja = new ArrayList<>();
        String query = "SELECT f.nom_empleat, f.projecte_nom, r.day, r.start_time, r.end_time " +
                "FROM feinaassignada f " +
                "JOIN  requirements r " +
                "ON f.requeriment_id = r.id " +
                "WHERE f.nom_empleat = ?";

        System.out.println(query);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignarFeinaObject(resultSet);
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
                    fDTO.setDay(formattedDate);
                    fDTO.setStartTime(formattedStartTime);
                    fDTO.setEndTime(formattedEndTime);
                    ja.add(fDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }
}
