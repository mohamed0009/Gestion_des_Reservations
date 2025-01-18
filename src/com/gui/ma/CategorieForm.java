/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gui.ma;

import com.emsi.entities.Categorie;
import com.emsi.service.CategorieService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import javax.swing.JButton;
import com.gui.utils.StyleUtils;

/**
 *
 * @author HP
 */
public class CategorieForm extends javax.swing.JInternalFrame {
    private CategorieService categorieService;
    private DefaultTableModel model;
    private JTextField searchField;

    /**
     * Creates new form ClientForm
     */
    public CategorieForm() {
        categorieService = new CategorieService();
        initComponents();
        applyStyles();
        this.setTitle("Gestion des Catégories");
        model = (DefaultTableModel) listeclient.getModel();
        load();
        addFieldValidation(codetxt);
        addFieldValidation(libelletxt);
        addSearchBar();
        addButtonHoverEffect(addclient);
        addButtonHoverEffect(jButton2);
        addButtonHoverEffect(jButton3);
    }

    public void load() {
        model.setRowCount(0);
        for (Categorie categorie : categorieService.findAll()) {
            model.addRow(new Object[] {
                    categorie.getId(),
                    categorie.getLibelle(),
                    categorie.getDescription(),
                    categorie.getPrix()

            });

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        codetxt = new javax.swing.JTextField();
        libelletxt = new javax.swing.JTextField();
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
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null }
                },
                new String[] {
                        "Id", "Code", "Libelle"
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

        jLabel1.setText("Code :");

        jLabel2.setText("Libelle :");

        codetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codetxtActionPerformed(evt);
            }
        });

        addclient.setText("Add Categorie");
        addclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addclientActionPerformed(evt);
            }
        });

        jButton2.setText("Update Categorie");

        jButton3.setText("Remove Categorie");
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
                                                        .addComponent(jLabel2))
                                                .addGap(72, 72, 72)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(codetxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(libelletxt,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 321,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                        .addComponent(codetxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(libelletxt, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69,
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

    private void codetxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_codetxtActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_codetxtActionPerformed

    private void addclientActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addclientActionPerformed
        // TODO add your handling code here:
        if (!validateFields())
            return;
        if (categorieService.create(new Categorie(libelletxt.getText(), codetxt.getText(), 0.0))) {
            showSuccessMessage("Catégorie ajoutée avec succès");
            load();
        }
    }// GEN-LAST:event_addclientActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton3ActionPerformed

    private boolean validateFields() {
        if (codetxt.getText().trim().isEmpty() || libelletxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addFieldValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                validate();
            }

            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            private void validate() {
                if (field.getText().trim().isEmpty()) {
                    field.setBorder(BorderFactory.createLineBorder(Color.RED));
                } else {
                    field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            }
        });
    }

    private void showSuccessMessage(String message) {
        JPanel panel = new JPanel();
        panel.setBackground(StyleUtils.SUCCESS_COLOR);
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label);

        Timer timer = new Timer(2000, e -> panel.setVisible(false));
        timer.setRepeats(false);
        timer.start();

        // Ajouter le panel en haut du formulaire
        add(panel, BorderLayout.NORTH);
        panel.setVisible(true);
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }
        });
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

        // Ajouter le panel de recherche au-dessus de la table
        jPanel3.add(searchPanel, BorderLayout.NORTH);
    }

    private void search() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(listeclient.getModel());
        listeclient.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void applyStyles() {
        StyleUtils.stylePanel(jPanel1);
        StyleUtils.stylePanel(jPanel2);
        StyleUtils.stylePanel(jPanel3);

        StyleUtils.styleButton(addclient, StyleUtils.PRIMARY_COLOR);
        StyleUtils.styleButton(jButton2, StyleUtils.SUCCESS_COLOR);
        StyleUtils.styleButton(jButton3, StyleUtils.DANGER_COLOR);

        StyleUtils.styleTextField(codetxt);
        StyleUtils.styleTextField(libelletxt);

        StyleUtils.styleLabel(jLabel1);
        StyleUtils.styleLabel(jLabel2);

        StyleUtils.styleTable(listeclient);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addclient;
    private javax.swing.JTextField codetxt;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField libelletxt;
    private javax.swing.JTable listeclient;
    // End of variables declaration//GEN-END:variables
}
