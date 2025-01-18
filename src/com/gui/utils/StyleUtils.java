package com.gui.utils;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;

public class StyleUtils {
    // Colors
    public static final Color BACKGROUND_COLOR = new Color(247, 248, 252);
    public static final Color BORDER_COLOR = new Color(203, 213, 225);
    public static final Color TEXT_COLOR = new Color(71, 85, 105);
    public static final Color PRIMARY_COLOR = new Color(79, 70, 229); // Modern indigo
    public static final Color SECONDARY_COLOR = new Color(100, 116, 139); // Modern slate
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94); // Modern green
    public static final Color DANGER_COLOR = new Color(239, 68, 68); // Modern red
    public static final Color PANEL_BACKGROUND = Color.WHITE;

    // Fonts
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void styleButton(JButton button, Color color) {
        if (button == null)
            return;
        button.setFont(BOLD_FONT);
        button.setForeground(color);
        button.setBackground(PANEL_BACKGROUND);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        textField.setBackground(Color.WHITE);
    }

    public static void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
    }

    public static void styleMenuItem(JMenuItem menuItem) {
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setBackground(PANEL_BACKGROUND);
        menuItem.setForeground(TEXT_COLOR);
        menuItem.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(PRIMARY_COLOR);
                menuItem.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(PANEL_BACKGROUND);
                menuItem.setForeground(TEXT_COLOR);
            }
        });
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    public static void styleDateChooser(JDateChooser dateChooser) {
        if (dateChooser == null)
            return;

        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setBackground(Color.WHITE);
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }
}