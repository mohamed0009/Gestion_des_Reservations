package com.gui.ma;

import com.emsi.entities.Reservation;
import com.emsi.service.ReservationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationViewer extends JInternalFrame {
    private JTable reservationTable;
    private ReservationService reservationService;

    public ReservationViewer() {
        super("View Reservations", true, true, true, true);
        reservationService = new ReservationService();
        initComponents();
        loadReservations();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        reservationTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        add(scrollPane, BorderLayout.CENTER);
        setSize(600, 400);
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.findAll();
        String[] columns = { "Client", "Room", "Check-in", "Check-out", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Reservation reservation : reservations) {
            model.addRow(new Object[] {
                    reservation.getClientName(),
                    reservation.getRoomNumber(),
                    reservation.getDateDebut(),
                    reservation.getDateFin(),
                    reservation.getStatus()
            });
        }

        reservationTable.setModel(model);
    }
}