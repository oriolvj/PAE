package com.pae.pae.repositories;

import com.pae.pae.models.LlocTreballDTO;
import com.pae.pae.models.MaterialDTO;
import com.pae.pae.utils.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@Repository
public class MaterialRepository {
    private MaterialDTO mDTO = null;

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.URL, Config.USER, Config.PWD);
    }
    private void AssignarMaterialObject(ResultSet resultSet) throws SQLException {
        try {
            mDTO = new MaterialDTO(
                    resultSet.getInt("id"),
                    resultSet.getString("marca"),
                    resultSet.getString("model"),
                    resultSet.getInt("quantitat"),
                    resultSet.getInt("preu_lloguer"),
                    resultSet.getBoolean("propietari")
            );
        }catch (SQLException e) {
            throw new SQLException("Error al obtener datos del ResultSet", e);
        }
    }


    public ArrayList<MaterialDTO> getMaterials() {
        ArrayList<MaterialDTO> materials = new ArrayList<>();
        String query = "SELECT * FROM materials";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AssignarMaterialObject(resultSet);
                materials.add(mDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public MaterialDTO getMaterial(int id) {
        String query = "SELECT * FROM materials WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    AssignarMaterialObject(resultSet);
                    return mDTO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addMaterial(Map<String, String> newmaterialRequest) {
        String checkQuery = "SELECT COUNT(*) FROM materials WHERE marca = ? AND model = ?";
        String insertQuery = "INSERT INTO materials (marca, model, quantitat, preu_lloguer, propietari) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection()) {
            // Check if the material already exists
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, newmaterialRequest.get("marca"));
                checkStatement.setString(2, newmaterialRequest.get("model"));
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        // Material already exists
                        System.out.println("Material already exists");
                        return false;
                    }
                }
            }
            // Insert the new material
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, newmaterialRequest.get("marca"));
                insertStatement.setString(2, newmaterialRequest.get("model"));
                insertStatement.setInt(3, Integer.parseInt(newmaterialRequest.get("quantitat")));
                insertStatement.setInt(4, Integer.parseInt(newmaterialRequest.get("preu_lloguer")));
                insertStatement.setBoolean(5, Boolean.parseBoolean(newmaterialRequest.get("propietari")));
                insertStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean Materialremove(int id) {
        String query = "DELETE FROM materials WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public boolean MaterialModify ( int id, Map<String, String > modifyRequest){
            String checkQuery = "SELECT COUNT(*) FROM materials WHERE id = ?";
            String updateQuery = "UPDATE materials SET marca = ?, model = ?, quantitat = ?, preu_lloguer = ?, propietari = ? WHERE id = ?";
            try (Connection connection = getConnection()) {
                // Check if id exists
                try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                    checkStatement.setInt(1, id);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (!resultSet.next() || resultSet.getInt(1) == 0) {
                            // id does not exist
                            return false;
                        }
                    }
                }
                // Update the material
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, modifyRequest.get("marca"));
                    updateStatement.setString(2, modifyRequest.get("model"));
                    updateStatement.setInt(3, Integer.parseInt(modifyRequest.get("quantitat")));
                    updateStatement.setInt(4, Integer.parseInt(modifyRequest.get("preu_lloguer")));
                    updateStatement.setBoolean(5, Boolean.parseBoolean(modifyRequest.get("propietari")));
                    updateStatement.setInt(6, id);
                    return updateStatement.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
