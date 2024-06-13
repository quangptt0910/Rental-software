package com.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainControllerDemo {

    @FXML
    private TextField searchBar;
    @FXML
    private HBox typeBox;
    @FXML
    private HBox brandBox;
    @FXML
    private VBox vehiclesVBox;

    private DataStorage dataStorage = new DataStorage();

    @FXML
    public void initialize() {
        // Initialize types
        String[] types = {"All", "ICECar", "HybridCar", "BEVCar","PHEVCar", "Motorcycle", "Pickup", "Camper"};
        for (String type : types) {
            Button typeButton = new Button(type);
            typeButton.setOnAction(event -> handleTypeFilter(type));
            typeBox.getChildren().add(typeButton);
        }

        // Initialize brands
        List<String> brands = dataStorage.getAllBrands();
        if (brands.isEmpty()) {
            System.out.println("No brands found in the database.");
        } else {
            for (String brand : brands) {
                Button brandButton = new Button(brand);
                brandButton.setOnAction(event -> handleBrandFilter(brand));
                brandButton.setMinWidth(100);
                brandButton.setMaxWidth(Double.MAX_VALUE);
                brandBox.getChildren().add(brandButton);
            }
        }

        // Display all vehicles initially
        List<Vehicle> vehicles = dataStorage.getAvailableVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found in the database.");
        } else {
            displayVehicles(vehicles);
        }
        
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchBar.getText().toLowerCase();
        List<Vehicle> filteredVehicles = dataStorage.getAvailableVehicles().stream()
                .filter(vehicle -> vehicle.getBrand().toLowerCase().contains(searchTerm) ||
                        vehicle.getModel().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        displayVehicles(filteredVehicles);
    }

    private void handleTypeFilter(String type) {
        String searchTerm = searchBar.getText().toLowerCase();
        List<Vehicle> filteredVehicles = dataStorage.getAvailableVehicles().stream()
                .filter(vehicle -> (type.equals("All") || vehicle.getType().equals(type)) &&
                        (vehicle.getBrand().toLowerCase().contains(searchTerm) ||
                                vehicle.getModel().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());

        displayVehicles(filteredVehicles);
    }

    private void handleBrandFilter(String brand) {
        String searchTerm = searchBar.getText().toLowerCase();
        List<Vehicle> filteredVehicles = dataStorage.getAvailableVehicles().stream()
                .filter(vehicle -> vehicle.getBrand().equals(brand) &&
                        (vehicle.getBrand().toLowerCase().contains(searchTerm) ||
                                vehicle.getModel().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());

        displayVehicles(filteredVehicles);
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        vehiclesVBox.getChildren().clear();
        for (Vehicle vehicle : vehicles) {
            HBox vehicleBox = new HBox(10);
            Label vehicleLabel = new Label(vehicle.toString());
            vehicleLabel.setWrapText(true); // Ensure the label text wraps
            vehicleLabel.setPrefWidth(300); // Set preferred width to ensure full text is visible
            Label priceLabel = new Label("$" + vehicle.getPricePerDay() + " / day");
            Button detailsButton = new Button("Details");
            detailsButton.setOnAction(event -> showVehicleDetails(vehicle));
            vehicleBox.getChildren().addAll(vehicleLabel, priceLabel, detailsButton);
            vehiclesVBox.getChildren().add(vehicleBox);
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
