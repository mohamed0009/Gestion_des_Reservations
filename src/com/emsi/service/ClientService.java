package com.emsi.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ma.emdi.connection.ConnectionJdbc; // Votre classe de connexion
import com.emsi.dao.IDAO;
import com.emsi.entities.Client;

public class ClientService implements IDAO<Client> {

    @Override
    public boolean create(Client client) {
        String sql = "INSERT INTO client (nom, prenom, telephone, email, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getEmail());
            stmt.setInt(5, client.getUserId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1)); // Get the generated ID
                    System.out.println("Client added with ID: " + client.getId());
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding client: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
        return clients;
    }

    @Override
    public Client findById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToClient(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client (id: " + id + "): " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Client client) {
        String sql = "UPDATE client SET nom = ?, prenom = ?, telephone = ?, email = ?, user_id = ? WHERE id = ?";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getEmail());
            stmt.setInt(5, client.getUserId());
            stmt.setInt(6, client.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating client (id: " + client.getId() + "): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Client client) {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, client.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err
                    .println("Erreur lors de la suppression du client (id: " + client.getId() + "): " + e.getMessage());
            return false;
        }
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("telephone"),
                rs.getString("email"));
        client.setId(rs.getInt("id"));
        client.setUserId(rs.getInt("user_id"));
        return client;
    }

    public List<Client> findByEmail(String email) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE email = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client par email (" + email + "): " + e.getMessage());
        }
        return clients.isEmpty() ? null : clients;
    }

    public Client findByUserId(int userId) {
        String sql = "SELECT * FROM client WHERE user_id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
