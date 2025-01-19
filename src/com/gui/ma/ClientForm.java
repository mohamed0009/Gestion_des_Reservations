/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.entities.Client;
import com.emsi.entities.User;
import com.emsi.service.ClientService;
import com.emsi.service.UserService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import com.gui.utils.StyleUtils;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.gui.utils.IconUtils;

/**
 *
 * @author HP
 */
public class ClientForm extends javax.swing.JInternalFrame {
    private ClientService clientService;
    private UserService userService;
    private DefaultTableModel model;
    private JButton viewCredentialsButton;
    private javax.swing.JButton addclient;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable listeclient;
    private javax.swing.JTextField mailtxt;
    private javax.swing.JTextField nomtxt;
    private javax.swing.JTextField prenomtxt;
    private javax.swing.JTextField teltxt;

    /**
     * Creates new form ClientForm
     */
    public ClientForm() {
        clientService = new ClientService();
        userService = new UserService();

        // Set basic properties first
        setTitle("Gestion des Clients");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        IconUtils.setInternalFrameIcon(this, IconUtils.CLIENT_ICON);

        // Initialize components
        initComponents();
        applyStyles();

        // Set minimum size
        setMinimumSize(new Dimension(800, 600));

        // Initialize the table model
        model = (DefaultTableModel) listeclient.getModel();
        load();

        // Add row selection listener
        listeclient.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = listeclient.getSelectedRow();
                if (selectedRow != -1) {
                    // Retrieve client details from the selected row
                    int clientId = (int) model.getValueAt(selectedRow, 0); // ID is in the first column
                    Client selectedClient = clientService.findById(clientId);

                    // Populate text fields with client details
                    if (selectedClient != null) {
                        nomtxt.setText(selectedClient.getNom());
                        prenomtxt.setText(selectedClient.getPrenom());
                        teltxt.setText(selectedClient.getTelephone());
                        mailtxt.setText(selectedClient.getEmail());
                    }
                }
            }
        });

        // Pack and set size after all components are initialized
        pack();
        setSize(800, 600);
    }

    private void applyStyles() {
        // Style the panels
        StyleUtils.stylePanel(jPanel1);
        StyleUtils.stylePanel(jPanel2);
        StyleUtils.stylePanel(jPanel3);

        // Set modern panel titles
        Border titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleUtils.PRIMARY_COLOR),
                "Nouveau Client",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                StyleUtils.PRIMARY_COLOR);
        jPanel2.setBorder(BorderFactory.createCompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleUtils.PRIMARY_COLOR),
                "Liste des Clients",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                StyleUtils.PRIMARY_COLOR);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Style text fields with placeholders
        StyleUtils.styleTextField(nomtxt);
        StyleUtils.styleTextField(prenomtxt);
        StyleUtils.styleTextField(teltxt);
        StyleUtils.styleTextField(mailtxt);

        // Style labels
        StyleUtils.styleLabel(jLabel1);
        StyleUtils.styleLabel(jLabel2);
        StyleUtils.styleLabel(jLabel3);
        StyleUtils.styleLabel(jLabel4);

        // Style buttons
        addclient.setText("Ajouter Client");
        jButton2.setText("Modifier Client");
        jButton3.setText("Supprimer Client");

        StyleUtils.styleButton(addclient, StyleUtils.PRIMARY_COLOR);
        StyleUtils.styleButton(jButton2, StyleUtils.SUCCESS_COLOR);
        StyleUtils.styleButton(jButton3, StyleUtils.DANGER_COLOR);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);

        // Add buttons to panel
        buttonPanel.add(addclient);
        buttonPanel.add(jButton2);
        buttonPanel.add(jButton3);

        // Add button panel to form
        jPanel2.add(buttonPanel, BorderLayout.SOUTH);

        // Style table
        StyleUtils.styleTable(listeclient);

        // Add search functionality
        addSearchBar();
    }

    private void addSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(StyleUtils.PANEL_BACKGROUND);

        JTextField searchField = new JTextField(20);
        StyleUtils.styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(200, 30));

        JLabel searchLabel = new JLabel("Rechercher :");
        StyleUtils.styleLabel(searchLabel);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            public void removeUpdate(DocumentEvent e) {
                search();
            }

            public void insertUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = searchField.getText().toLowerCase();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(listeclient.getModel());
                listeclient.setRowSorter(sorter);

                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        jPanel3.add(searchPanel, BorderLayout.NORTH);
    }

    public void load() {
        model.setRowCount(0);
        for (Client client : clientService.findAll()) {
            model.addRow(new Object[] {
                    client.getId(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getTelephone(),
                    client.getEmail(),

            });

        }

    }

    private void nomtxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nomtxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_nomtxtActionPerformed

    private void addclientActionPerformed(java.awt.event.ActionEvent evt) {
        // First validate the input
        if (!validateInput()) {
            return;
        }

        try {
            // Create user credentials
            String username = mailtxt.getText().trim(); // Use email as username
            String password = generateRandomPassword(); // Generate a random password

            // Create User object
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);

            // Register user with client details
            if (userService.register(newUser,
                    nomtxt.getText().trim(),
                    prenomtxt.getText().trim(),
                    teltxt.getText().trim(),
                    mailtxt.getText().trim())) {

                JOptionPane.showMessageDialog(this,
                        "Client ajouté avec succès\nIdentifiants de connexion:\nUsername: " + username + "\nPassword: "
                                + password,
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                load();
                clearFormFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la création du client. Veuillez réessayer.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Une erreur est survenue: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateRandomPassword() {
        // Generate a random 8-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    private void clearFormFields() {
        nomtxt.setText("");
        prenomtxt.setText("");
        teltxt.setText("");
        mailtxt.setText("");
    }

    private void teltxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_teltxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_teltxtActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // Vérifiez si une ligne a été sélectionnée dans le tableau
        int selectedRow = listeclient.getSelectedRow();

        if (selectedRow != -1) {
            // Récupérer l'ID du client à partir de la ligne sélectionnée
            int clientId = (int) model.getValueAt(selectedRow, 0); // Supposons que l'ID est dans la première colonne

            // Rechercher le client dans la base de données (ou le service)
            Client clientToUpdate = clientService.findById(clientId);

            if (clientToUpdate != null) {
                // Mettre à jour les informations du client à partir des champs de texte
                clientToUpdate.setNom(nomtxt.getText());
                clientToUpdate.setPrenom(prenomtxt.getText());
                clientToUpdate.setTelephone(teltxt.getText());
                clientToUpdate.setEmail(mailtxt.getText());
                // Keep the existing user_id
                int userId = clientToUpdate.getUserId();
                clientToUpdate.setUserId(userId);

                // Effectuer l'opération de mise à jour dans la base de données via le service
                boolean isUpdated = clientService.update(clientToUpdate);

                if (isUpdated) {
                    JOptionPane.showMessageDialog(this, "Client mis à jour avec succès");
                    load(); // Recharger la liste des clients pour afficher les modifications
                    clearFormFields(); // Clear the form after successful update
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du client", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Client introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à mettre à jour", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = listeclient.getSelectedRow();

        if (selectedRow != -1) {
            int clientId = (int) model.getValueAt(selectedRow, 0);
            Client clientToDelete = clientService.findById(clientId);

            if (clientToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer ce client ?\n" +
                                "Cette action supprimera également:\n" +
                                "- Toutes les réservations du client\n" +
                                "- Le compte utilisateur associé",
                        "Confirmer la suppression",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (deleteClientWithCascade(clientToDelete)) {
                        JOptionPane.showMessageDialog(this, "Client supprimé avec succès");
                        load();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Erreur lors de la suppression du client",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Client introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        // Check for empty fields
        if (nomtxt.getText().trim().isEmpty() ||
                prenomtxt.getText().trim().isEmpty() ||
                teltxt.getText().trim().isEmpty() ||
                mailtxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate phone number (must be 10 digits)
        String phone = teltxt.getText().trim();
        if (!phone.matches("^0[0-9]{9}$")) {
            JOptionPane.showMessageDialog(this,
                    "Le numéro de téléphone doit commencer par 0 et contenir 10 chiffres",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email format
        String email = mailtxt.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer une adresse email valide",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void updateClientDetails(Client client) {
        client.setNom(nomtxt.getText());
        client.setPrenom(prenomtxt.getText());
        client.setTelephone(teltxt.getText());
        client.setEmail(mailtxt.getText());
    }

    private void updateClientActionPerformed(java.awt.event.ActionEvent evt) {
        // Mise à jour du client sélectionné
        int selectedRow = listeclient.getSelectedRow();

        if (selectedRow != -1) {
            int clientId = (int) model.getValueAt(selectedRow, 0);
            Client clientToUpdate = clientService.findById(clientId);

            if (clientToUpdate != null) {
                clientToUpdate.setNom(nomtxt.getText());
                clientToUpdate.setPrenom(prenomtxt.getText());
                clientToUpdate.setTelephone(teltxt.getText());
                clientToUpdate.setEmail(mailtxt.getText());

                boolean isUpdated = clientService.update(clientToUpdate);

                if (isUpdated) {
                    JOptionPane.showMessageDialog(this, "Client mis à jour avec succès");
                    load();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du client", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Client introuvable", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à mettre à jour", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listeclient = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nomtxt = new javax.swing.JTextField();
        prenomtxt = new javax.swing.JTextField();
        teltxt = new javax.swing.JTextField();
        mailtxt = new javax.swing.JTextField();
        addclient = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion des Clients");
        setPreferredSize(new Dimension(800, 600));

        // Initialize labels
        jLabel1.setText("Nom :");
        jLabel2.setText("Prenom :");
        jLabel3.setText("Telephone :");
        jLabel4.setText("Email :");

        // Initialize buttons
        addclient.setText("Ajouter Client");
        jButton2.setText("Modifier Client");
        jButton3.setText("Supprimer Client");

        // Add action listeners
        addclient.addActionListener(evt -> addclientActionPerformed(evt));
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));

        // Setup table
        listeclient.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Id", "Nom", "Prenom", "Telephone", "Email"
                }));
        jScrollPane1.setViewportView(listeclient);

        // Setup main container with BorderLayout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));

        // Setup form panel (top)
        jPanel2.setBorder(BorderFactory.createTitledBorder("Nouveau Client"));
        jPanel2.setLayout(new BorderLayout(10, 10));

        // Create form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(jLabel1, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomtxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(jLabel2, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(prenomtxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(jLabel3, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(teltxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(jLabel4, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(mailtxt, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addclient);
        buttonPanel.add(jButton2);
        buttonPanel.add(jButton3);

        // Add panels to form panel
        jPanel2.add(formPanel, BorderLayout.CENTER);
        jPanel2.add(buttonPanel, BorderLayout.SOUTH);

        // Setup table panel (bottom)
        jPanel3.setBorder(BorderFactory.createTitledBorder("Liste des Clients"));
        jPanel3.setLayout(new BorderLayout(10, 10));
        jPanel3.add(jScrollPane1, BorderLayout.CENTER);

        // Add panels to main container
        contentPane.add(jPanel2, BorderLayout.NORTH);
        contentPane.add(jPanel3, BorderLayout.CENTER);

        pack();
    }

    private void viewClientCredentials() {
        int selectedRow = listeclient.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un client",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int clientId = (int) model.getValueAt(selectedRow, 0);
            Client selectedClient = clientService.findById(clientId);

            if (selectedClient != null) {
                int userId = selectedClient.getUserId();
                User user = userService.findById(userId);

                if (user != null) {
                    String message = String.format(
                            "Identifiants du client:\n\n" +
                                    "Nom: %s %s\n" +
                                    "Username: %s\n" +
                                    "Email: %s\n",
                            selectedClient.getNom(),
                            selectedClient.getPrenom(),
                            user.getUsername(),
                            selectedClient.getEmail());

                    JOptionPane.showMessageDialog(this,
                            message,
                            "Informations d'identification",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Utilisateur non trouvé",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la récupération des identifiants: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAllClients() {
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer tous les clients ?\n" +
                        "Cette action supprimera également:\n" +
                        "- Toutes les réservations des clients\n" +
                        "- Tous les comptes utilisateurs associés\n" +
                        "Cette action est irréversible!",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Get all clients
                List<Client> clients = clientService.findAll();
                int totalClients = clients.size();
                int deletedCount = 0;

                // Delete each client and their associated data
                for (Client client : clients) {
                    if (deleteClientWithCascade(client)) {
                        deletedCount++;
                    }
                }

                // Show result message
                if (deletedCount == totalClients) {
                    JOptionPane.showMessageDialog(this,
                            String.format("%d clients et leurs données associées ont été supprimés avec succès.",
                                    deletedCount),
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            String.format("%d sur %d clients ont été supprimés.\nCertaines suppressions ont échoué.",
                                    deletedCount, totalClients),
                            "Suppression partielle",
                            JOptionPane.WARNING_MESSAGE);
                }

                // Refresh the table
                load();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Une erreur est survenue lors de la suppression: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean deleteClientWithCascade(Client client) {
        try {
            // 1. Delete all reservations for this client
            String deleteReservationsSQL = "DELETE FROM reservation WHERE client_id = ?";
            try (Connection conn = ma.emdi.connection.ConnectionJdbc.getCnx();
                    PreparedStatement stmt = conn.prepareStatement(deleteReservationsSQL)) {
                stmt.setInt(1, client.getId());
                stmt.executeUpdate();
            }

            // 2. Delete the client
            if (clientService.delete(client)) {
                // 3. Delete the associated user
                User user = userService.findById(client.getUserId());
                if (user != null) {
                    userService.delete(user);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
