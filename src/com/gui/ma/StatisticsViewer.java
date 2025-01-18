package com.gui.ma;

import com.emsi.entities.Client;
import com.emsi.service.ClientService;
import com.emsi.service.ReservationService;
import com.emsi.utils.PDFExporter;
import com.emsi.utils.StatisticsUtil;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Calendar;
import java.util.List;

public class StatisticsViewer extends JInternalFrame {
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> clientComboBox;
    private JPanel chartsPanel;
    private ReservationService reservationService;
    private Client currentClient;
    private StatisticsUtil statisticsUtil;

    public StatisticsViewer(Client client) {
        super("Statistics & Reports", true, true, true, true);
        this.currentClient = client;
        this.reservationService = new ReservationService();
        this.statisticsUtil = new StatisticsUtil();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // Control Panel at the top
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Year selection
        JLabel yearLabel = new JLabel("Select Year:");
        yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear - 5; year <= currentYear + 5; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(currentYear);
        yearComboBox.addActionListener(e -> refreshCharts());

        // Client selection (only visible for admin)
        JLabel clientLabel = new JLabel("Select Client:");
        clientComboBox = new JComboBox<>();
        if (currentClient == null) {
            // Admin view - load all clients
            loadClients();
            clientComboBox.addActionListener(e -> refreshCharts());
            controlPanel.add(clientLabel);
            controlPanel.add(clientComboBox);
        }

        // Export button
        JButton exportButton = new JButton("Export to PDF");
        exportButton.addActionListener(e -> exportToPDF());

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshCharts());

        controlPanel.add(yearLabel);
        controlPanel.add(yearComboBox);
        controlPanel.add(exportButton);
        controlPanel.add(refreshButton);

        add(controlPanel, BorderLayout.NORTH);

        // Charts Panel in the center
        chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(chartsPanel, BorderLayout.CENTER);
    }

    private void loadData() {
        if (currentClient != null) {
            refreshCharts();
        } else if (clientComboBox.getSelectedItem() != null) {
            refreshCharts();
        }
    }

    private void loadClients() {
        try {
            ClientService clientService = new ClientService();
            List<Client> clients = clientService.findAll();
            for (Client client : clients) {
                clientComboBox.addItem(client.getNom() + " " + client.getPrenom());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading clients: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCharts() {
        if (reservationService == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: ReservationService not initialized",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        chartsPanel.removeAll();

        try {
            // Get selected year
            int selectedYear = (Integer) yearComboBox.getSelectedItem();

            // Get client ID based on selection or current client
            int clientId;
            if (currentClient != null) {
                clientId = currentClient.getId();
            } else {
                String selectedClient = (String) clientComboBox.getSelectedItem();
                if (selectedClient == null)
                    return;
                // Extract client ID from the selected item
                ClientService clientService = new ClientService();
                List<Client> clients = clientService.findAll();
                Client selectedClientObj = clients.stream()
                        .filter(c -> (c.getNom() + " " + c.getPrenom()).equals(selectedClient))
                        .findFirst()
                        .orElse(null);
                if (selectedClientObj == null)
                    return;
                clientId = selectedClientObj.getId();
            }

            // Create and add charts
            JFreeChart roomsChart = statisticsUtil.createRoomsByCategoryChart(selectedYear, clientId);
            JFreeChart reservationsChart = statisticsUtil.createReservationsOverTimeChart(selectedYear, clientId);

            chartsPanel.add(new ChartPanel(roomsChart));
            chartsPanel.add(new ChartPanel(reservationsChart));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error refreshing charts: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private void exportToPDF() {
        if (reservationService == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: ReservationService not initialized",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files (*.pdf)", "pdf"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Get data for export
                int selectedYear = (Integer) yearComboBox.getSelectedItem();
                int clientId = currentClient != null ? currentClient.getId()
                        : ((Client) clientComboBox.getSelectedItem()).getId();

                // Create PDF
                PDFExporter pdfExporter = new PDFExporter();
                pdfExporter.exportStatistics(filePath, selectedYear, clientId,
                        reservationService.findAll(), statisticsUtil);

                JOptionPane.showMessageDialog(this,
                        "PDF exported successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error exporting PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}