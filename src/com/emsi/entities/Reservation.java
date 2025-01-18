package com.emsi.entities;

import java.util.Date;

public class Reservation {
    private int id;
    private String clientName;
    private String roomNumber;
    private Date dateDebut;
    private Date dateFin;
    private String status;
    private int chamberId;
    private int clientId;

    public Reservation() {
        // Default constructor
    }

    public Reservation(int id, String clientName, String roomNumber, Date dateDebut, Date dateFin) {
        this.id = id;
        this.clientName = clientName;
        this.roomNumber = roomNumber;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getChamberId() {
        return chamberId;
    }

    public void setChamberId(int chamberId) {
        this.chamberId = chamberId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", clientName=" + clientName + ", roomNumber=" + roomNumber +
                ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + "]";
    }
}
