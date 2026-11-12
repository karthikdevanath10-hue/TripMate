package com.tripmate.model;

/**
 * Demonstrates INHERITANCE and POLYMORPHISM:
 * Extends Trip and provides its own cost calculation for a single traveller.
 */
public class SoloTrip extends Trip {

    public SoloTrip() {
        super();
    }

    public SoloTrip(String tripId, User user, Destination destination, double totalBudget) {
        super(tripId, user, destination, totalBudget);
    }

    @Override
    public double calculateTotalCost() {
        if (getDestination() == null) {
            return 0.0;
        }
        // Solo trip cost = standard single traveller cost
        return getDestination().getEstimatedCostPerPerson();
    }

    @Override
    public double getPerPersonCost() {
        return calculateTotalCost();
    }

    @Override
    public String getTripType() {
        return "Solo Trip";
    }
}

