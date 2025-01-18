package com.emsi.service;

import com.emsi.entities.Reservation;
import com.emsi.entities.Chamber;
import com.emsi.entities.Client;
import ma.emdi.connection.ConnectionJdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReservationService {
	private JTable table; // Reference to the UI table

	public void setTable(JTable table) {
		this.table = table;
	}

	public List<Reservation> findByClientId(int clientId) {
		List<Reservation> reservations = new ArrayList<>();
		String sql = "SELECT r.*, ch.telephone as room_number, " +
				"cat.libelle as room_type " +
				"FROM reservation r " +
				"JOIN chambre ch ON r.chamber_id = ch.id " +
				"JOIN categorie cat ON ch.categorie_id = cat.id " +
				"WHERE r.client_id = ?";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, clientId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Reservation res = new Reservation();
				res.setId(rs.getInt("id"));
				res.setClientName(rs.getString("client_name"));
				res.setRoomNumber(rs.getString("room_number"));
				res.setDateDebut(rs.getDate("date_debut"));
				res.setDateFin(rs.getDate("date_fin"));
				res.setStatus(rs.getString("status"));
				reservations.add(res);
			}
		} catch (SQLException e) {
			System.err.println("Error fetching reservations: " + e.getMessage());
		}
		return reservations;
	}

	// Add method to load reservations into the table
	public void loadReservationsToTable(JTable table, int clientId) {
		List<Reservation> reservations = findByClientId(clientId);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Clear existing rows

		for (Reservation res : reservations) {
			model.addRow(new Object[] {
					res.getRoomNumber(),
					res.getDateDebut(),
					res.getDateFin(),
					res.getStatus()
			});
		}
	}

	public List<Reservation> findAll() {
		List<Reservation> reservations = new ArrayList<>();
		String sql = "SELECT r.*, " +
				"CONCAT(c.nom, ' ', c.prenom) as client_name, " +
				"ch.telephone as room_number " +
				"FROM reservation r " +
				"JOIN client c ON r.client_id = c.id " +
				"JOIN chambre ch ON r.chamber_id = ch.id";

		try (Connection conn = ConnectionJdbc.getCnx();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(rs.getInt("id"));
				reservation.setClientName(rs.getString("client_name"));
				reservation.setRoomNumber(rs.getString("room_number"));
				reservation.setDateDebut(rs.getDate("date_debut"));
				reservation.setDateFin(rs.getDate("date_fin"));
				reservation.setStatus(rs.getString("status"));
				reservations.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reservations;
	}

	public boolean create(Reservation reservation) {
		// Assume you have methods to get IDs from names
		int clientId = getClientIdByName(reservation.getClientName());
		int chamberId = getChamberIdByRoomNumber(reservation.getRoomNumber());

		String sql = "INSERT INTO reservation (client_id, chamber_id, date_debut, date_fin, status) " +
				"VALUES (?, ?, ?, ?, 'pending')";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, clientId);
			stmt.setInt(2, chamberId);
			stmt.setDate(3, new java.sql.Date(reservation.getDateDebut().getTime()));
			stmt.setDate(4, new java.sql.Date(reservation.getDateFin().getTime()));

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error creating reservation: " + e.getMessage());
			return false;
		}
	}

	public boolean update(Reservation reservation) {
		int clientId = getClientIdByName(reservation.getClientName());
		int chamberId = getChamberIdByRoomNumber(reservation.getRoomNumber());

		String sql = "UPDATE reservation SET client_id = ?, chamber_id = ?, date_debut = ?, date_fin = ?, status = ? WHERE id = ?";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, clientId);
			stmt.setInt(2, chamberId);
			stmt.setDate(3, new java.sql.Date(reservation.getDateDebut().getTime()));
			stmt.setDate(4, new java.sql.Date(reservation.getDateFin().getTime()));
			stmt.setString(5, reservation.getStatus());
			stmt.setInt(6, reservation.getId());

			boolean success = stmt.executeUpdate() > 0;
			if (success && table != null) {
				refreshTable(); // Refresh the table after successful update
			}
			return success;
		} catch (SQLException e) {
			System.err.println("Error updating reservation: " + e.getMessage());
			return false;
		}
	}

	public boolean delete(Reservation reservation) {
		String sql = "DELETE FROM reservation WHERE id = ?";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, reservation.getId());
			boolean success = stmt.executeUpdate() > 0;
			if (success && table != null) {
				refreshTable(); // Refresh the table after successful deletion
			}
			return success;
		} catch (SQLException e) {
			System.err.println("Error deleting reservation: " + e.getMessage());
			return false;
		}
	}

	private void refreshTable() {
		if (table != null) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0); // Clear existing rows
			List<Reservation> reservations = findAll();
			for (Reservation res : reservations) {
				model.addRow(new Object[] {
						res.getId(),
						res.getClientName(),
						res.getRoomNumber(),
						res.getDateDebut(),
						res.getDateFin(),
						res.getStatus()
				});
			}
		}
	}

	// Example methods to get IDs
	private int getClientIdByName(String clientName) {
		String sql = "SELECT id FROM client WHERE CONCAT(nom, ' ', prenom) = ?";
		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, clientName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			System.err.println("Error getting client ID: " + e.getMessage());
		}
		return -1;
	}

	private int getChamberIdByRoomNumber(String roomNumber) {
		String sql = "SELECT id FROM chambre WHERE telephone = ?";
		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			// Extract room number from display string (e.g. "Room #101 (Standard)")
			String number = roomNumber.replaceAll(".*#(\\d+).*", "$1");
			stmt.setString(1, number);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			System.err.println("Error getting chamber ID: " + e.getMessage());
		}
		return -1;
	}

	public List<Chamber> findChambreBetweenDates(Client client, Date dateDebut, Date dateFin) {
		List<Chamber> chambers = new ArrayList<>();
		String sql = "SELECT DISTINCT ch.* FROM chambre ch " +
				"JOIN reservation r ON ch.id = r.chamber_id " +
				"JOIN categorie cat ON ch.categorie_id = cat.id " +
				"WHERE r.client_id = ? " +
				"AND ((r.date_debut BETWEEN ? AND ?) " +
				"OR (r.date_fin BETWEEN ? AND ?))";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, client.getId());
			stmt.setDate(2, new java.sql.Date(dateDebut.getTime()));
			stmt.setDate(3, new java.sql.Date(dateFin.getTime()));
			stmt.setDate(4, new java.sql.Date(dateDebut.getTime()));
			stmt.setDate(5, new java.sql.Date(dateFin.getTime()));

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Chamber chamber = new Chamber();
				chamber.setId(rs.getInt("id"));
				chamber.setTelephone(rs.getString("telephone"));
				chamber.setCategorieId(rs.getInt("categorie_id"));
				chambers.add(chamber);
			}
		} catch (SQLException e) {
			System.err.println("Error finding chambers between dates: " + e.getMessage());
		}
		return chambers;
	}

	public Reservation findById(int id) {
		String sql = "SELECT r.*, " +
				"CONCAT(c.nom, ' ', c.prenom) as client_name, " +
				"ch.telephone as room_number " +
				"FROM reservation r " +
				"JOIN client c ON r.client_id = c.id " +
				"JOIN chambre ch ON r.chamber_id = ch.id " +
				"WHERE r.id = ?";

		try (Connection conn = ConnectionJdbc.getCnx();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Reservation reservation = new Reservation();
				reservation.setId(rs.getInt("id"));
				reservation.setClientName(rs.getString("client_name"));
				reservation.setRoomNumber(rs.getString("room_number"));
				reservation.setDateDebut(rs.getDate("date_debut"));
				reservation.setDateFin(rs.getDate("date_fin"));
				reservation.setStatus(rs.getString("status"));
				return reservation;
			}
		} catch (SQLException e) {
			System.err.println("Error finding reservation by ID: " + e.getMessage());
		}
		return null;
	}
}
