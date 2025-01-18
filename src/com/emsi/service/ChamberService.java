package com.emsi.service;

import ma.emdi.connection.ConnectionJdbc;
import com.emsi.dao.IDAO;
import com.emsi.entities.Chamber;
import com.emsi.entities.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ChamberService implements IDAO<Chamber> {
    private JTable table; // Reference to the UI table

    public void setTable(JTable table) {
        this.table = table;
    }

    @Override
    public boolean create(Chamber chamber) {
        String sql = "INSERT INTO chambre (telephone, categorie_id) VALUES (?, ?)";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, chamber.getTelephone());
            stmt.setInt(2, chamber.getCategorieId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        chamber.setId(generatedKeys.getInt(1));
                        System.out.println("Chambre ajoutée avec succès avec ID : " + chamber.getId());
                    }
                }
                if (table != null) {
                    refreshTable();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la chambre (telephone: " + chamber.getTelephone() +
                    ", categorie_id: " + chamber.getCategorieId() + "): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Chamber chamber) {
        String sql = "UPDATE chambre SET telephone = ?, categorie_id = ? WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chamber.getTelephone());
            stmt.setInt(2, chamber.getCategorieId());
            stmt.setInt(3, chamber.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful update
            }
            return success;

        } catch (SQLException e) {
            System.err.println(
                    "Erreur lors de la mise à jour de la chambre (id: " + chamber.getId() + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Chamber chamber) {
        String sql = "DELETE FROM chambre WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chamber.getId());
            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful deletion
            }
            return success;

        } catch (SQLException e) {
            System.err.println(
                    "Erreur lors de la suppression de la chambre (id: " + chamber.getId() + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void refreshTable() {
        if (table != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows
            List<Chamber> chambers = findAll();
            for (Chamber chamber : chambers) {
                model.addRow(new Object[] {
                        chamber.getId(),
                        chamber.getTelephone(),
                        chamber.getCategorieId(),
                        chamber.getStatus()
                });
            }
        }
    }

    @Override
    public List<Chamber> findAll() {
        List<Chamber> chambers = new ArrayList<>();
        String sql = "SELECT * FROM chambre";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Chamber chamber = mapResultSetToChamber(rs); // Refactored mapping logic
                chambers.add(chamber);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des chambres : " + e.getMessage());
            e.printStackTrace();
        }
        return chambers;
    }

    @Override
    public Chamber findById(int id) {
        String sql = "SELECT * FROM Chambre WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToChamber(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la chambre (id: " + id + "): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Chamber> findAvailable() {
        List<Chamber> chambers = new ArrayList<>();
        String sql = "SELECT ch.*, cat.libelle FROM " +
                "chambre ch JOIN categorie cat ON ch.categorie_id = cat.id " +
                "WHERE ch.status = 'available'";

        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Chamber chamber = new Chamber();
                chamber.setId(rs.getInt("id"));
                chamber.setTelephone(rs.getString("telephone"));
                chamber.setStatus(rs.getString("status"));

                Categorie cat = new Categorie();
                cat.setId(rs.getInt("categorie_id"));
                cat.setLibelle(rs.getString("libelle"));
                chamber.setCategorie(cat);

                chambers.add(chamber);
            }
        } catch (SQLException e) {
            System.err.println("Error finding available rooms: " + e.getMessage());
        }
        return chambers;
    }

    private Chamber mapResultSetToChamber(ResultSet rs) throws SQLException {
        return new Chamber(
                rs.getInt("id"),
                rs.getString("telephone"),
                rs.getInt("categorie_id"));
    }

    public List<Chamber> findChambreByCategorie(Categorie categorie) {
        List<Chamber> chambers = new ArrayList<>();
        String sql = "SELECT * FROM chambre WHERE categorie_id = ?";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categorie.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                chambers.add(mapResultSetToChamber(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding chambers by category: " + e.getMessage());
        }
        return chambers;
    }
}
