/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.service.DashboardService;
import com.gui.utils.IconUtils;
import ma.emdi.connection.ConnectionJdbc;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class AdminDashboard extends javax.swing.JInternalFrame {
    private final DashboardService dashboardService;
    private JTable activityTable;
    private JLabel totalClientsLabel;
    private JLabel totalRoomsLabel;
    private JLabel totalReservationsLabel;
    private JLabel revenueLabel;
    private Timer refreshTimer;

    /** Creates new form AdminDashboard */
    public AdminDashboard() {
        super("Admin Dashboard", true, true, true, true);
        this.dashboardService = new DashboardService();
        IconUtils.setInternalFrameIcon(this, IconUtils.DASHBOARD_ICON);
        initComponents();
        customInitComponents();
        loadStats();
        loadRecentBookings();
        setSize(1000, 700);
        setLocation(50, 50);

        // Set up auto-refresh timer (refresh every 5 seconds)
        refreshTimer = new Timer(5000, e -> {
            loadStats();
            loadRecentBookings();
        });
        refreshTimer.start();
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }

    private void customInitComponents() {
        // Main panel setup
        mainPanel.setBackground(new Color(247, 248, 252));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats Panel
        statsPanel.setLayout(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);

        // Initialize labels
        totalClientsLabel = new JLabel("0");
        totalRoomsLabel = new JLabel("0");
        totalReservationsLabel = new JLabel("0");
        revenueLabel = new JLabel("0.00 DH");

        // Create stat cards with gradients
        statsPanel.add(createGradientStatCard("Total Clients", totalClientsLabel,
                new Color(14, 165, 233), new Color(56, 189, 248), "👥"));
        statsPanel.add(createGradientStatCard("Available Rooms", totalRoomsLabel,
                new Color(34, 197, 94), new Color(74, 222, 128), "🏠"));
        statsPanel.add(createGradientStatCard("Active Reservations", totalReservationsLabel,
                new Color(249, 115, 22), new Color(251, 146, 60), "📅"));
        statsPanel.add(createGradientStatCard("Today's Revenue", revenueLabel,
                new Color(139, 92, 246), new Color(167, 139, 250), "💰"));

        // Activity Table
        String[] columns = { "Type", "Details", "Date" };
        activityTable = new JTable(new DefaultTableModel(columns, 0));
        activityTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        JPanel activityPanel = new JPanel(new BorderLayout(0, 10));
        activityPanel.setOpaque(false);
        activityPanel.setBorder(createCardBorder());

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to main panel
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(activityPanel, BorderLayout.CENTER);
    }

    private void loadRecentBookings() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                DefaultTableModel model = (DefaultTableModel) activityTable.getModel();
                model.setRowCount(0); // Clear existing rows

                String sql = "SELECT r.id, c.nom, c.prenom, ch.telephone, r.date_debut, r.date_fin, r.status " +
                        "FROM reservation r " +
                        "JOIN client c ON r.client_id = c.id " +
                        "JOIN chambre ch ON r.chamber_id = ch.id " +
                        "ORDER BY r.date_debut DESC LIMIT 10";

                try (Connection conn = ConnectionJdbc.getCnx();
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        String clientName = rs.getString("nom") + " " + rs.getString("prenom");
                        String roomNumber = rs.getString("telephone");
                        java.sql.Date startDate = rs.getDate("date_debut");
                        String status = rs.getString("status");

                        String details = String.format("Room %s - %s - %s",
                                roomNumber, clientName, status);

                        model.addRow(new Object[] {
                                "Reservation",
                                details,
                                new SimpleDateFormat("dd/MM/yyyy").format(startDate)
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Error loading recent bookings: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        }.execute();
    }

    private JPanel createGradientStatCard(String title, JLabel valueLabel, Color startColor, Color endColor,
            String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(createCardBorder());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        card.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(startColor);

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadStats() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Update stats in background
                    int totalClients = dashboardService.getTotalClients();
                    int availableRooms = dashboardService.getAvailableRooms();
                    int activeReservations = dashboardService.getActiveReservations();
                    double todayRevenue = dashboardService.getTodayRevenue();
                    java.util.List<Map<String, String>> activities = dashboardService.getRecentActivity();

                    // Update UI on EDT
                    SwingUtilities.invokeLater(() -> {
                        totalClientsLabel.setText(String.valueOf(totalClients));
                        totalRoomsLabel.setText(String.valueOf(availableRooms));
                        totalReservationsLabel.setText(String.valueOf(activeReservations));
                        revenueLabel.setText(String.format("%.2f DH", todayRevenue));

                        DefaultTableModel model = (DefaultTableModel) activityTable.getModel();
                        model.setRowCount(0);
                        for (Map<String, String> activity : activities) {
                            model.addRow(new Object[] {
                                    activity.get("type"),
                                    activity.get("description"),
                                    activity.get("date")
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(AdminDashboard.this,
                                "Error loading stats: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
                return null;
            }
        };
        worker.execute();
    }

    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 225, 229), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Generated code below - do not modify
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Admin Dashboard");

        mainPanel.setLayout(new java.awt.BorderLayout(20, 20));
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel statsPanel;
    // End of variables declaration//GEN-END:variables
}