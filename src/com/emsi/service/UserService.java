package com.emsi.service;

import com.emsi.entities.User;
import ma.emdi.connection.ConnectionJdbc;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserService {
    private JTable table; // Reference to the UI table
    private Connection conn;
    private ClientService clientService;

    public UserService() {
        conn = ConnectionJdbc.getCnx();
        clientService = new ClientService();
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback to plain password if hashing fails
        }
    }

    private boolean validateInput(String username, String password, String nom, String prenom, String telephone,
            String email) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                telephone == null || telephone.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            return false;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }

        // Validate phone number (assuming 10 digits)
        if (!telephone.matches("\\d{10}")) {
            return false;
        }

        return true;
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user, String nom, String prenom, String telephone, String email) {
        Connection conn = null;
        PreparedStatement userStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConnectionJdbc.getCnx();
            conn.setAutoCommit(false); // Start transaction

            // First create the user
            String userSql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
            userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, hashPassword(user.getPassword()));
            userStmt.setString(3, "CLIENT"); // Default role for registration

            int affectedRows = userStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // Get the generated user ID
            generatedKeys = userStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                return false;
            }

            int userId = generatedKeys.getInt(1);

            // Now create the client profile
            String clientSql = "INSERT INTO client (nom, prenom, telephone, email, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement clientStmt = conn.prepareStatement(clientSql);
            clientStmt.setString(1, nom);
            clientStmt.setString(2, prenom);
            clientStmt.setString(3, telephone);
            clientStmt.setString(4, email);
            clientStmt.setInt(5, userId);

            affectedRows = clientStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null)
                    generatedKeys.close();
                if (userStmt != null)
                    userStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = ConnectionJdbc.getCnx();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        }
        return users;
    }

    public boolean update(User user) {
        String sql = "UPDATE user SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful update
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(User user) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            boolean success = stmt.executeUpdate() > 0;
            if (success && table != null) {
                refreshTable(); // Refresh the table after successful deletion
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }

    private void refreshTable() {
        if (table != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows
            List<User> users = findAll();
            for (User user : users) {
                model.addRow(new Object[] {
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                });
            }
        }
    }
}