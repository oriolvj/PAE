package com.pae.pae.repositories;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.UsuariDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.pae.pae.models.Rols;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@Repository
public class UsuariRepository {

    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";

    private UsuariDTO uDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    private void AssignUsuariObject(ResultSet resultSet) throws SQLException {
        try {
            String rolString = resultSet.getString("rol");
            Rols rol = (rolString != null) ? Rols.valueOf(rolString) : null;

            String jornadaString = resultSet.getString("jornada");
            Jornada jornada = (jornadaString != null) ? Jornada.valueOf(jornadaString) : null;

            uDTO = new UsuariDTO(
                    resultSet.getString("username"),
                    resultSet.getString("nom"),
                    (resultSet.getObject("edat") != null) ? resultSet.getInt("edat") : null,
                    (resultSet.getObject("tlf") != null) ? resultSet.getInt("tlf") : null,
                    resultSet.getString("email"),
                    resultSet.getString("pwd"),
                    rol,
                    resultSet.getString("preferencia"),
                    resultSet.getBoolean("actiu"),
                    resultSet.getBoolean("contractat"),
                    jornada
            );
        } catch (IllegalArgumentException e) {
            throw new SQLException("Error al convertir datos de Rol o Jornada desde el ResultSet", e);
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }


    public ArrayList<UsuariDTO> getUsuaris() {
        ArrayList<UsuariDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM usuaris";

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
        String query = "SELECT * FROM usuaris WHERE username = ?";
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

    public boolean existsUsuari(String username) {
        String query = "SELECT * FROM usuaris WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean RegisterUser (Map<String, String> newUserRequest) throws SQLException {
        String query = "INSERT INTO usuaris (username, nom, edat, tlf, email, pwd, administrador, rol, preferencia, actiu, contractat, jornada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newUserRequest.get("username"));
            stmt.setString(2, newUserRequest.get("nom"));
            stmt.setInt(3, Integer.parseInt(newUserRequest.get("edat")));
            stmt.setString(4, newUserRequest.get("tlf"));
            stmt.setString(5, newUserRequest.get("email"));
            stmt.setString(6, newUserRequest.get("pwd"));
            stmt.setString(7, Rols.valueOf(newUserRequest.get("rol")).toString());
            stmt.setString(8, newUserRequest.get("preferencia"));
            stmt.setBoolean(9, Boolean.parseBoolean(newUserRequest.get("actiu")));
            stmt.setBoolean(10, Boolean.parseBoolean(newUserRequest.get("contractat")));
            stmt.setString(11, newUserRequest.get("jornada"));



            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Usuari insertat correctament");
                return true;
            }
        }
        return false;
    }

    public boolean removeUser(String username) {
        boolean ok = true;
        String query = "DELETE FROM usuaris WHERE username = ?";
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

    public boolean usuariModify(String username, Map<String, String> modifyRequest) {
        String query = "UPDATE usuaris SET nom = ?, edat = ?, tlf = ?, email = ?, pwd = ?, administrador = ?, rol = ?, preferencia = ?, actiu = ?, contractat = ?, jornada = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, modifyRequest.get("username"));
            stmt.setString(2, modifyRequest.get("nom"));
            stmt.setInt(3, Integer.parseInt(modifyRequest.get("edat")));
            stmt.setString(4, modifyRequest.get("tlf"));
            stmt.setString(5, modifyRequest.get("email"));
            stmt.setString(6, modifyRequest.get("pwd"));
            stmt.setString(7, Rols.valueOf(modifyRequest.get("rol")).toString());
            stmt.setString(8, modifyRequest.get("preferencia"));
            stmt.setBoolean(9, Boolean.parseBoolean(modifyRequest.get("actiu")));
            stmt.setBoolean(10, Boolean.parseBoolean(modifyRequest.get("contractat")));
            stmt.setString(11, modifyRequest.get("jornada"));

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Usuari actualitzat correctament");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}
