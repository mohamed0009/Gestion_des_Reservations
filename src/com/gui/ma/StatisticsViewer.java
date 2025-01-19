package com.gui.ma;

import com.emsi.entities.Client;
import com.emsi.service.ReservationService;
import com.emsi.utils.PDFExporter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Map;

public class StatisticsViewer extends javax.swing.JInternalFrame {
    private final Client currentClient;
    private final ReservationService reservationService;
    private ChartPanel categoryChartPanel;
    private ChartPanel timeChartPanel;

    public StatisticsViewer(Client client) {
        super("Statistics & Reports", true, true, true, true);
        this.currentClient = client;
        this.reservationService = new ReservationService();
        initComponents();
        customizeComponents();
        populateYearComboBox();
        createCharts();
    }

    private void customizeComponents() {
        // Set modern look for controls
        yearComboBox.setBackground(Color.WHITE);
        exportButton.setBackground(new Color(79, 70, 229));
        exportButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(79, 70, 229));
        refreshButton.setForeground(Color.WHITE);

        // Add borders and padding
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void populateYearComboBox() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear - 5; year <= currentYear + 1; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }
        yearComboBox.setSelectedItem(String.valueOf(currentYear));
    }

    private void createCharts() {
        int selectedYear = Integer.parseInt(yearComboBox.getSelectedItem().toString());

        // Create category chart
        Map<String, Integer> categoryStats = reservationService.getReservationsByCategory(currentClient.getId(),
                selectedYear);
        DefaultPieDataset categoryDataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
            categoryDataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart categoryChart = ChartFactory.createPieChart(
                "Room Reservations by Category",
                categoryDataset,
                true,
                true,
                false);

        if (categoryChartPanel != null) {
            chartsPanel.remove(categoryChartPanel);
        }
        categoryChartPanel = new ChartPanel(categoryChart);
        categoryChartPanel.setPreferredSize(new Dimension(400, 300));

        // Create time series chart
        Map<String, Integer> timeStats = reservationService.getReservationsOverTime(currentClient.getId(),
                selectedYear);
        DefaultCategoryDataset timeDataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : timeStats.entrySet()) {
            timeDataset.addValue(entry.getValue(), "Reservations", entry.getKey());
        }

        JFreeChart timeChart = ChartFactory.createLineChart(
                "Reservations Over Time",
                "Month",
                "Number of Reservations",
                timeDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        if (timeChartPanel != null) {
            chartsPanel.remove(timeChartPanel);
        }
        timeChartPanel = new ChartPanel(timeChart);
        timeChartPanel.setPreferredSize(new Dimension(400, 300));

        // Add charts to panel
        chartsPanel.removeAll();
        chartsPanel.add(categoryChartPanel);
        chartsPanel.add(timeChartPanel);
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private void yearComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        createCharts();
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createCharts();
    }

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {
        PDFExporter exporter = new PDFExporter();
        String year = yearComboBox.getSelectedItem().toString();
        exporter.exportStatistics(currentClient, year, categoryChartPanel, timeChartPanel);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        controlPanel = new javax.swing.JPanel();
        yearLabel = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox<>();
        exportButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        chartsPanel = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Statistics & Reports");

        controlPanel.setBackground(new java.awt.Color(255, 255, 255));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        yearLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        yearLabel.setText("Select Year:");
        controlPanel.add(yearLabel);

        yearComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14));
        yearComboBox.setPreferredSize(new java.awt.Dimension(100, 30));
        yearComboBox.addActionListener(evt -> yearComboBoxActionPerformed(evt));
        controlPanel.add(yearComboBox);

        exportButton.setFont(new java.awt.Font("Segoe UI", 0, 14));
        exportButton.setText("Export to PDF");
        exportButton.addActionListener(evt -> exportButtonActionPerformed(evt));
        controlPanel.add(exportButton);

        refreshButton.setFont(new java.awt.Font("Segoe UI", 0, 14));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(evt -> refreshButtonActionPerformed(evt));
        controlPanel.add(refreshButton);

        getContentPane().add(controlPanel, java.awt.BorderLayout.NORTH);

        chartsPanel.setBackground(new java.awt.Color(255, 255, 255));
        chartsPanel.setLayout(new java.awt.GridLayout(1, 2, 20, 20));
        getContentPane().add(chartsPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JPanel chartsPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JComboBox<String> yearComboBox;
    private javax.swing.JLabel yearLabel;
    // End of variables declaration
}