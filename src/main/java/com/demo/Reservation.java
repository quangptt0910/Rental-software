package com.demo;

public class Reservation {
    private int reservationId;  // Changed to int
    private int vehicleId;  // Changed to int
    private String customerName;
    private String reservationDate;
    private String returnDate;

    // Constructor, getters, setters, etc.

    public Reservation(int reservationId, int vehicleId, String customerName, String reservationDate, String returnDate) {
        this.reservationId = reservationId;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.reservationDate = reservationDate;
        this.returnDate = returnDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
