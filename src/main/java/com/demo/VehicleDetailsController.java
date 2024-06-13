package com.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VehicleDetailsController {

    @FXML
    private Label brandLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Label brandModelLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button reserveButton;

    private Vehicle vehicle;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        brandModelLabel.setText(vehicle.getBrand() + " " + vehicle.getModel());
        typeLabel.setText(vehicle.getType());
        priceLabel.setText("$" + vehicle.getPricePerDay() + " / day");
    }

    @FXML
    private void handleReserve() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/MakeReservationView.fxml"));
            Parent root = loader.load();

            MakeReservationController controller = loader.getController();
            controller.setVehicle(vehicle);

            Stage stage = new Stage();
            stage.setTitle("Make Reservation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
