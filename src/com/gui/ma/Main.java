/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/MDIApplication.java to edit this template
 */
package com.gui.ma;

import com.gui.utils.StyleUtils;
import com.gui.utils.IconUtils;
import com.emsi.entities.User;
import com.emsi.entities.Client;
import com.emsi.service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import javax.swing.Box;
import java.awt.image.BufferedImage;

/**
 *
 * @author HP
 */
public class Main extends javax.swing.JFrame {

    private User currentUser;

    /**
     * Creates new form Main
     */
    public Main(User user) {
        this.currentUser = user;
        try {
            // Utiliser un Look and Feel moderne
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            customizeUIDefaults();
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
            setMinimumSize(new Dimension(1200, 800));
            IconUtils.setFrameIcon(this, IconUtils.APP_ICON);
        } catch (Exception e) {
            // Silently fail if icon cannot be loaded
            System.out.println("Could not load application icon");
        }

        initComponents();
        this.setTitle("LuxeStay Manager");
        this.setLocationRelativeTo(null);
        setupMenuAccess();
        setupIcons();

        // Create a gradient background for desktopPane
        desktopPane.setBackground(StyleUtils.BACKGROUND_COLOR);

        // Add appropriate dashboard
        if ("admin".equals(currentUser.getRole())) {
            AdminDashboard dashboard = new AdminDashboard();
            desktopPane.add(dashboard);
            dashboard.setSize(desktopPane.getWidth(), desktopPane.getHeight());
            dashboard.setLocation(0, 0);
            dashboard.setVisible(true);
        } else {
            // Get Client object for the current user
            ClientService clientService = new ClientService();
            Client client = clientService.findByUserId(currentUser.getId());
            if (client == null) {
                JOptionPane.showMessageDialog(this,
                        "Error: No client profile found for this user.",
                        "Profile Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            ClientDashboard dashboard = new ClientDashboard(client);
            desktopPane.add(dashboard);
            dashboard.setSize(desktopPane.getWidth(), desktopPane.getHeight());
            dashboard.setLocation(0, 0);
            dashboard.setVisible(true);
        }

        // Personnaliser le menu
        menuBar.setBackground(StyleUtils.PRIMARY_COLOR);
        menuBar.setForeground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Update menu items text and style
        fileMenu.setText("Management");
        openMenuItem.setText("Clients Management");
        saveMenuItem.setText("Rooms Management");
        saveAsMenuItem.setText("Reservations");
        exitMenuItem.setText("Categories");

        editMenu.setText("Reports");
        helpMenu.setText("Settings");

        styleMenu(fileMenu);
        styleMenu(editMenu);
        styleMenu(helpMenu);

        // Style all menu items
        styleMenuItem(openMenuItem);
        styleMenuItem(saveMenuItem);
        styleMenuItem(saveAsMenuItem);
        styleMenuItem(exitMenuItem);

        // Update search menu items
        cutMenuItem.setText("Daily Report");
        copyMenuItem.setText("Monthly Report");
        pasteMenuItem.setText("Annual Report");
        deleteMenuItem.setText("Custom Report");

        // Update help menu items
        contentMenuItem.setText("System Settings");
        aboutMenuItem.setText("About System");

        // Style these menu items too
        styleMenuItem(cutMenuItem);
        styleMenuItem(copyMenuItem);
        styleMenuItem(pasteMenuItem);
        styleMenuItem(deleteMenuItem);
        styleMenuItem(contentMenuItem);
        styleMenuItem(aboutMenuItem);

        // Handle permissions based on user role
        if (!"admin".equals(currentUser.getRole())) {
            // Disable admin-only features
            editMenu.setVisible(false);
            helpMenu.setVisible(false);
        }
    }

    private void setupIcons() {
        // Set menu icons
        fileMenu.setIcon(IconUtils.resizeIcon(IconUtils.DASHBOARD_ICON, 20, 20));
        editMenu.setIcon(IconUtils.resizeIcon(IconUtils.STATS_ICON, 20, 20));

        // Set menu item icons
        openMenuItem.setIcon(IconUtils.resizeIcon(IconUtils.CLIENT_ICON, 16, 16));
        saveMenuItem.setIcon(IconUtils.resizeIcon(IconUtils.ROOM_ICON, 16, 16));
        saveAsMenuItem.setIcon(IconUtils.resizeIcon(IconUtils.RESERVATION_ICON, 16, 16));
        exitMenuItem.setIcon(IconUtils.resizeIcon(IconUtils.CATEGORY_ICON, 16, 16));
    }

    private void styleMenu(JMenu menu) {
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void styleMenuItem(JMenuItem menuItem) {
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setBackground(StyleUtils.PANEL_BACKGROUND);
        menuItem.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        menuItem.setOpaque(true);
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

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        desktopPane.setBackground(new java.awt.Color(0, 0, 0));

        menuBar.setAutoscrolls(true);
        menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        fileMenu.setMnemonic('f');
        fileMenu.setText("Gestion");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Clients");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Chamber");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Reservation");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Categorie");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Rechrche");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentMenuItem.setMnemonic('c');
        contentMenuItem.setText("Contents");
        helpMenu.add(contentMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Make desktopPane fill the entire frame
        desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        // Use BorderLayout to make desktopPane fill the entire frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
        CategorieForm ca = new CategorieForm();
        desktopPane.add(ca);
        ca.setVisible(true);

    }// GEN-LAST:event_exitMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed

        ChamberForm ch = new ChamberForm();
        desktopPane.add(ch);
        ch.setVisible(true);

        // TODO add your handling code here:
    }// GEN-LAST:event_saveMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if ("admin".equals(currentUser.getRole())) {
            ClientForm cf = new ClientForm();
            // Set size and center the form
            cf.setSize(800, 600);
            // Center the form in the desktop pane
            Dimension desktopSize = desktopPane.getSize();
            Dimension frameSize = cf.getSize();
            cf.setLocation((desktopSize.width - frameSize.width) / 2,
                    (desktopSize.height - frameSize.height) / 2);
            desktopPane.add(cf);
            cf.setVisible(true);
            try {
                cf.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
            }
        } else {
            ClientProfileForm cpf = new ClientProfileForm(currentUser);
            desktopPane.add(cpf);
            cpf.setVisible(true);
        }
    }

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAsMenuItemActionPerformed
        // TODO add your handling code here:
        ReservationForm cr = new ReservationForm();
        desktopPane.add(cr);
        cr.setVisible(true);
    }// GEN-LAST:event_saveAsMenuItemActionPerformed

    /*
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Start with the login form
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    // End of variables declaration//GEN-END:variables

    private void customizeUIDefaults() {
        UIManager.put("Panel.background", StyleUtils.PANEL_BACKGROUND);
        UIManager.put("Button.background", StyleUtils.PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("InternalFrame.titleFont", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("InternalFrame.borderColor", new Color(229, 231, 235));
        UIManager.put("TitleBar.background", StyleUtils.PRIMARY_COLOR);
        UIManager.put("TitleBar.foreground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", StyleUtils.PRIMARY_COLOR);
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);
        UIManager.put("Menu.selectionBackground", StyleUtils.PRIMARY_COLOR.darker());
        UIManager.put("MenuBar.borderColor", StyleUtils.PRIMARY_COLOR);
    }

    private void setupMenuAccess() {
        boolean isAdmin = "admin".equals(currentUser.getRole());

        // Add logout menu item
        JMenu userMenu = new JMenu(currentUser.getUsername());
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setText("⇥ Logout");
        logoutItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutItem.addActionListener(e -> logout());
        userMenu.add(logoutItem);

        // Style the user menu
        styleMenu(userMenu);
        styleMenuItem(logoutItem);
        userMenu.setIcon(new ImageIcon(new BufferedImage(1, 16, BufferedImage.TYPE_INT_ARGB))); // Empty icon for
                                                                                                // spacing
        menuBar.add(Box.createHorizontalGlue()); // Push user menu to right
        menuBar.add(userMenu);

        // Management menu items
        saveMenuItem.setVisible(isAdmin);
        exitMenuItem.setVisible(isAdmin);

        // Reports menu
        editMenu.setVisible(isAdmin);

        // Settings menu
        helpMenu.setVisible(isAdmin);

        // Client can only see:
        // - Their profile (openMenuItem - Client Profile)
        // - Make reservations (saveAsMenuItem - Reservations)
        openMenuItem.setText(isAdmin ? "Clients Management" : "Client Profile");
        saveAsMenuItem.setText(isAdmin ? "Reservations" : "Book a Room");
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }

}
