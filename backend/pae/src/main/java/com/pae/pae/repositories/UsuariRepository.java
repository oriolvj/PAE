package com.pae.pae.repositories;

import com.pae.pae.models.UsuariDTO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class UsuariRepository {

    private static final String URL = "";
    private static final String USER = "";
    private static final String PWD = "";

    private UsuariDTO uDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    private void AssignUsuariObject(ResultSet resultSet) throws SQLException {
        uDTO = new UsuariDTO(resultSet.getString("username"),
                resultSet.getString("nom"),
                resultSet.getInt("edat"),
                resultSet.getInt("tlf"),
                resultSet.getString("email"),
                resultSet.getString("pwd"),
                resultSet.getBoolean("administrador"),
                resultSet.getString("rol"));
    }

    public ArrayList<UsuariDTO> getUsuaris() {
        ArrayList<UsuariDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM usuari";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignUsuariObject(resultSet);
                ja.add(uDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public UsuariDTO getUsuari(String username) {
        String query = "SELECT * FROM usuari WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignUsuariObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uDTO;
    }

    public boolean usuariRemove(String username) {
        boolean ok = true;
        String query = "DELETE FROM usuari WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }

}
