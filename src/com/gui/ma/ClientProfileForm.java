package com.gui.ma;

import com.emsi.entities.User;
import com.emsi.entities.Client;
import com.emsi.service.ClientService;
import com.gui.utils.StyleUtils;
import javax.swing.*;
import java.awt.*;

public class ClientProfileForm extends javax.swing.JInternalFrame {
    private User currentUser;
    private ClientService clientService;
    private Client clientData;
    private JPanel mainPanel;
    private JTextField nomtxt, prenomtxt, teltxt, mailtxt;
    private JLabel nameLabel, surnameLabel, phoneLabel, emailLabel;
    private JButton updateButton;
    private JLabel statusLabel;

    public ClientProfileForm(User user) {
        this.currentUser = user;
        this.clientService = new ClientService();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("My Profile");

        initComponents();
        loadClientData();
        applyStyles();
        pack();
        setSize(600, 500);
    }

    private void initComponents() {
        // Create main panel with padding
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Create and add form fields
        nameLabel = new JLabel("First Name:");
        nomtxt = new JTextField(20);

        surnameLabel = new JLabel("Last Name:");
        prenomtxt = new JTextField(20);

        phoneLabel = new JLabel("Phone Number:");
        teltxt = new JTextField(20);

        emailLabel = new JLabel("Email Address:");
        mailtxt = new JTextField(20);

        // Add components to form panel
        addFormRow(formPanel, nameLabel, nomtxt, gbc, 0);
        addFormRow(formPanel, surnameLabel, prenomtxt, gbc, 1);
        addFormRow(formPanel, phoneLabel, teltxt, gbc, 2);
        addFormRow(formPanel, emailLabel, mailtxt, gbc, 3);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Status label for feedback
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(StyleUtils.DANGER_COLOR);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateButton = new JButton("Update Profile");
        updateButton.addActionListener(e -> updateProfile());
        buttonPanel.add(updateButton);
        mainPanel.add(buttonPanel);

        // Add main panel to frame
        add(mainPanel);
    }

    private void addFormRow(JPanel panel, JLabel label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
    }

    private void loadClientData() {
        clientData = clientService.findByUserId(currentUser.getId());
        if (clientData != null) {
            nomtxt.setText(clientData.getNom());
            prenomtxt.setText(clientData.getPrenom());
            teltxt.setText(clientData.getTelephone());
            mailtxt.setText(clientData.getEmail());
        } else {
            statusLabel.setText("Error: Could not load client data");
        }
    }

    private void applyStyles() {
        // Style the main panel
        mainPanel.setBackground(Color.WHITE);

        // Style text fields
        StyleUtils.styleTextField(nomtxt);
        StyleUtils.styleTextField(prenomtxt);
        StyleUtils.styleTextField(teltxt);
        StyleUtils.styleTextField(mailtxt);

        // Style labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        nameLabel.setFont(labelFont);
        surnameLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);

        // Style update button
        StyleUtils.styleButton(updateButton, StyleUtils.PRIMARY_COLOR);
    }

    private void updateProfile() {
        if (validateFields()) {
            clientData.setNom(nomtxt.getText().trim());
            clientData.setPrenom(prenomtxt.getText().trim());
            clientData.setTelephone(teltxt.getText().trim());
            clientData.setEmail(mailtxt.getText().trim());

            if (clientService.update(clientData)) {
                statusLabel.setForeground(new Color(40, 167, 69));
                statusLabel.setText("Profile updated successfully!");
            } else {
                statusLabel.setForeground(StyleUtils.DANGER_COLOR);
                statusLabel.setText("Failed to update profile");
            }
        }
    }

    private boolean validateFields() {
        if (nomtxt.getText().trim().isEmpty() ||
                prenomtxt.getText().trim().isEmpty() ||
                teltxt.getText().trim().isEmpty() ||
                mailtxt.getText().trim().isEmpty()) {

            statusLabel.setText("All fields are required");
            return false;
        }

        // Validate email format
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!mailtxt.getText().matches(emailPattern)) {
            statusLabel.setText("Please enter a valid email address");
            return false;
        }

        // Validate phone number (assuming it should be numeric and have a reasonable
        // length)
        String phoneNumber = teltxt.getText().trim();
        if (!phoneNumber.matches("\\d{10}")) {
            statusLabel.setText("Please enter a valid 10-digit phone number");
            return false;
        }

        return true;
    }
}