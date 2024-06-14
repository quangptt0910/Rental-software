package com.demo;

import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class MakeReservationView {
    private DataStorage dataStorage = new DataStorage();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Make Reservation");

        // Hide the vehicle ID field
        TextField vehicleIdField = new TextField();
        vehicleIdField.setPromptText("Vehicle ID");
        vehicleIdField.setVisible(false);

        TextField customerNameField = new TextField();
        customerNameField.setPromptText("Customer Name");

        TextField customerEmailField = new TextField();
        customerEmailField.setPromptText("Customer Email");

        TextField customerPhoneField = new TextField();
        customerPhoneField.setPromptText("Customer Phone");

        TextField reservationDateField = new TextField();
        reservationDateField.setPromptText("Reservation Date (YYYY-MM-DD)");

        TextField returnDateField = new TextField();
        returnDateField.setPromptText("Return Date (YYYY-MM-DD)");

        Button reserveButton = new Button("Reserve");
        reserveButton.setOnAction(e -> {
            try {
                int vehicleId = 0;
                Reservation reservation = new Reservation(
                        0,
                        vehicleId,
                        customerNameField.getText(),
                        customerEmailField.getText(),
                        customerPhoneField.getText(),
                        reservationDateField.getText(),
                        returnDateField.getText()
                );
                dataStorage.addReservation(reservation);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reservation made successfully!");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to make reservation. Please check your input.");
                alert.showAndWait();
            }
        });

        VBox vbox = new VBox(10, customerNameField, customerEmailField, customerPhoneField, reservationDateField, returnDateField, reserveButton);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}
