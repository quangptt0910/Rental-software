package com.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    public DataStorage() {
        DatabaseUtil.initializeDatabase();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE isAvailable = 1";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("id"),  // Changed to int
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

        try (Connection conn = DatabaseUtil.connect();
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
        String insertReservation = "INSERT INTO reservations (vehicleId, customerName, reservationDate, returnDate) VALUES (?, ?, ?, ?)";
        String updateVehicle = "UPDATE vehicles SET isAvailable = 0 WHERE id = ?";

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmtReservation = conn.prepareStatement(insertReservation);
             PreparedStatement pstmtVehicle = conn.prepareStatement(updateVehicle)) {

            pstmtReservation.setInt(1, reservation.getVehicleId());
            pstmtReservation.setString(2, reservation.getCustomerName());
            pstmtReservation.setString(3, reservation.getReservationDate());
            pstmtReservation.setString(4, reservation.getReturnDate());
            pstmtReservation.executeUpdate();

            pstmtVehicle.setInt(1, reservation.getVehicleId());
            pstmtVehicle.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        try (Connection conn = DatabaseUtil.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservationId"),  // Changed to int
                        rs.getInt("vehicleId"),  // Changed to int
                        rs.getString("customerName"),
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

        try (Connection conn = DatabaseUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertVehicle)) {

            pstmt.setString(1, vehicle.getBrand());
            pstmt.setString(2, vehicle.getModel());
            pstmt.setString(3, vehicle.getType());
            pstmt.setBoolean(4, vehicle.isAvailable());
            pstmt.setDouble(5, vehicle.getPricePerDay());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
