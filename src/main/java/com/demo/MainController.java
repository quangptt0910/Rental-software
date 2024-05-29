package com.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private TextField searchBar;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private VBox vehiclesVBox;

    private DataStorage dataStorage = new DataStorage();

    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("All", "ICECar", "HybridCar", "BEVCar", "Motorcycle", "Pickup", "Camper");
        typeChoiceBox.setValue("All");
        displayVehicles(dataStorage.getAvailableVehicles());
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchBar.getText().toLowerCase();
        String selectedType = typeChoiceBox.getValue();

        List<Vehicle> filteredVehicles = dataStorage.getAvailableVehicles().stream()
                .filter(vehicle -> (selectedType.equals("All") || vehicle.getType().equals(selectedType)) &&
                        (vehicle.getBrand().toLowerCase().contains(searchTerm) ||
                                vehicle.getModel().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());

        displayVehicles(filteredVehicles);
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        vehiclesVBox.getChildren().clear();
        for (Vehicle vehicle : vehicles) {
            Button vehicleButton = new Button(vehicle.toString());
            vehicleButton.setMaxWidth(Double.MAX_VALUE);
            vehicleButton.setOnAction(event -> showVehicleDetails(vehicle));
            vehiclesVBox.getChildren().add(vehicleButton);
        }
    }

    private void showVehicleDetails(Vehicle vehicle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/demo/VehicleDetailsView.fxml"));
            Parent root = loader.load();

            VehicleDetailsController controller = loader.getController();
            controller.setVehicle(vehicle);

            Stage stage = new Stage();
            stage.setTitle(vehicle.getBrand() + " " + vehicle.getModel() + " Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
