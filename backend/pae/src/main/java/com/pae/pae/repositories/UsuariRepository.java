package com.pae.pae.repositories;

import com.pae.pae.models.UsuariDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.pae.pae.models.Rols;

import java.sql.*;
import java.util.ArrayList;

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
        String rol = resultSet.getString("rol");
        Rols r = Rols.valueOf(rol);
        uDTO = new UsuariDTO(resultSet.getString("username"),
                resultSet.getString("nom"),
                resultSet.getInt("edat"),
                resultSet.getInt("tlf"),
                resultSet.getString("email"),
                resultSet.getString("pwd"),
                resultSet.getBoolean("administrador"),
                r);
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

    public boolean addUser (UsuariDTO newUser) throws SQLException {
        String query = "INSERT INTO usuaris (username, nom, edat, tlf, email, pwd, administrador, rol, preferencia, actiu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getNom());
            stmt.setInt(3, newUser.getEdat());
            stmt.setInt(4, newUser.getTlf());
            stmt.setString(5, newUser.getEmail());
            stmt.setString(6, newUser.getPwd());
            stmt.setBoolean(7, newUser.getAdministrador());
            stmt.setString(8, newUser.getRol().toString());
            stmt.setString(9, newUser.getPreferencia());
            stmt.setBoolean(10, newUser.getActiu());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Usuari insertat correctament");
                return true;
            }
        }
        return false;
    }

    public boolean usuariRemove(String username) {
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




}
