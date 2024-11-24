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

    /*public boolean doAuth(UsuariDTO user) {
        String query= "select * from usuaris where username=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                String password = rs.getString("pwd");
                //BCrypt.Result result = BCrypt.verifyer().verify(user.getPassword().toCharArray(), password);
                return result.verified;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            ok = false;
        }
    }*/

}
