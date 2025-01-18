package com.gui.ma;

import com.emsi.utils.StatisticsUtil;
import com.gui.utils.StyleUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private StatisticsUtil statisticsUtil;

    public DashboardPanel() {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statisticsUtil = new StatisticsUtil();
        initializeCharts();
    }

    private void initializeCharts() {
        // Room distribution by category (Pie Chart)
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        JFreeChart roomsByCategoryChart = statisticsUtil.createRoomsByCategoryChart(currentYear, 0);
        ChartPanel roomsPanel = new ChartPanel(roomsByCategoryChart);
        roomsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Room Distribution"));
        add(roomsPanel);

        // Monthly reservations (Bar Chart)
        JFreeChart monthlyReservationsChart = statisticsUtil.createReservationsOverTimeChart(currentYear, 0);
        ChartPanel reservationsPanel = new ChartPanel(monthlyReservationsChart);
        reservationsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Reservation Trends"));
        add(reservationsPanel);

        // Revenue by category (Bar Chart)
        JFreeChart revenueChart = statisticsUtil.createRoomsByCategoryChart(currentYear, 0);
        ChartPanel revenuePanel = new ChartPanel(revenueChart);
        revenuePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Revenue Analysis"));
        add(revenuePanel);

        // Occupancy rate by category (Bar Chart)
        JFreeChart occupancyChart = statisticsUtil.createRoomsByCategoryChart(currentYear, 0);
        ChartPanel occupancyPanel = new ChartPanel(occupancyChart);
        occupancyPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Occupancy Rates"));
        add(occupancyPanel);
    }

    public void refreshCharts() {
        removeAll();
        initializeCharts();
        revalidate();
        repaint();
    }
}