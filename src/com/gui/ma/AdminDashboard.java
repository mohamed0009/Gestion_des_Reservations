package com.gui.ma;

import com.gui.utils.StyleUtils;
import com.emsi.service.DashboardService;
import ma.emdi.connection.ConnectionJdbc;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class AdminDashboard extends JInternalFrame {
    private JMenuBar menuBar;
    private JLabel totalClientsLabel;
    private JLabel totalRoomsLabel;
    private JLabel totalReservationsLabel;
    private JLabel revenueLabel;
    private JTable activityTable;
    private DashboardService dashboardService;
    private JTextField searchField;

    public AdminDashboard() {
        super("Dashboard", true, false, true, true);
        this.dashboardService = new DashboardService();
        this.searchField = new JTextField(15);
        // Initialize labels
        totalClientsLabel = new JLabel("0");
        totalRoomsLabel = new JLabel("0");
        totalReservationsLabel = new JLabel("0");
        revenueLabel = new JLabel("0 DH");
        setLayout(new BorderLayout());
        initComponents();
        loadStats();
        setVisible(true);
    }

    private void initComponents() {
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBackground(new Color(247, 248, 252));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize activity table first
        String[] columns = { "Activity Type", "Details", "Date/Time" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        activityTable = new JTable(model);

        // Set column widths
        activityTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        activityTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        activityTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Set custom cell renderer for better formatting
        activityTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 0) {
                    setHorizontalAlignment(JLabel.CENTER);
                    setText(value.toString());
                    setForeground(
                            value.toString().equals("Reservation") ? new Color(111, 66, 193) : new Color(32, 151, 243));
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setForeground(new Color(71, 85, 105));
                    setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        });

        // Top Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        // Create modern stat cards with gradients
        statsPanel.add(createGradientStatCard("New Clients", totalClientsLabel,
                new Color(111, 66, 193), new Color(165, 108, 255), "👥"));
        statsPanel.add(createGradientStatCard("Available Rooms", totalRoomsLabel,
                new Color(32, 151, 243), new Color(85, 178, 245), "🏠"));
        statsPanel.add(createGradientStatCard("Active Bookings", totalReservationsLabel,
                new Color(255, 145, 83), new Color(255, 175, 128), "📅"));
        statsPanel.add(createGradientStatCard("Today's Revenue", revenueLabel,
                new Color(75, 192, 111), new Color(124, 217, 152), "💰"));

        // Center Panel with Tables
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);

        // Recent Bookings Table
        JPanel bookingsPanel = createTablePanel("Recent Bookings", activityTable);

        // Right side stats panel
        JPanel quickStatsPanel = new JPanel(new BorderLayout(0, 15));
        quickStatsPanel.setBackground(Color.WHITE);
        quickStatsPanel.setBorder(createCardBorder());
        quickStatsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats header
        JLabel statsTitle = new JLabel("Quick Statistics");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        quickStatsPanel.add(statsTitle, BorderLayout.NORTH);

        // Stats content
        JPanel statsContent = new JPanel(new GridLayout(4, 1, 0, 15));
        statsContent.setOpaque(false);

        statsContent.add(createQuickStatRow("Total Revenue", "50,000 DH", "↑ 12%"));
        statsContent.add(createQuickStatRow("Monthly Bookings", "125", "↑ 8%"));
        statsContent.add(createQuickStatRow("Average Stay", "3.5 days", "↓ 2%"));
        statsContent.add(createQuickStatRow("Room Occupancy", "75%", "↑ 5%"));

        quickStatsPanel.add(statsContent, BorderLayout.CENTER);

        // Search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });

        // Add panels to center
        centerPanel.add(bookingsPanel);
        centerPanel.add(quickStatsPanel);

        // Bottom Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        // Action buttons with handlers
        JButton newReservationBtn = createActionButton("New Reservation", "➕", new Color(111, 66, 193));
        newReservationBtn.addActionListener(e -> {
            ReservationForm form = new ReservationForm();
            getDesktopPane().add(form);
            form.setVisible(true);
        });

        JButton addClientBtn = createActionButton("Add Client", "👥", new Color(32, 151, 243));
        addClientBtn.addActionListener(e -> {
            ClientForm form = new ClientForm();
            getDesktopPane().add(form);
            form.setVisible(true);
        });

        JButton addRoomBtn = createActionButton("Manage Rooms", "🏠", new Color(75, 192, 111));
        addRoomBtn.addActionListener(e -> {
            ChamberForm form = new ChamberForm();
            getDesktopPane().add(form);
            form.setVisible(true);
        });

        actionPanel.add(newReservationBtn);
        actionPanel.add(addClientBtn);
        actionPanel.add(addRoomBtn);

        // Add all to main container
        mainContainer.add(statsPanel, BorderLayout.NORTH);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(actionPanel, BorderLayout.SOUTH);

        add(mainContainer);

        // Add Statistics Menu Item
        JMenuItem statsMenuItem = new JMenuItem("Statistics & Reports");
        StyleUtils.styleMenuItem(statsMenuItem);
        statsMenuItem.addActionListener(e -> openStatisticsViewer());

        // Add to appropriate menu
        // ... rest of existing code ...

        // After initializing the activity table
        loadRecentBookings();
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) activityTable.getModel();
        DefaultTableModel filteredModel = new DefaultTableModel(
                new String[] { "Type", "Description", "Date" }, 0);

        for (int i = 0; i < model.getRowCount(); i++) {
            String type = model.getValueAt(i, 0).toString().toLowerCase();
            String desc = model.getValueAt(i, 1).toString().toLowerCase();
            String date = model.getValueAt(i, 2).toString().toLowerCase();

            if (type.contains(searchText) || desc.contains(searchText) || date.contains(searchText)) {
                filteredModel.addRow(new Object[] {
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2)
                });
            }
        }
        activityTable.setModel(filteredModel);
    }

    private void loadStats() {
        // Update stats
        totalClientsLabel.setText(String.valueOf(dashboardService.getTotalClients()));
        totalRoomsLabel.setText(String.valueOf(dashboardService.getAvailableRooms()));
        totalReservationsLabel.setText(String.valueOf(dashboardService.getActiveReservations()));
        revenueLabel.setText(String.format("%.2f DH", dashboardService.getTodayRevenue()));

        // Update activity table
        DefaultTableModel model = (DefaultTableModel) activityTable.getModel();
        model.setRowCount(0);
        for (Map<String, String> activity : dashboardService.getRecentActivity()) {
            model.addRow(new Object[] {
                    activity.get("type"),
                    activity.get("description"),
                    activity.get("date")
            });
        }
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

    private JPanel createTablePanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(createCardBorder());

        // Title with search
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 32, 34));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 225, 229), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private JPanel createQuickStatRow(String label, String value, String change) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComp.setForeground(new Color(100, 100, 100));

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        valuePanel.setOpaque(false);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel changeComp = new JLabel(change);
        changeComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changeComp.setForeground(change.startsWith("↑") ? new Color(75, 192, 111) : new Color(255, 99, 132));

        valuePanel.add(valueComp);
        valuePanel.add(changeComp);

        row.add(labelComp, BorderLayout.WEST);
        row.add(valuePanel, BorderLayout.EAST);

        return row;
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

    private void openStatisticsViewer() {
        StatisticsViewer viewer = new StatisticsViewer(null);
        getDesktopPane().add(viewer);
        try {
            viewer.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
        viewer.setVisible(true);
    }

    private void initializeMenuBar() {
        // ... existing menu initialization code ...

        // Add Dashboard menu item
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        StyleUtils.styleMenuItem(dashboardItem);
        dashboardItem.addActionListener(e -> openDashboard());
        menuBar.add(dashboardItem);

        // ... rest of existing code ...
    }

    private void openDashboard() {
        DashboardPanel dashboard = new DashboardPanel();
        JInternalFrame frame = new JInternalFrame("Dashboard", true, true, true, true);
        frame.setContentPane(dashboard);
        frame.setSize(1000, 800);
        frame.setLocation(50, 50);
        frame.setVisible(true);
        getDesktopPane().add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    private void loadRecentBookings() {
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
        }
    }
}