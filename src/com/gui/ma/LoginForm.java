package com.gui.ma;

import com.emsi.entities.User;
import com.emsi.service.UserService;
import com.gui.utils.StyleUtils;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public LoginForm() {
        userService = new UserService();
        initComponents();
        applyStyles();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main panel with two sections
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left Panel - Login Form
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(39, 89, 45));
        leftPanel.add(welcomeLabel, gbc);

        JLabel subtitleLabel = new JLabel("Please enter your credentials to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(128, 128, 128));
        leftPanel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(30, 0, 5, 0);

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

        // Message label for errors
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(StyleUtils.DANGER_COLOR);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(messageLabel, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(350, 45));
        StyleUtils.styleButton(loginButton, new Color(190, 210, 50));
        loginButton.addActionListener(e -> login());
        leftPanel.add(loginButton, gbc);

        // Register link panel
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setBackground(Color.WHITE);
        JLabel registerLabel = new JLabel("Don't have an account? ");
        JButton registerButton = new JButton("Register");
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(new Color(190, 210, 50));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> showRegisterForm());

        registerPanel.add(registerLabel);
        registerPanel.add(registerButton);
        leftPanel.add(registerPanel, gbc);

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
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    private void showRegisterForm() {
        // Create and show registration form
        RegisterForm registerForm = new RegisterForm();
        registerForm.setVisible(true);
        this.dispose();
    }

    private void applyStyles() {
        getRootPane().setDefaultButton(loginButton);
        passwordField.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password");
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                Main mainForm = new Main(user);
                mainForm.setVisible(true);
            });
        } else {
            messageLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}