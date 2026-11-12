package com.tripmate.service;

import com.tripmate.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service managing trip creation, polymorphic cost calculation,
 * and day-by-day itinerary generation.
 */
@Service
public class TripService {

    /**
     * Factory method: Instantiates either SoloTrip or GroupTrip demonstrating POLYMORPHISM.
     */
    public Trip createTrip(User user, Destination destination, double totalBudget) {
        String tripId = "TRIP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Trip trip;

        if (user.isGroup()) {
            trip = new GroupTrip(tripId, user, destination, totalBudget);
        } else {
            trip = new SoloTrip(tripId, user, destination, totalBudget);
        }

        // Generate tailored day-wise itinerary
        List<ItineraryDay> itinerary = generateItinerary(destination, trip);
        trip.setItinerary(itinerary);

        return trip;
    }

    /**
     * Generates a realistic day-wise itinerary based on the destination's recommended days
     * and attractions.
     */
    public List<ItineraryDay> generateItinerary(Destination destination, Trip trip) {
        List<ItineraryDay> daysList = new ArrayList<>();
        if (destination == null) {
            return daysList;
        }

        int totalDays = destination.getRecommendedDays();
        List<String> attractions = destination.getTouristAttractions();
        double totalCost = (trip != null) ? trip.calculateTotalCost() : destination.getEstimatedCostPerPerson();
        double averageDailyCost = Math.round(totalCost / Math.max(1, totalDays));

        for (int i = 1; i <= totalDays; i++) {
            List<String> places = new ArrayList<>();
            List<String> activities = new ArrayList<>();
            String dayTitle;

            if (i == 1) {
                dayTitle = "Arrival & Landmark Sightseeing";
                if (!attractions.isEmpty()) places.add(attractions.get(0));
                if (attractions.size() > 1) places.add(attractions.get(1));
                activities.add("Arrival in " + destination.getName() + " and hotel check-in");
                activities.add("Orientation walk and exploring local markets");
                activities.add("Enjoying authentic regional cuisine for dinner");
            } else if (i == totalDays) {
                dayTitle = "Scenic Views, Souvenirs & Departure";
                if (attractions.size() >= totalDays) {
                    places.add(attractions.get(attractions.size() - 1));
                }
                places.add("Local Artisan Handicrafts & Souvenir Market");
                activities.add("Morning panoramic viewpoint or temple visit");
                activities.add("Shopping for local specialties and mementos");
                activities.add("Hotel check-out and relaxed return journey");
            } else {
                dayTitle = "Deep Cultural Exploration & Adventure";
                int idx = Math.min(i, attractions.size() - 1);
                places.add(attractions.get(idx));
                if (attractions.size() > idx + 1) {
                    places.add(attractions.get(idx + 1));
                }
                activities.add("Guided sightseeing tour and photography session");
                activities.add("Tasting local street foods and regional beverages");
                activities.add("Sunset viewpoints and leisure evening activities");
            }

            ItineraryDay day = new ItineraryDay(i, dayTitle, places, activities, averageDailyCost);
            daysList.add(day);
        }

        return daysList;
    }
}

