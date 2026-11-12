package com.tripmate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates DATA ENCAPSULATION:
 * Represents a travel destination with private fields and public accessors.
 */
public class Destination {

    private String id;
    private String name;
    private String state;
    private String description;
    private double travelCost;
    private double accommodationCost;
    private double foodCost;
    private double localTransportCost;
    private int recommendedDays;
    private List<String> touristAttractions = new ArrayList<>();
    private String imageUrl;
    private String tag; // e.g. "Hill Station", "Beach Paradise", "Heritage & Culture"

    public Destination() {
    }

    public Destination(String id, String name, String state, String description,
                       double travelCost, double accommodationCost, double foodCost,
                       double localTransportCost, int recommendedDays,
                       List<String> touristAttractions, String imageUrl, String tag) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.travelCost = travelCost;
        this.accommodationCost = accommodationCost;
        this.foodCost = foodCost;
        this.localTransportCost = localTransportCost;
        this.recommendedDays = recommendedDays;
        this.touristAttractions = touristAttractions;
        this.imageUrl = imageUrl;
        this.tag = tag;
    }

    // Encapsulation - Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTravelCost() {
        return travelCost;
    }

    public void setTravelCost(double travelCost) {
        this.travelCost = Math.max(0, travelCost);
    }

    public double getAccommodationCost() {
        return accommodationCost;
    }

    public void setAccommodationCost(double accommodationCost) {
        this.accommodationCost = Math.max(0, accommodationCost);
    }

    public double getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(double foodCost) {
        this.foodCost = Math.max(0, foodCost);
    }

    public double getLocalTransportCost() {
        return localTransportCost;
    }

    public void setLocalTransportCost(double localTransportCost) {
        this.localTransportCost = Math.max(0, localTransportCost);
    }

    public int getRecommendedDays() {
        return recommendedDays;
    }

    public void setRecommendedDays(int recommendedDays) {
        this.recommendedDays = Math.max(1, recommendedDays);
    }

    public List<String> getTouristAttractions() {
        return touristAttractions;
    }

    public void setTouristAttractions(List<String> touristAttractions) {
        this.touristAttractions = (touristAttractions != null) ? touristAttractions : new ArrayList<>();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Calculates the estimated cost for one person for this destination.
     * Formula: Travel + (Accommodation/day * days) + (Food/day * days) + LocalTransport
     */
    public double getEstimatedCostPerPerson() {
        return travelCost + (accommodationCost * recommendedDays) + (foodCost * recommendedDays) + localTransportCost;
    }
}

