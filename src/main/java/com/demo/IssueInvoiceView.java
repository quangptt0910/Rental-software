package com.demo;
// IssueInvoiceView.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.List;

public class IssueInvoiceView {
    private DataStorage dataStorage = new DataStorage();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Issue Invoice");

        ListView<String> reservationsListView = new ListView<>();
        Button loadReservationsButton = new Button("Load Reservations");

        loadReservationsButton.setOnAction(e -> {
            List<Reservation> reservations = dataStorage.getReservations();
            reservationsListView.getItems().clear();
            reservations.forEach(reservation -> reservationsListView.getItems().add(reservation.toString()));
        });

        VBox vbox = new VBox(loadReservationsButton, reservationsListView);
        Scene scene = new Scene(vbox, 300, 400);
        stage.setScene(scene);
        stage.show();
    }
}
