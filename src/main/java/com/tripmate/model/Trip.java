package com.tripmate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates ABSTRACTION, INHERITANCE and ENCAPSULATION:
 * - Abstract base class defining common trip behaviors and encapsulated fields.
 * - Subclasses (SoloTrip and GroupTrip) implement calculateTotalCost() polymorphically.
 */
public abstract class Trip {

    private String tripId;
    private User user;
    private Destination destination;
    private double totalBudget;
    private List<ItineraryDay> itinerary = new ArrayList<>();

    public Trip() {
    }

    public Trip(String tripId, User user, Destination destination, double totalBudget) {
        this.tripId = tripId;
        this.user = user;
        this.destination = destination;
        this.totalBudget = totalBudget;
    }

    // Abstract method demonstrating Polymorphism in subclasses
    public abstract double calculateTotalCost();

    // Abstract method for per-person cost breakdown
    public abstract double getPerPersonCost();

    // Subclasses return their specific trip category
    public abstract String getTripType();

    // Concrete shared logic based on encapsulated attributes
    public double calculateRemainingBudget() {
        return Math.max(0, this.totalBudget - calculateTotalCost());
    }

    public boolean isWithinBudget() {
        return this.totalBudget >= calculateTotalCost();
    }

    public double getBudgetDeficit() {
        double diff = calculateTotalCost() - this.totalBudget;
        return Math.max(0, diff);
    }

    // Encapsulated Getters & Setters
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        if (totalBudget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }
        this.totalBudget = totalBudget;
    }

    public List<ItineraryDay> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<ItineraryDay> itinerary) {
        this.itinerary = (itinerary != null) ? itinerary : new ArrayList<>();
    }
}

