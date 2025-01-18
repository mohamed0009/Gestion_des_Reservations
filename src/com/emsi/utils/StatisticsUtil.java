package com.emsi.utils;

import com.emsi.service.ChambreService;
import com.emsi.service.ReservationService;
import com.emsi.entities.Reservation;
import com.emsi.entities.Chamber;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsUtil {
    private ReservationService reservationService;
    private ChambreService chambreService;

    public StatisticsUtil() {
        this.reservationService = new ReservationService();
        this.chambreService = new ChambreService();
    }

    public JFreeChart createRoomsByCategoryChart(int year, int clientId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Get all reservations for the client in the specified year
            List<Reservation> reservations = reservationService.findAll().stream()
                    .filter(r -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(r.getDateDebut());
                        return cal.get(Calendar.YEAR) == year && r.getClientId() == clientId;
                    })
                    .collect(Collectors.toList());

            // Count reservations by room category
            Map<String, Integer> categoryCount = new HashMap<>();
            for (Reservation res : reservations) {
                Chamber chambre = chambreService.findById(res.getChamberId());
                String category = chambre.getCategorie().getCode();
                categoryCount.merge(category, 1, Integer::sum);
            }

            // Add data to dataset
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                dataset.addValue(entry.getValue(), "Reservations", entry.getKey());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Room Reservations by Category",
                "Room Category",
                "Number of Reservations",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
    }

    public JFreeChart createReservationsOverTimeChart(int year, int clientId) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Reservations");

        try {
            // Get all reservations for the client in the specified year
            List<Reservation> reservations = reservationService.findAll().stream()
                    .filter(r -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(r.getDateDebut());
                        return cal.get(Calendar.YEAR) == year && r.getClientId() == clientId;
                    })
                    .collect(Collectors.toList());

            // Count reservations by month
            Map<Integer, Integer> monthCount = new HashMap<>();
            for (Reservation res : reservations) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(res.getDateDebut());
                int month = cal.get(Calendar.MONTH);
                monthCount.merge(month, 1, Integer::sum);
            }

            // Add data to dataset
            for (int month = 0; month < 12; month++) {
                series.add(new Month(month + 1, year), monthCount.getOrDefault(month, 0));
            }
            dataset.addSeries(series);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createTimeSeriesChart(
                "Reservations Over Time",
                "Month",
                "Number of Reservations",
                dataset,
                true,
                true,
                false);
    }
}