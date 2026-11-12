package com.tripmate.model;

/**
 * Demonstrates INHERITANCE and POLYMORPHISM:
 * Extends Trip and provides realistic group cost calculation accounting for
 * shared accommodation (double occupancy) and shared local transportation.
 */
public class GroupTrip extends Trip {

    public GroupTrip() {
        super();
    }

    public GroupTrip(String tripId, User user, Destination destination, double totalBudget) {
        super(tripId, user, destination, totalBudget);
    }

    @Override
    public double calculateTotalCost() {
        Destination dest = getDestination();
        User usr = getUser();
        if (dest == null || usr == null) {
            return 0.0;
        }

        int travellers = Math.max(1, usr.getNumberOfTravellers());
        int days = Math.max(1, dest.getRecommendedDays());

        // Group sharing dynamics:
        // 1. Travel tickets are per person
        double totalTravelCost = dest.getTravelCost() * travellers;

        // 2. Hotel rooms are shared (2 people per room)
        int roomsNeeded = (int) Math.ceil(travellers / 2.0);
        double totalAccommodationCost = roomsNeeded * dest.getAccommodationCost() * days;

        // 3. Food is per person
        double totalFoodCost = dest.getFoodCost() * days * travellers;

        // 4. Local transport is shared via group vehicle / cabs
        // Base vehicle covers up to 4 persons; additional vehicles/fares scaled smoothly
        double groupTransportFactor = 1.0 + (travellers - 1) * 0.30;
        double totalLocalTransportCost = dest.getLocalTransportCost() * groupTransportFactor;

        return Math.round(totalTravelCost + totalAccommodationCost + totalFoodCost + totalLocalTransportCost);
    }

    @Override
    public double getPerPersonCost() {
        User usr = getUser();
        int travellers = (usr != null && usr.getNumberOfTravellers() > 0) ? usr.getNumberOfTravellers() : 1;
        return Math.round(calculateTotalCost() / travellers);
    }

    @Override
    public String getTripType() {
        return "Group Trip";
    }

    public int getRoomsNeeded() {
        User usr = getUser();
        int travellers = (usr != null && usr.getNumberOfTravellers() > 0) ? usr.getNumberOfTravellers() : 1;
        return (int) Math.ceil(travellers / 2.0);
    }
}

