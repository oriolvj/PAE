package com.pae.pae.repositories;

import com.pae.pae.models.Jornada;
import com.pae.pae.models.TecnicDTO;
import com.pae.pae.models.UsuariDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@Repository
public class TecnicRepository {
    @Value("${SPRING_DATASOURCE_URL}")
    private String URL = "";
    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String USER = "";
    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String PWD = "";
    private TecnicDTO tDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    private void AssignTecnicsObject(ResultSet resultSet) throws SQLException {
        try {
            String jornadaString = resultSet.getString("jornada");
            Jornada jornada = (jornadaString != null) ? Jornada.valueOf(jornadaString) : null;
            tDTO = new TecnicDTO(
                    resultSet.getInt("id"),                  // ID del tècnic// Nom del tècnic
                    resultSet.getInt("sou"),                // Sou
                    resultSet.getString("posicio"),
                    resultSet.getString("preferencia"),
                    resultSet.getBoolean("actiu"),
                    resultSet.getBoolean("contractat"),
                    jornada// Posició
            );
        } catch (SQLException e) {
            throw new SQLException("Error al obtenir dades del ResultSet per a TecnicDTO", e);
        }
    }

    public ArrayList<TecnicDTO> getTecnics() {
        ArrayList<TecnicDTO> tecnics = new ArrayList<>();
        String query = "SELECT * FROM tecnics";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                AssignTecnicsObject(resultSet);
                tecnics.add(tDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tecnics;
    }


    public TecnicDTO getTecnic(int id) {
        String query = "SELECT * FROM tecnics WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignTecnicsObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tDTO;
    }

    public boolean registerTecnic(Map<String, String> newTecnicRequest) {
        String checkPositionQuery = "SELECT COUNT(*) FROM lloc_treball WHERE posicio = ?";
        String insertTecnicQuery = "INSERT INTO tecnics (username, sou, posicio, nom_tecnic, preferencia, actiu, contractat, jornada) VALUES (?, ?, ?, ?, ?, ?, ?, CAST(? AS jornada))";

        try (Connection connection = getConnection()) {

            // Check if the position exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkPositionQuery)) {
                checkStmt.setString(1, newTecnicRequest.get("posicio"));
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0) {
                        // Position does not exist, return error
                        //throw new SQLException("Position does not exist");
                        System.out.println("Position already exists");
                        return false;
                    }
                }
            }

            // Insert the new tecnic
            try (PreparedStatement insertTecnicStmt = connection.prepareStatement(insertTecnicQuery)) {
                insertTecnicStmt.setString(1, newTecnicRequest.get("username"));
                insertTecnicStmt.setInt(2, Integer.parseInt(newTecnicRequest.get("sou")));
                insertTecnicStmt.setString(3, newTecnicRequest.get("posicio"));
                insertTecnicStmt.setString(4, newTecnicRequest.get("preferencia"));
                insertTecnicStmt.setBoolean(5, Boolean.parseBoolean(newTecnicRequest.get("actiu")));
                insertTecnicStmt.setBoolean(6, Boolean.parseBoolean(newTecnicRequest.get("contractat")));
                insertTecnicStmt.setString(7, newTecnicRequest.get("jornada"));

                insertTecnicStmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Tecnicremove(String username) {
        String query = "DELETE FROM tecnics WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean TecnicModify(String username, Map<String, String> modifyRequest) {
        String checkPositionQuery = "SELECT COUNT(*) FROM lloc_treball WHERE posicio = ?";
        String query = "UPDATE tecnics SET sou = ?, posicio = ?, preferencia = ?, actiu = ?, contractat = ?, jornada = CAST(? AS jornada) WHERE username = ?";
        try (Connection connection = getConnection()) {
            // Check if the position exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkPositionQuery)) {
                checkStmt.setString(1, modifyRequest.get("posicio"));
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0) {
                        System.out.println("Position does not exist");
                        return false;
                    }
                }
            }

            // Update the tecnic
            try (PreparedStatement updateTecnicStmt = connection.prepareStatement(query)) {
                updateTecnicStmt.setInt(1, Integer.parseInt(modifyRequest.get("sou")));
                updateTecnicStmt.setString(2, modifyRequest.get("posicio"));
                updateTecnicStmt.setString(4, modifyRequest.get("preferencia"));
                updateTecnicStmt.setBoolean(5, Boolean.parseBoolean(modifyRequest.get("actiu")));
                updateTecnicStmt.setBoolean(6, Boolean.parseBoolean(modifyRequest.get("contractat")));
                updateTecnicStmt.setString(7, modifyRequest.get("jornada"));
                updateTecnicStmt.setString(8, username);
                updateTecnicStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<TecnicDTO> getTecnicsByModalitat(boolean modalitat) {
        ArrayList<TecnicDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM tecncis WHERE contractat = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, modalitat);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignTecnicsObject(resultSet);
                    ja.add(tDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public ArrayList<TecnicDTO> getTecnicsByPreferencia(String preferencia) {
        ArrayList<TecnicDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM tecnics WHERE preferencia = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, preferencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignTecnicsObject(resultSet);
                    ja.add(tDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public ArrayList<TecnicDTO> getTecnicsByJornada(String jornada) {
        ArrayList<TecnicDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM tecnics WHERE contractat = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, jornada);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignTecnicsObject(resultSet);
                    ja.add(tDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public ArrayList<TecnicDTO> getTecnicsByModalitatAndPreferencia(boolean modalitat, String preferencia) {
        ArrayList<TecnicDTO> ja = new ArrayList<>();
        String query = "SELECT * FROM tecnics WHERE contractat = ? and preferencia = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, modalitat);
            statement.setString(2, preferencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AssignTecnicsObject(resultSet);
                    ja.add(tDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public String getNomTecnic(String username) {
        String query = "SELECT nom FROM usuaris WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nom");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
