package com.emsi.service;

import java.sql.*;
import com.emsi.entities.Chamber;
import com.emsi.entities.Categorie;
import ma.emdi.connection.ConnectionJdbc;

public class ChambreService {
    private Connection connection;

    public ChambreService() {
        this.connection = ConnectionJdbc.getCnx();
    }

    public Chamber findById(int id) {
        String sql = "SELECT * FROM chambre WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToChambre(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Chamber mapResultSetToChambre(ResultSet rs) throws SQLException {
        Chamber chambre = new Chamber();
        chambre.setId(rs.getInt("id"));
        chambre.setTelephone(rs.getString("telephone"));
        
        Categorie categorie = new Categorie();
        categorie.setId(rs.getInt("categorie_id"));
        chambre.setCategorie(categorie);
        
        return chambre;
    }
} 