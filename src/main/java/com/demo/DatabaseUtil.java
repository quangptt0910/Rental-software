package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/rental_cars.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createVehiclesTable = "CREATE TABLE IF NOT EXISTS vehicles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "brand TEXT, " +
                "model TEXT, " +
                "type TEXT, " +
                "isAvailable BOOLEAN, " +
                "pricePerDay REAL)";

        String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                "reservationId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehicleId INTEGER, " +
                "customerName TEXT, " +
                "customerEmail TEXT, " +
                "customerPhone TEXT, " +
                "reservationDate TEXT, " +
                "returnDate TEXT, " +
                "FOREIGN KEY(vehicleId) REFERENCES vehicles(id) ON DELETE CASCADE)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createVehiclesTable);
            stmt.execute(createReservationsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
