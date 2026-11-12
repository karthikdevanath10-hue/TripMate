package com.tripmate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single day itinerary schedule with places to visit,
 * planned activities, and estimated daily expense.
 */
public class ItineraryDay {

    private int dayNumber;
    private String title;
    private List<String> places = new ArrayList<>();
    private List<String> activities = new ArrayList<>();
    private double estimatedDayCost;

    public ItineraryDay() {
    }

    public ItineraryDay(int dayNumber, String title, List<String> places, List<String> activities, double estimatedDayCost) {
        this.dayNumber = dayNumber;
        this.title = title;
        this.places = places;
        this.activities = activities;
        this.estimatedDayCost = estimatedDayCost;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public double getEstimatedDayCost() {
        return estimatedDayCost;
    }

    public void setEstimatedDayCost(double estimatedDayCost) {
        this.estimatedDayCost = estimatedDayCost;
    }
}

