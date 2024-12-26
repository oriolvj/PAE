package com.pae.pae.repositories;

import com.pae.pae.models.LlocTreballDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@Repository
public class LlocTreballRepository {
    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";
    private LlocTreballDTO lltDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    private void AssignarLlocTreballObject(ResultSet resultSet) throws SQLException {
        try {
            lltDTO = new LlocTreballDTO(
                    resultSet.getString("posicio")
            );
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }


    public ArrayList<LlocTreballDTO> getLlocsTreball() {
        ArrayList<LlocTreballDTO> llocsTreball = new ArrayList<>();
        String query = "SELECT * FROM posicions";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarLlocTreballObject(resultSet);
                llocsTreball.add(lltDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return llocsTreball;
    }

    public boolean addlloctreball(Map<String, String> newlloctreballRequest) {
    String checkQuery = "SELECT COUNT(*) FROM posicions WHERE posicio = ?";
    String insertQuery = "INSERT INTO posicions (posicio) VALUES (?)";
    try (Connection connection = getConnection()) {
        // Check if the position already exists
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, newlloctreballRequest.get("posicio"));
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Position already exists
                    System.out.println("Position already exists");
                    return false;
                }
            }
        }

        // Insert the new position
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, newlloctreballRequest.get("posicio"));
            insertStatement.executeUpdate();
            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean lloctreballremove(String posicio) {
        String query = "DELETE FROM posicions WHERE posicio = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, posicio);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lloctreballModify(String posicio_ant, Map<String, String> modifyRequest) {
        String checkQuery = "SELECT COUNT(*) FROM posicions WHERE posicio = ?";
        String updateQuery = "UPDATE posicions SET posicio = ? WHERE posicio = ?";
        try (Connection connection = getConnection()) {
            // Check if posicio_ant exists
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, posicio_ant);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (!resultSet.next() || resultSet.getInt(1) == 0) {
                        // posicio_ant does not exist
                        return false;
                    }
                }
            }

            // Update the position
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, modifyRequest.get("posicio"));
                updateStatement.setString(2, posicio_ant);
                return updateStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
