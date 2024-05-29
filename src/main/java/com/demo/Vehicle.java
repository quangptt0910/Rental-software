package com.demo;

public class Vehicle {
    private int id;
    private String brand;
    private String model;
    private String type;
    private boolean isAvailable;
    private double pricePerDay;

    public Vehicle(int id, String brand, String model, String type, boolean isAvailable, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.isAvailable = isAvailable;
        this.pricePerDay = pricePerDay;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    @Override
    public String toString() {
        return brand + " " + model + " (" + type + ")";
    }
}
