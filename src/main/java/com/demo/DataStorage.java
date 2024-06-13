package com.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final int MAX_RETRIES = 10;
    private static final long RETRY_DELAY_MS = 100;

    public DataStorage() {
        DatabaseUtil.initializeDatabase();
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getBoolean("isAvailable"),
                        rs.getDouble("pricePerDay")
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE isAvailable = 1";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getBoolean("isAvailable"),
                        rs.getDouble("pricePerDay")
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String query = "SELECT DISTINCT brand FROM vehicles";

        try (Connection conn = connectWithRetry();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public void addReservation(Reservation reservation) {
        String insertReservation = "INSERT INTO reservations (vehicleId, customerName, customerEmail, customerPhone, reservationDate, returnDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmtReservation = conn.prepareStatement(insertReservation, Statement.RETURN_GENERATED_KEYS)) {

            pstmtReservation.setInt(1, reservation.getVehicleId());
            pstmtReservation.setString(2, reservation.getCustomerName());
            pstmtReservation.setString(3, reservation.getCustomerEmail());
            pstmtReservation.setString(4, reservation.getCustomerPhone());
            pstmtReservation.setString(5, reservation.getReservationDate());
            pstmtReservation.setString(6, reservation.getReturnDate());
            pstmtReservation.executeUpdate();

            ResultSet rs = pstmtReservation.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                reservation.setReservationId(generatedId);
                System.out.println("Generated reservationId: " + generatedId); // Debug statement
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        try (Connection conn = connectWithRetry();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("vehicleId"),
                        rs.getString("customerName"),
                        rs.getString("customerEmail"),
                        rs.getString("customerPhone"),
                        rs.getString("reservationDate"),
                        rs.getString("returnDate")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void addVehicle(Vehicle vehicle) {
        String insertVehicle = "INSERT INTO vehicles (brand, model, type, isAvailable, pricePerDay) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmt = conn.prepareStatement(insertVehicle, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, vehicle.getBrand());
            pstmt.setString(2, vehicle.getModel());
            pstmt.setString(3, vehicle.getType());
            pstmt.setBoolean(4, vehicle.isAvailable());
            pstmt.setDouble(5, vehicle.getPricePerDay());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                vehicle.setId(rs.getInt(1)); // Set the generated ID in the vehicle object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        String updateVehicle = "UPDATE vehicles SET brand = ?, model = ?, type = ?, isAvailable = ?, pricePerDay = ? WHERE id = ?";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmt = conn.prepareStatement(updateVehicle)) {

            pstmt.setString(1, vehicle.getBrand());
            pstmt.setString(2, vehicle.getModel());
            pstmt.setString(3, vehicle.getType());
            pstmt.setBoolean(4, vehicle.isAvailable());
            pstmt.setDouble(5, vehicle.getPricePerDay());
            pstmt.setInt(6, vehicle.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteVehicle(int vehicleId) {
        String deleteVehicle = "DELETE FROM vehicles WHERE id = ?";
        String deleteReservations = "DELETE FROM reservations WHERE vehicleId = ?";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmtVehicle = conn.prepareStatement(deleteVehicle);
             PreparedStatement pstmtReservations = conn.prepareStatement(deleteReservations)) {

            pstmtReservations.setInt(1, vehicleId);
            pstmtReservations.executeUpdate();

            pstmtVehicle.setInt(1, vehicleId);
            pstmtVehicle.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int reservationId) {
        String deleteReservation = "DELETE FROM reservations WHERE reservationId = ?";

        try (Connection conn = connectWithRetry();
             PreparedStatement pstmtReservation = conn.prepareStatement(deleteReservation)) {

            // Delete reservation
            pstmtReservation.setInt(1, reservationId);
            pstmtReservation.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connectWithRetry() throws SQLException {
        int retries = 0;
        while (true) {
            try {
                return DatabaseUtil.connect();
            } catch (SQLException e) {
                if (e.getMessage().contains("database is locked") && retries < MAX_RETRIES) {
                    retries++;
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Thread interrupted during retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
    }
}
