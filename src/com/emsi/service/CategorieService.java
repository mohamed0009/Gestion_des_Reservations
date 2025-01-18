package com.emsi.service;

import ma.emdi.connection.ConnectionJdbc;
import com.emsi.dao.IDAO;
import com.emsi.entities.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CategorieService implements IDAO<Categorie> {
    private JTable table; // Reference to the UI table

    public void setTable(JTable table) {
        this.table = table;
    }

    @Override
    public boolean create(Categorie categorie) {
        String checkSql = "SELECT COUNT(*) FROM categorie WHERE libelle = ?";
        String insertSql = "INSERT INTO categorie (libelle, description, prix) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Vérifier si la catégorie existe déjà
            checkStmt.setString(1, categorie.getLibelle());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out
                        .println("Erreur : La catégorie avec le libelle '" + categorie.getLibelle() + "' existe déjà.");
                return false;
            }

            // Insérer si elle n'existe pas
            insertStmt.setString(1, categorie.getLibelle());
            insertStmt.setString(2, categorie.getDescription());
            insertStmt.setDouble(3, categorie.getPrix());
            boolean success = insertStmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable();
            }
            return success;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Categorie categorie) {
        String sql = "UPDATE categorie SET libelle = ?, description = ?, prix = ? WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categorie.getLibelle());
            stmt.setString(2, categorie.getDescription());
            stmt.setDouble(3, categorie.getPrix());
            stmt.setInt(4, categorie.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful update
            }
            return success;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la catégorie : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Categorie categorie) {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categorie.getId());
            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful deletion
            }
            return success;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void refreshTable() {
        if (table != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows
            List<Categorie> categories = findAll();
            for (Categorie categorie : categories) {
                model.addRow(new Object[] {
                        categorie.getId(),
                        categorie.getLibelle(),
                        categorie.getDescription(),
                        categorie.getPrix()
                });
            }
        }
    }

    @Override
    public List<Categorie> findAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categorie";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getDouble("prix"));
                categorie.setId(rs.getInt("id"));
                categories.add(categorie);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories : " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Categorie findById(int id) {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getDouble("prix"));
                categorie.setId(rs.getInt("id"));
                return categorie;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la catégorie : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
