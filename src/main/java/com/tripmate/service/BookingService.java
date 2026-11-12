package com.tripmate.service;

import com.tripmate.model.Booking;
import com.tripmate.model.Payment;
import com.tripmate.model.Trip;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service managing booking creation, in-memory caching with HashMap/ArrayList,
 * and optional CSV file persistence.
 */
@Service
public class BookingService {

    private final Map<String, Booking> bookingMap = new ConcurrentHashMap<>();
    private static final String CSV_FILE_PATH = "data/bookings.csv";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @PostConstruct
    public void init() {
        loadBookingsFromCsv();
    }

    public synchronized Booking createBooking(Trip trip, Payment payment) {
        Booking booking = new Booking(trip, payment);
        bookingMap.put(booking.getBookingId(), booking);
        appendBookingToCsv(booking);
        return booking;
    }

    public Booking getBooking(String bookingId) {
        if (bookingId == null) {
            return null;
        }
        return bookingMap.get(bookingId.trim().toUpperCase());
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>(bookingMap.values());
        // Sort descending by booking date
        list.sort((a, b) -> {
            if (a.getBookingDate() == null || b.getBookingDate() == null) return 0;
            return b.getBookingDate().compareTo(a.getBookingDate());
        });
        return list;
    }

    private void appendBookingToCsv(Booking booking) {
        try {
            File file = new File(CSV_FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            boolean writeHeader = !file.exists() || file.length() == 0;
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                if (writeHeader) {
                    writer.println("BookingID,CustomerName,TripType,Destination,Travellers,TotalCost,AdvancePaid,RemainingBalance,TransactionID,Status,Date");
                }
                String dateStr = (booking.getBookingDate() != null) ? booking.getBookingDate().format(ISO_FORMATTER) : "";
                String txId = (booking.getPayment() != null) ? booking.getPayment().getTransactionId() : "N/A";
                writer.printf("%s,\"%s\",\"%s\",\"%s\",%d,%.2f,%.2f,%.2f,\"%s\",\"%s\",\"%s\"%n",
                        booking.getBookingId(),
                        sanitizeCsv(booking.getCustomerName()),
                        sanitizeCsv((booking.getTrip() != null) ? booking.getTrip().getTripType() : "N/A"),
                        sanitizeCsv(booking.getDestinationName()),
                        booking.getNumberOfTravellers(),
                        booking.getTotalTripCost(),
                        booking.getAdvancePaid(),
                        booking.getRemainingBalance(),
                        sanitizeCsv(txId),
                        booking.getStatus(),
                        dateStr
                );
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save booking to CSV file: " + e.getMessage());
        }
    }

    private void loadBookingsFromCsv() {
        File file = new File(CSV_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE_PATH), StandardCharsets.UTF_8)) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                // Basic CSV parse
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 10) {
                    String bookingId = cleanField(parts[0]);
                    String customerName = cleanField(parts[1]);
                    String tripType = cleanField(parts[2]);
                    String destination = cleanField(parts[3]);
                    int travellers = Integer.parseInt(cleanField(parts[4]));
                    double totalCost = Double.parseDouble(cleanField(parts[5]));
                    double advancePaid = Double.parseDouble(cleanField(parts[6]));
                    String txId = cleanField(parts[8]);
                    String status = cleanField(parts[9]);
                    LocalDateTime dt = LocalDateTime.now();
                    if (parts.length >= 11 && !parts[10].isEmpty()) {
                        try {
                            dt = LocalDateTime.parse(cleanField(parts[10]), ISO_FORMATTER);
                        } catch (Exception ignored) {}
                    }

                    Payment payment = new Payment();
                    payment.processPayment(txId);

                    Booking booking = new Booking();
                    booking.setBookingId(bookingId);
                    booking.setBookingDate(dt);
                    booking.setStatus(status);
                    booking.setPayment(payment);

                    bookingMap.put(bookingId, booking);
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not parse previous CSV bookings: " + e.getMessage());
        }
    }

    private String sanitizeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }

    private String cleanField(String val) {
        if (val == null) return "";
        String trimmed = val.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\"\"", "\"");
    }
}

