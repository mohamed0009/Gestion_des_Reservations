/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.entities.*;
import com.emsi.service.*;
import com.gui.utils.StyleUtils;
import com.gui.utils.IconUtils;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReservationForm extends JInternalFrame {
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 35;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private JComboBox<ClientComboItem> clientCombo;
    private JComboBox<ChamberComboItem> chamberCombo;
    private JDateChooser dateDebutChooser;
    private JDateChooser dateFinChooser;
    private JButton submitButton;
    private JButton addreservation;
    private JButton jButton2;
    private JButton jButton3;
    private JTable listereservation;
    private JTextField clienttxt;
    private JTextField chambertxt;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;

    private ClientService clientService;
    private ChamberService chamberService;
    private ReservationService reservationService;

    private Client currentClient;
    private DefaultTableModel model;

    // Helper classes for ComboBox items
    private static class ClientComboItem {
        int id;
        String display;

        public ClientComboItem(int id, String nom, String prenom) {
            this.id = id;
            this.display = nom + " " + prenom;
        }

        @Override
        public String toString() {
            return display;
        }
    }

    private static class ChamberComboItem {
        int id;
        String display;

        public ChamberComboItem(int id, String telephone, String categorie) {
            this.id = id;
            this.display = "Room #" + telephone + " (" + categorie + ")";
        }

        @Override
        public String toString() {
            return display;
        }
    }

    public ReservationForm(Client client) {
        super("New Reservation", true, true, true, true);
        this.currentClient = client;
        this.clientService = new ClientService();
        this.chamberService = new ChamberService();
        this.reservationService = new ReservationService();

        initComponents();
        loadComboBoxData();

        // If client is provided, select and disable client selection
        if (currentClient != null) {
            for (int i = 0; i < clientCombo.getItemCount(); i++) {
                ClientComboItem item = clientCombo.getItemAt(i);
                if (item.id == currentClient.getId()) {
                    clientCombo.setSelectedIndex(i);
                    clientCombo.setEnabled(false);
                    break;
                }
            }
        }

        setSize(400, 600);
        setLocation(100, 100);
    }

    // Default constructor for admin use
    public ReservationForm() {
        reservationService = new ReservationService();
        clientService = new ClientService();
        chamberService = new ChamberService();

        // Set basic properties first
        setTitle("Gestion des Réservations");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        IconUtils.setInternalFrameIcon(this, IconUtils.RESERVATION_ICON);

        // Initialize components
        initComponents();
        applyStyles();

        // Set minimum size
        setMinimumSize(new Dimension(800, 600));

        // Initialize the table model
        model = (DefaultTableModel) listereservation.getModel();
        load();

        // Pack and set size after all components are initialized
        pack();
        setSize(800, 600);
    }

    private void initializeServices() {
        clientService = new ClientService();
        chamberService = new ChamberService();
        reservationService = new ReservationService();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 225, 229), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Title
        JLabel titleLabel = new JLabel("New Reservation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(5, 0, 15, 0);

        // Client Selection
        JLabel clientLabel = new JLabel("Select Client:");
        clientLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clientLabel.setForeground(new Color(71, 85, 105));
        mainPanel.add(clientLabel, gbc);

        clientCombo = new JComboBox<>();
        clientCombo.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        StyleUtils.styleComboBox(clientCombo);
        mainPanel.add(clientCombo, gbc);

        // Room Selection
        JLabel chamberLabel = new JLabel("Select Room:");
        chamberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chamberLabel.setForeground(new Color(71, 85, 105));
        mainPanel.add(chamberLabel, gbc);

        chamberCombo = new JComboBox<>();
        chamberCombo.setPreferredSize(new Dimension(300, 35));
        StyleUtils.styleComboBox(chamberCombo);
        mainPanel.add(chamberCombo, gbc);

        // Check-in Date
        JLabel dateDebutLabel = new JLabel("Check-in Date:");
        dateDebutLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateDebutLabel.setForeground(new Color(71, 85, 105));
        mainPanel.add(dateDebutLabel, gbc);

        dateDebutChooser = new JDateChooser();
        dateDebutChooser.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        dateDebutChooser.setDateFormatString(DATE_FORMAT);
        dateDebutChooser.setMinSelectableDate(new Date()); // Can't select past dates
        StyleUtils.styleDateChooser(dateDebutChooser);
        mainPanel.add(dateDebutChooser, gbc);

        // Check-out Date
        JLabel dateFinLabel = new JLabel("Check-out Date:");
        dateFinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateFinLabel.setForeground(new Color(71, 85, 105));
        mainPanel.add(dateFinLabel, gbc);

        dateFinChooser = new JDateChooser();
        dateFinChooser.setPreferredSize(new Dimension(300, 35));
        dateFinChooser.setDateFormatString("yyyy-MM-dd");
        dateFinChooser.setMinSelectableDate(new Date());
        StyleUtils.styleDateChooser(dateFinChooser);
        mainPanel.add(dateFinChooser, gbc);

        // Submit Button
        submitButton = new JButton("Create Reservation");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(300, 40));
        StyleUtils.styleButton(submitButton, StyleUtils.PRIMARY_COLOR);
        submitButton.addActionListener(e -> createReservation());

        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(submitButton, gbc);

        // Wrap in a container with padding
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(248, 250, 252));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.add(mainPanel, BorderLayout.CENTER);

        add(container, BorderLayout.CENTER);
    }

    private void loadComboBoxData() {
        // Load Clients
        List<Client> clients = clientService.findAll();
        for (Client client : clients) {
            clientCombo.addItem(new ClientComboItem(
                    client.getId(),
                    client.getNom(),
                    client.getPrenom()));
        }

        // Load Available Rooms
        List<Chamber> chambers = chamberService.findAvailable();
        for (Chamber chamber : chambers) {
            chamberCombo.addItem(new ChamberComboItem(
                    chamber.getId(),
                    chamber.getTelephone(),
                    chamber.getCategorie().getLibelle()));
        }
    }

    private void createReservation() {
        if (!validateInputs()) {
            return;
        }

        ClientComboItem selectedClient = (ClientComboItem) clientCombo.getSelectedItem();
        ChamberComboItem selectedChamber = (ChamberComboItem) chamberCombo.getSelectedItem();
        Reservation reservation = new Reservation();
        reservation.setClientName(selectedClient.display);
        reservation.setRoomNumber(selectedChamber.display);
        reservation.setDateDebut(new java.sql.Date(dateDebutChooser.getDate().getTime()));
        reservation.setDateFin(new java.sql.Date(dateFinChooser.getDate().getTime()));
        reservation.setStatus("pending");

        if (reservationService.create(reservation)) {
            JOptionPane.showMessageDialog(this,
                    "Reservation created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the parent dashboard
            Container parent = this.getParent();
            if (parent instanceof JDesktopPane) {
                JDesktopPane desktop = (JDesktopPane) parent;
                for (JInternalFrame frame : desktop.getAllFrames()) {
                    if (frame instanceof ClientDashboard) {
                        ((ClientDashboard) frame).loadClientData();
                        break;
                    }
                }
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error creating reservation",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        // Check for null values first
        if (clientCombo.getSelectedItem() == null ||
                chamberCombo.getSelectedItem() == null ||
                dateDebutChooser.getDate() == null ||
                dateFinChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check dates
        Date today = new Date();
        Date checkIn = dateDebutChooser.getDate();
        Date checkOut = dateFinChooser.getDate();

        if (checkIn.before(today)) {
            JOptionPane.showMessageDialog(this,
                    "Check-in date cannot be in the past",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkOut.before(checkIn)) {
            JOptionPane.showMessageDialog(this,
                    "Check-out date must be after check-in date",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearForm() {
        clientCombo.setSelectedIndex(-1);
        chamberCombo.setSelectedIndex(-1);
        dateDebutChooser.setDate(null);
        dateFinChooser.setDate(null);
    }

    private void applyStyles() {
        // Style the panels
        StyleUtils.stylePanel(jPanel1);
        StyleUtils.stylePanel(jPanel2);
        StyleUtils.stylePanel(jPanel3);

        // Style text fields
        StyleUtils.styleTextField(clienttxt);
        StyleUtils.styleTextField(chambertxt);

        // Style labels
        StyleUtils.styleLabel(jLabel1);
        StyleUtils.styleLabel(jLabel2);
        StyleUtils.styleLabel(jLabel3);
        StyleUtils.styleLabel(jLabel4);

        // Style buttons
        addreservation.setText("Ajouter Réservation");
        jButton2.setText("Modifier Réservation");
        jButton3.setText("Supprimer Réservation");

        StyleUtils.styleButton(addreservation, StyleUtils.PRIMARY_COLOR);
        StyleUtils.styleButton(jButton2, StyleUtils.SUCCESS_COLOR);
        StyleUtils.styleButton(jButton3, StyleUtils.DANGER_COLOR);

        // Style date choosers
        dateDebutChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateFinChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Style table
        StyleUtils.styleTable(listereservation);
    }

    public void load() {
        model.setRowCount(0);
        if (currentClient != null) {
            // Load only reservations for current client
            for (Reservation reservation : reservationService.findByClient(currentClient)) {
                model.addRow(new Object[] {
                        reservation.getId(),
                        reservation.getClient().getNom() + " " + reservation.getClient().getPrenom(),
                        reservation.getChamber().getTelephone(),
                        reservation.getDateDebut(),
                        reservation.getDateFin(),
                        reservation.getStatus()
                });
            }
        } else {
            // Load all reservations for admin
            for (Reservation reservation : reservationService.findAll()) {
                model.addRow(new Object[] {
                        reservation.getId(),
                        reservation.getClient().getNom() + " " + reservation.getClient().getPrenom(),
                        reservation.getChamber().getTelephone(),
                        reservation.getDateDebut(),
                        reservation.getDateFin(),
                        reservation.getStatus()
                });
            }
        }
    }
}
