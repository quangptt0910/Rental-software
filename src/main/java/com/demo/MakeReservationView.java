package com.demo;

// MakeReservationView.java
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

        TextField vehicleIdField = new TextField();
        vehicleIdField.setPromptText("Vehicle ID");

        TextField customerNameField = new TextField();
        customerNameField.setPromptText("Customer Name");

        TextField reservationDateField = new TextField();
        reservationDateField.setPromptText("Reservation Date");

        TextField returnDateField = new TextField();
        returnDateField.setPromptText("Return Date");

        Button reserveButton = new Button("Reserve");
        reserveButton.setOnAction(e -> {
            try {
                int vehicleId = Integer.parseInt(vehicleIdField.getText());
                Reservation reservation = new Reservation(
                        0, // Dummy value for reservationId, as it will be auto-incremented by the database
                        vehicleId,
                        customerNameField.getText(),
                        reservationDateField.getText(),
                        returnDateField.getText()
                );
                dataStorage.addReservation(reservation);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reservation made successfully!");
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid vehicle ID. Please enter a valid number.");
                alert.showAndWait();
            }
        });

        VBox vbox = new VBox(10, vehicleIdField, customerNameField, reservationDateField, returnDateField, reserveButton);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
