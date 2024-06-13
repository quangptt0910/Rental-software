package com.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MakeReservationController {
    @FXML
    private TextField vehicleIdField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerEmailField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private TextField reservationDateField;
    @FXML
    private TextField returnDateField;

    private DataStorage dataStorage = new DataStorage();
    private Vehicle vehicle;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleIdField.setText(String.valueOf(vehicle.getId()));
        vehicleIdField.setDisable(true);
    }

    @FXML
    private void handleConfirmReservation() {
        try {
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
            String customerName = customerNameField.getText();
            String customerEmail = customerEmailField.getText();
            String customerPhone = customerPhoneField.getText();
            String reservationDate = reservationDateField.getText();
            String returnDate = returnDateField.getText();

            if (customerName.isEmpty() || customerEmail.isEmpty() || customerPhone.isEmpty() || reservationDate.isEmpty() || returnDate.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }

            Reservation reservation = new Reservation(
                    0, // Dummy value for reservationId, as it will be auto-incremented by the database
                    vehicleId,
                    customerName,
                    customerEmail,
                    customerPhone,
                    reservationDate,
                    returnDate
            );

            dataStorage.addReservation(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation made successfully!");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid vehicle ID. Please enter a valid number.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
