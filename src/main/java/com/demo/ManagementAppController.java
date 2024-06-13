package com.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ManagementAppController {

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, Integer> idColumn;
    @FXML
    private TableColumn<Vehicle, String> brandColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, String> typeColumn;
    @FXML
    private TableColumn<Vehicle, Double> priceColumn;
    @FXML
    private TableColumn<Vehicle, Boolean> availabilityColumn;

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> reservationVehicleIdColumn;
    @FXML
    private TableColumn<Reservation, String> customerNameColumn;
    @FXML
    private TableColumn<Reservation, String> customerEmailColumn;
    @FXML
    private TableColumn<Reservation, String> customerPhoneColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> returnDateColumn;

    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField availabilityField;

    @FXML
    private TextField vehicleSearchField;
    @FXML
    private ChoiceBox<String> vehicleBrandFilterChoiceBox;
    @FXML
    private ChoiceBox<String> vehicleTypeFilterChoiceBox;
    @FXML
    private TextField reservationSearchField;
    @FXML
    private ChoiceBox<Integer> reservationVehicleIdFilterChoiceBox;

    private DataStorage dataStorage;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<Reservation> reservationList;

    private FilteredList<Vehicle> filteredVehicleList;
    private FilteredList<Reservation> filteredReservationList;

    public void initialize() {
        dataStorage = new DataStorage();
        loadVehicleData();
        loadReservationData();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        vehicleTable.setItems(filteredVehicleList);

        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
                brandField.setText(selectedVehicle.getBrand());
                modelField.setText(selectedVehicle.getModel());
                typeField.setText(selectedVehicle.getType());
                priceField.setText(String.valueOf(selectedVehicle.getPricePerDay()));
                availabilityField.setText(String.valueOf(selectedVehicle.isAvailable()));
            }
        });

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        reservationVehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        reservationTable.setItems(filteredReservationList);

        // Set up a Timeline to refresh the data every 10 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            loadReservationData();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        setupFilters();
    }

    private void loadVehicleData() {
        List<Vehicle> vehicles = dataStorage.getAllVehicles();
        vehicleList = FXCollections.observableArrayList(vehicles);
        filteredVehicleList = new FilteredList<>(vehicleList, p -> true);
        vehicleTable.setItems(filteredVehicleList);
        setupFilters(); // Setup filters after loading data
    }

    private void loadReservationData() {
        List<Reservation> reservations = dataStorage.getReservations();
        reservationList = FXCollections.observableArrayList(reservations);
        filteredReservationList = new FilteredList<>(reservationList, p -> true);
        reservationTable.setItems(filteredReservationList);
    }

    private void setupFilters() {
        // Initialize brand filter ChoiceBox
        vehicleBrandFilterChoiceBox.setItems(FXCollections.observableArrayList(
                vehicleList.stream().map(Vehicle::getBrand).distinct().collect(Collectors.toList())
        ));
        vehicleBrandFilterChoiceBox.getItems().add(0, "All Brands");
        vehicleBrandFilterChoiceBox.setValue("All Brands");

        // Initialize type filter ChoiceBox
        vehicleTypeFilterChoiceBox.setItems(FXCollections.observableArrayList(
                vehicleList.stream().map(Vehicle::getType).distinct().collect(Collectors.toList())
        ));
        vehicleTypeFilterChoiceBox.getItems().add(0, "All Types");
        vehicleTypeFilterChoiceBox.setValue("All Types");

        // Initialize vehicle ID filter ChoiceBox for reservations
        reservationVehicleIdFilterChoiceBox.setItems(FXCollections.observableArrayList(
                vehicleList.stream().map(Vehicle::getId).collect(Collectors.toList())
        ));
        reservationVehicleIdFilterChoiceBox.getItems().add(0, null);
        reservationVehicleIdFilterChoiceBox.setValue(null);
    }

    @FXML
    private void handleAddVehicle() {
        try {
            String brand = brandField.getText();
            String model = modelField.getText();
            String type = typeField.getText();
            double price = Double.parseDouble(priceField.getText());
            boolean isAvailable = Boolean.parseBoolean(availabilityField.getText());

            Vehicle newVehicle = new Vehicle(0, brand, model, type, isAvailable, price);
            dataStorage.addVehicle(newVehicle); // This will set the auto-incremented ID in newVehicle

            if (newVehicle.getId() != 0) { // Check if ID was correctly set
                vehicleList.add(newVehicle);
                vehicleTable.refresh(); // Refresh the table to show the new vehicle
                clearFields();
            } else {
                showAlert("Failed to add vehicle. Please try again.");
            }
        } catch (Exception e) {
            showAlert("Invalid input. Please check your data.");
        }
    }

    @FXML
    private void handleUpdateVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            try {
                selectedVehicle.setBrand(brandField.getText());
                selectedVehicle.setModel(modelField.getText());
                selectedVehicle.setType(typeField.getText());
                selectedVehicle.setPricePerDay(Double.parseDouble(priceField.getText()));
                selectedVehicle.setAvailable(Boolean.parseBoolean(availabilityField.getText()));

                dataStorage.updateVehicle(selectedVehicle);
                vehicleTable.refresh();
                clearFields();
            } catch (Exception e) {
                showAlert("Invalid input. Please check your data.");
            }
        } else {
            showAlert("No vehicle selected. Please select a vehicle to update.");
        }
    }

    @FXML
    private void handleDeleteVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            dataStorage.deleteVehicle(selectedVehicle.getId());
            vehicleList.remove(selectedVehicle);
            clearFields();
        } else {
            showAlert("No vehicle selected. Please select a vehicle to delete.");
        }
    }

    @FXML
    private void handleDeleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            dataStorage.deleteReservation(selectedReservation.getReservationId());
            reservationList.remove(selectedReservation);
            loadVehicleData(); // Refresh vehicle data to reflect changes
        } else {
            showAlert("No reservation selected. Please select a reservation to delete.");
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        typeField.clear();
        priceField.clear();
        availabilityField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleVehicleSearch() {
        String searchText = vehicleSearchField.getText().toLowerCase();
        filteredVehicleList.setPredicate(vehicle -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            return vehicle.getBrand().toLowerCase().contains(searchText)
                    || vehicle.getModel().toLowerCase().contains(searchText);
        });
    }

    @FXML
    private void handleVehicleFilter() {
        String selectedBrand = vehicleBrandFilterChoiceBox.getValue();
        String selectedType = vehicleTypeFilterChoiceBox.getValue();

        Predicate<Vehicle> brandPredicate = vehicle -> {
            if (selectedBrand == null || selectedBrand.equals("All Brands")) {
                return true;
            }
            return vehicle.getBrand().equalsIgnoreCase(selectedBrand);
        };

        Predicate<Vehicle> typePredicate = vehicle -> {
            if (selectedType == null || selectedType.equals("All Types")) {
                return true;
            }
            return vehicle.getType().equalsIgnoreCase(selectedType);
        };

        filteredVehicleList.setPredicate(brandPredicate.and(typePredicate));
    }

    @FXML
    private void handleReservationSearch() {
        String searchText = reservationSearchField.getText().toLowerCase();
        filteredReservationList.setPredicate(reservation -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            return reservation.getCustomerName().toLowerCase().contains(searchText)
                    || reservation.getCustomerEmail().toLowerCase().contains(searchText)
                    || reservation.getCustomerPhone().toLowerCase().contains(searchText);
        });
    }

    @FXML
    private void handleReservationFilter() {
        Integer selectedVehicleId = reservationVehicleIdFilterChoiceBox.getValue();

        Predicate<Reservation> vehicleIdPredicate = reservation -> {
            if (selectedVehicleId == null) {
                return true;
            }
            return reservation.getVehicleId() == selectedVehicleId;
        };

        filteredReservationList.setPredicate(vehicleIdPredicate);
    }
}
