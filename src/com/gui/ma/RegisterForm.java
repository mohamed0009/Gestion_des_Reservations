package com.gui.ma;

import com.emsi.entities.User;
import com.emsi.service.UserService;
import com.gui.utils.StyleUtils;
import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {
    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JLabel messageLabel;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField telephoneField;
    private JTextField emailField;

    public RegisterForm() {
        userService = new UserService();
        initComponents();
        applyStyles();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main panel with two sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left Panel - Registration Form
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Create a scroll pane for the left panel
        JScrollPane scrollPane = new JScrollPane(leftPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Create Account");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(39, 89, 45));
        leftPanel.add(welcomeLabel, gbc);

        JLabel subtitleLabel = new JLabel("Please fill in the form to create your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(128, 128, 128));
        leftPanel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(20, 0, 5, 0);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(usernameField);
        leftPanel.add(usernameField, gbc);

        gbc.insets = new Insets(15, 0, 5, 0);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(passwordField);
        leftPanel.add(passwordField, gbc);

        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(confirmPasswordField);
        leftPanel.add(confirmPasswordField, gbc);

        // Client Information Fields
        JLabel nomLabel = new JLabel("First Name");
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(nomLabel, gbc);

        nomField = new JTextField();
        nomField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(nomField);
        leftPanel.add(nomField, gbc);

        JLabel prenomLabel = new JLabel("Last Name");
        prenomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(prenomLabel, gbc);

        prenomField = new JTextField();
        prenomField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(prenomField);
        leftPanel.add(prenomField, gbc);

        JLabel telephoneLabel = new JLabel("Phone Number");
        telephoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(telephoneLabel, gbc);

        telephoneField = new JTextField();
        telephoneField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(telephoneField);
        leftPanel.add(telephoneField, gbc);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        leftPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(350, 40));
        StyleUtils.styleTextField(emailField);
        leftPanel.add(emailField, gbc);

        // Message label for errors/success
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(StyleUtils.DANGER_COLOR);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(messageLabel, gbc);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(350, 45));
        StyleUtils.styleButton(registerButton, new Color(190, 210, 50));
        registerButton.addActionListener(e -> register());
        leftPanel.add(registerButton, gbc);

        // Login link panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(Color.WHITE);
        JLabel loginLabel = new JLabel("Already have an account? ");
        JButton loginButton = new JButton("Login");
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(new Color(190, 210, 50));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> showLoginForm());

        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        leftPanel.add(loginPanel, gbc);

        // Right Panel - Hotel Image
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a gradient background
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(39, 89, 45);
                Color color2 = new Color(190, 210, 50);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Add panels to main panel
        mainPanel.add(scrollPane);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    private void applyStyles() {
        getRootPane().setDefaultButton(registerButton);
        confirmPasswordField.addActionListener(e -> register());
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();

        // Validate all fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        if (userService.register(user, nom, prenom, telephone, email)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new LoginForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginForm() {
        new LoginForm().setVisible(true);
        this.dispose();
    }
}