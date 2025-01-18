package com.gui.ma;

import com.gui.utils.StyleUtils;
import com.emsi.entities.Client;
import ma.emdi.connection.ConnectionJdbc;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;

public class ClientDashboard extends JInternalFrame {
    private final Client currentClient;
    private JTable reservationsTable;
    private JLabel totalReservationsLabel;
    private JLabel nextReservationLabel;

    public ClientDashboard(Client client) {
        super("My Dashboard", true, false, true, true);
        this.currentClient = client;
        setLayout(new BorderLayout());
        initComponents();
        loadClientData();
        setSize(800, 600);
        setLocation(50, 50);
        setVisible(true);
    }

    private void initComponents() {
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBackground(new Color(247, 248, 252));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome Panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Welcome, " + currentClient.getNom() + " " + currentClient.getPrenom());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);

        // Stats cards
        totalReservationsLabel = createStatsCard("Total Reservations", "0");
        nextReservationLabel = createStatsCard("Next Reservation", "None");

        statsPanel.add(totalReservationsLabel.getParent());
        statsPanel.add(nextReservationLabel.getParent());

        // Reservations Table
        JPanel reservationsPanel = createReservationsPanel();

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton newReservationBtn = createActionButton("New Reservation", "➕", new Color(111, 66, 193));
        newReservationBtn.addActionListener(e -> openNewReservation());

        JButton viewStatsBtn = createActionButton("My Statistics", "📊", new Color(32, 151, 243));
        viewStatsBtn.addActionListener(e -> openStatistics());

        actionPanel.add(newReservationBtn);
        actionPanel.add(viewStatsBtn);

        // Add components to main container
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(welcomePanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(reservationsPanel, BorderLayout.CENTER);
        mainContainer.add(actionPanel, BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(createCardBorder());

        // Title
        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Table
        String[] columns = { "Room", "Check-in", "Check-out", "Status" };
        reservationsTable = new JTable(new DefaultTableModel(columns, 0));
        reservationsTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadClientData() {
        loadReservations();
        updateStats();
    }

    private void loadReservations() {
        DefaultTableModel model = (DefaultTableModel) reservationsTable.getModel();
        model.setRowCount(0);

        String sql = "SELECT ch.telephone, r.date_debut, r.date_fin, r.status " +
                "FROM reservation r " +
                "JOIN chambre ch ON r.chamber_id = ch.id " +
                "WHERE r.client_id = ? " +
                "ORDER BY r.date_debut DESC";

        try (Connection conn = ConnectionJdbc.getCnx();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentClient.getId());
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                model.addRow(new Object[] {
                        "Room " + rs.getString("telephone"),
                        dateFormat.format(rs.getDate("date_debut")),
                        dateFormat.format(rs.getDate("date_fin")),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStats() {
        // Update total reservations
        String countSql = "SELECT COUNT(*) FROM reservation WHERE client_id = ?";
        String nextSql = "SELECT date_debut FROM reservation " +
                "WHERE client_id = ? AND date_debut >= CURRENT_DATE " +
                "ORDER BY date_debut ASC LIMIT 1";

        try (Connection conn = ConnectionJdbc.getCnx()) {
            // Get total reservations
            try (PreparedStatement stmt = conn.prepareStatement(countSql)) {
                stmt.setInt(1, currentClient.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalReservationsLabel.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Get next reservation
            try (PreparedStatement stmt = conn.prepareStatement(nextSql)) {
                stmt.setInt(1, currentClient.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    nextReservationLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JLabel createStatsCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(createCardBorder());
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return valueLabel;
    }

    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 225, 229), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JButton createActionButton(String text, String icon, Color color) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(color);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(color);
            }
        });

        return button;
    }

    private void openNewReservation() {
        ReservationForm form = new ReservationForm(currentClient);
        getDesktopPane().add(form);
        form.setVisible(true);
    }

    private void openStatistics() {
        StatisticsViewer viewer = new StatisticsViewer(currentClient);
        getDesktopPane().add(viewer);
        try {
            viewer.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
        viewer.setVisible(true);
    }
}