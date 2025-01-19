package com.emsi.service;

import ma.emdi.connection.ConnectionJdbc;
import java.sql.*;
import java.util.*;

public class DashboardService {

    public int getTotalClients() {
        String sql = "SELECT COUNT(*) FROM client";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total clients: " + e.getMessage());
        }
        return 0;
    }

    public int getAvailableRooms() {
        String sql = "SELECT COUNT(*) FROM chambre WHERE status = 'available'";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available rooms: " + e.getMessage());
        }
        return 0;
    }

    public int getActiveReservations() {
        String sql = "SELECT COUNT(*) FROM reservation WHERE status = 'confirmed'";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active reservations: " + e.getMessage());
        }
        return 0;
    }

    public double getTodayRevenue() {
        // Calculate revenue using the price from categorie table
        String sql = "SELECT SUM(cat.prix * DATEDIFF(r.date_fin, r.date_debut)) as total " +
                "FROM reservation r " +
                "JOIN chambre ch ON r.chamber_id = ch.id " +
                "JOIN categorie cat ON ch.categorie_id = cat.id " +
                "WHERE DATE(r.date_debut) = CURRENT_DATE AND r.status = 'confirmed'";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Double total = rs.getDouble("total");
                return total != null ? total : 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting today's revenue: " + e.getMessage());
        }
        return 0.0;
    }

    public List<Map<String, String>> getRecentActivity() {
        List<Map<String, String>> activities = new ArrayList<>();

        String sql = "SELECT 'Reservation' as type, " +
                "CONCAT(c.nom, ' ', c.prenom, ' - Room ', ch.telephone) as description, " +
                "r.date_debut as date " +
                "FROM reservation r " +
                "JOIN client c ON r.client_id = c.id " +
                "JOIN chambre ch ON r.chamber_id = ch.id " +
                "UNION ALL " +
                "SELECT 'New Client' as type, " +
                "CONCAT(nom, ' ', prenom) as description, " +
                "NOW() as date " +
                "FROM client " +
                "ORDER BY date DESC LIMIT 10";

        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, String> activity = new HashMap<>();
                activity.put("type", rs.getString("type"));
                activity.put("description", rs.getString("description"));
                activity.put("date", formatDate(rs.getTimestamp("date")));
                activities.add(activity);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent activity: " + e.getMessage());
        }
        return activities;
    }

    private String formatDate(Timestamp date) {
        if (date == null)
            return "";
        long diff = System.currentTimeMillis() - date.getTime();
        long days = diff / (24 * 60 * 60 * 1000);

        if (days == 0) {
            return "Today " + String.format("%02d:%02d", date.getHours(), date.getMinutes());
        } else if (days == 1) {
            return "Yesterday";
        } else {
            return String.format("%tF", date);
        }
    }

    public Map<String, Object> getExtendedStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total revenue query
        String revenueSql = "SELECT SUM(total_price) as total, " +
                "((SUM(total_price) - LAG(SUM(total_price)) OVER ()) / LAG(SUM(total_price)) OVER () * 100) as growth "
                +
                "FROM reservation " +
                "WHERE status = 'confirmed' " +
                "GROUP BY MONTH(date_debut) " +
                "ORDER BY MONTH(date_debut) DESC LIMIT 1";

        // Monthly bookings
        String bookingsSql = "SELECT COUNT(*) as count, " +
                "((COUNT(*) - LAG(COUNT(*)) OVER ()) / LAG(COUNT(*)) OVER () * 100) as growth " +
                "FROM reservation " +
                "GROUP BY MONTH(date_debut) " +
                "ORDER BY MONTH(date_debut) DESC LIMIT 1";

        // Average stay duration
        String avgStaySql = "SELECT AVG(DATEDIFF(date_fin, date_debut)) as avg_stay " +
                "FROM reservation " +
                "WHERE status = 'confirmed'";

        // Room occupancy rate
        String occupancySql = "SELECT (COUNT(CASE WHEN status = 'occupied' THEN 1 END) * 100.0 / COUNT(*)) as rate " +
                "FROM chambre";

        try (Connection conn = ConnectionJdbc.getCnx()) {
            // Execute queries and populate stats map
            try (Statement stmt = conn.createStatement()) {
                // Execute revenue query
                try (ResultSet rs = stmt.executeQuery(revenueSql)) {
                    if (rs.next()) {
                        stats.put("revenue", rs.getDouble("total"));
                        stats.put("revenue_growth", rs.getDouble("growth"));
                    }
                }

                // Execute other queries similarly
            }
        } catch (SQLException e) {
            System.err.println("Error getting extended stats: " + e.getMessage());
        }

        return stats;
    }
}