/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.entities.Client;
import com.emsi.service.ClientService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import com.gui.utils.StyleUtils;

/**
 *
 * @author HP
 */
public class ClientForm extends javax.swing.JInternalFrame {
    private ClientService clientService;
    private DefaultTableModel model;

    /**
     * Creates new form ClientForm
     */
    public ClientForm() {
        clientService = new ClientService();
        initComponents();
        applyStyles();
        this.setTitle("Gestion des Clients");
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

        // Style buttons with icons and colors
        addclient.setText("Ajouter Client");
        jButton2.setText("Modifier Client");
        jButton3.setText("Supprimer Client");

        StyleUtils.styleButton(addclient, StyleUtils.PRIMARY_COLOR);
        StyleUtils.styleButton(jButton2, StyleUtils.SUCCESS_COLOR);
        StyleUtils.styleButton(jButton3, StyleUtils.DANGER_COLOR);

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listeclient = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
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

        jPanel3.setBackground(new java.awt.Color(153, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Table Clients"));

        listeclient.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        listeclient.setForeground(new java.awt.Color(51, 51, 51));
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(27, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(216, 216, 216)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)));

        jPanel2.setBackground(new java.awt.Color(153, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("New Client"));

        jLabel1.setText("Nom :");

        jLabel2.setText("Prenom :");

        jLabel3.setText("Telephone :");

        jLabel4.setText("Email :");

        nomtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomtxtActionPerformed(evt);
            }
        });

        teltxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teltxtActionPerformed(evt);
            }
        });

        addclient.setText("Add Client");
        addclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addclientActionPerformed(evt);
            }
        });

        jButton2.setText("Update Client");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Remove Client");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(nomtxt)
                                                        .addComponent(prenomtxt)
                                                        .addComponent(teltxt)
                                                        .addComponent(mailtxt, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                321, Short.MAX_VALUE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(195, 195, 195)
                                                .addComponent(addclient)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3)))
                                .addContainerGap(196, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(nomtxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(prenomtxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(teltxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(mailtxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28,
                                        Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addclient)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addGap(24, 24, 24)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 9, Short.MAX_VALUE)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 452,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nomtxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nomtxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_nomtxtActionPerformed

    private void addclientActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addclientActionPerformed
        if (clientService
                .create(new Client(nomtxt.getText(), prenomtxt.getText(), teltxt.getText(), mailtxt.getText()))) {
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès");
            load();
            clearFormFields(); // Clear the form after adding the client
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

    }// GEN-LAST:event_addclientActionPerformed

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

                // Effectuer l'opération de mise à jour dans la base de données via le service
                boolean isUpdated = clientService.update(clientToUpdate);

                if (isUpdated) {
                    JOptionPane.showMessageDialog(this, "Client mis à jour avec succès");
                    load(); // Recharger la liste des clients pour afficher les modifications
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        int selectedRow = listeclient.getSelectedRow();

        if (selectedRow != -1) {
            // Get the client ID from the selected row
            int clientId = (int) model.getValueAt(selectedRow, 0);

            // Find the client by ID
            Client clientToDelete = clientService.findById(clientId);

            if (clientToDelete != null) {
                // Confirm deletion from the user
                int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce client ?",
                        "Confirmer", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Call the delete method in ClientService
                    boolean isDeleted = clientService.delete(clientToDelete);

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(this, "Client supprimé avec succès");
                        load(); // Refresh the client list
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du client", "Erreur",
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
    }// GEN-LAST:event_jButton3ActionPerformed

    private boolean validateInput() {
        if (nomtxt.getText().trim().isEmpty() || prenomtxt.getText().trim().isEmpty() ||
                teltxt.getText().trim().isEmpty() || mailtxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Basic email format check
        String email = mailtxt.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une adresse email valide", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Basic phone number format check (simple check for numbers only)
        String phone = teltxt.getText().trim();
        if (!phone.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro de téléphone valide (10 chiffres)", "Erreur",
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
