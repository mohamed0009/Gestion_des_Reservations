/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.entities.Chamber;
import com.emsi.service.ChamberService;
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
public class ChamberForm extends javax.swing.JInternalFrame {
    private ChamberService chamberService;
    private DefaultTableModel model;
    private JTextField searchField;

    /**
     * Creates new form ClientForm
     */
    public ChamberForm() {
        chamberService = new ChamberService();
        initComponents();
        applyStyles();
        this.setTitle("Gestion des Chambres");
        model = (DefaultTableModel) listeclient.getModel();
        load();

        // Add row selection listener
        listeclient.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = listeclient.getSelectedRow();
                if (selectedRow != -1) {
                    int chamberId = (int) model.getValueAt(selectedRow, 0);
                    Chamber selectedChamber = chamberService.findById(chamberId);
                    if (selectedChamber != null) {
                        teltxt.setText(selectedChamber.getTelephone());
                        categorietxt.setText(String.valueOf(selectedChamber.getCategorieId()));
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
                "Nouvelle Chambre",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                StyleUtils.PRIMARY_COLOR);
        jPanel2.setBorder(BorderFactory.createCompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleUtils.PRIMARY_COLOR),
                "Liste des Chambres",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                StyleUtils.PRIMARY_COLOR);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Style text fields
        StyleUtils.styleTextField(teltxt);
        StyleUtils.styleTextField(categorietxt);

        // Style labels
        StyleUtils.styleLabel(jLabel3);
        StyleUtils.styleLabel(jLabel4);

        // Style buttons
        addclient.setText("Ajouter Chambre");
        jButton2.setText("Modifier Chambre");
        jButton3.setText("Supprimer Chambre");

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

        searchField = new JTextField(20);
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
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        jPanel3.add(searchPanel, BorderLayout.NORTH);
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

    private boolean validateFields() {
        if (teltxt.getText().trim().isEmpty() || categorietxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate phone number format
        if (!teltxt.getText().matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "Le numéro de téléphone doit contenir 10 chiffres",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate category ID is a number
        try {
            Integer.parseInt(categorietxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La catégorie doit être un nombre",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        teltxt.setText("");
        categorietxt.setText("");
    }

    public void load() {
        model.setRowCount(0);
        for (Chamber chamber : chamberService.findAll()) {
            model.addRow(new Object[] {
                    chamber.getId(),
                    chamber.getTelephone(),
                    chamber.getCategorieId(),
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        teltxt = new javax.swing.JTextField();
        categorietxt = new javax.swing.JTextField();
        addclient = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel3.setBackground(new java.awt.Color(153, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Table Chambers"));

        listeclient.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        listeclient.setForeground(new java.awt.Color(51, 51, 51));
        listeclient.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null }
                },
                new String[] {
                        "Id", "Telephone", "categorie_id"
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
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("New Chamber"));

        jLabel3.setText("Telephone :");

        jLabel4.setText("Categorie :");

        teltxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teltxtActionPerformed(evt);
            }
        });

        categorietxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        categorietxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorietxtActionPerformed(evt);
            }
        });

        addclient.setText("Add Chamber");
        addclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addclientActionPerformed(evt);
            }
        });

        jButton2.setText("Update Chamber");

        jButton3.setText("Remove Chamber");

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
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(teltxt)
                                                        .addComponent(categorietxt,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 321,
                                                                Short.MAX_VALUE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(168, 168, 168)
                                                .addComponent(addclient)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton3)))
                                .addContainerGap(217, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(teltxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(categorietxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57,
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
                                                .addGap(0, 0, Short.MAX_VALUE)))
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

    private void addclientActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addclientActionPerformed
        // TODO add your handling code here:
        try {
            // Convert the category text to an integer
            int categorie = Integer.parseInt(categorietxt.getText());

            // Create a new Chamber object and attempt to save it
            boolean isCreated = chamberService.create(new Chamber(teltxt.getText(), categorie));

            if (isCreated) {
                JOptionPane.showMessageDialog(this, "Chamber ajouté avec succès");
                load(); // Reload data after successful creation
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la chambre", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Handle invalid number input
            JOptionPane.showMessageDialog(this, "Veuillez saisir un nombre valide pour la catégorie",
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle other exceptions
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite: " + e.getMessage(), "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

    }// GEN-LAST:event_addclientActionPerformed

    private void teltxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_teltxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_teltxtActionPerformed

    private void categorietxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_categorietxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_categorietxtActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addclient;
    private javax.swing.JTextField categorietxt;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable listeclient;
    private javax.swing.JTextField teltxt;
    // End of variables declaration//GEN-END:variables
}
