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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public String toString() {
        return brand + " " + model + " (" + type + ")";
    }
}
