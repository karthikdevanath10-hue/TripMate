package com.tripmate.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Demonstrates ENCAPSULATION & COMPOSITION:
 * Encapsulates complete booking information, tying together Trip, User, Destination,
 * and Payment records.
 */
public class Booking {

    private String bookingId;
    private Trip trip;
    private Payment payment;
    private LocalDateTime bookingDate;
    private String status; // "CONFIRMED", "CANCELLED"

    public Booking() {
    }

    public Booking(Trip trip, Payment payment) {
        this.bookingId = generateBookingId();
        this.trip = trip;
        this.payment = payment;
        this.bookingDate = LocalDateTime.now();
        this.status = (payment != null && payment.isSuccessful()) ? "CONFIRMED" : "PENDING";
    }

    private String generateBookingId() {
        int randomNum = 1000 + new Random().nextInt(9000);
        return "TM-2026-" + randomNum;
    }

    // Encapsulated accessors

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Calculated / convenience methods
    public double getTotalTripCost() {
        return (trip != null) ? trip.calculateTotalCost() : 0.0;
    }

    public double getAdvancePaid() {
        return (payment != null && payment.isSuccessful()) ? payment.getAdvanceAmount() : 0.0;
    }

    public double getRemainingBalance() {
        return Math.max(0, getTotalTripCost() - getAdvancePaid());
    }

    public String getCustomerName() {
        if (trip != null && trip.getUser() != null) {
            return trip.getUser().getFullName();
        }
        return "Traveler";
    }

    public String getDestinationName() {
        if (trip != null && trip.getDestination() != null) {
            return trip.getDestination().getName() + ", " + trip.getDestination().getState();
        }
        return "N/A";
    }

    public int getNumberOfTravellers() {
        if (trip != null && trip.getUser() != null) {
            return trip.getUser().getNumberOfTravellers();
        }
        return 1;
    }

    public String getFormattedBookingDate() {
        if (bookingDate == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a");
        return bookingDate.format(formatter);
    }
}

