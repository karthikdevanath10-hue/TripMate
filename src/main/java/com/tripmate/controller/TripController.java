package com.tripmate.controller;

import com.tripmate.model.*;
import com.tripmate.service.BookingService;
import com.tripmate.service.DemoPaymentService;
import com.tripmate.service.DestinationService;
import com.tripmate.service.TripService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TripController {

    private final DestinationService destinationService;
    private final TripService tripService;
    private final BookingService bookingService;
    private final DemoPaymentService paymentService;

    @Autowired
    public TripController(DestinationService destinationService,
                          TripService tripService,
                          BookingService bookingService,
                          DemoPaymentService paymentService) {
        this.destinationService = destinationService;
        this.tripService = tripService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    // ==========================================
    // 1. HOME PAGE
    // ==========================================
    @GetMapping("/")
    public String home(Model model) {
        List<Destination> all = destinationService.getAllDestinations();
        // Featured 6 destinations for homepage
        model.addAttribute("featuredDestinations", all.subList(0, Math.min(6, all.size())));
        return "index";
    }

    // ==========================================
    // 2. TRIP TYPE SELECTION
    // ==========================================
    @GetMapping("/plan")
    public String tripSelection() {
        return "trip-selection";
    }

    // ==========================================
    // 3. USER DETAILS & BUDGET ENTRY
    // ==========================================
    @GetMapping("/user-details")
    public String showUserDetailsForm(@RequestParam(value = "tripType", defaultValue = "solo") String tripType,
                                      @RequestParam(value = "destId", required = false) String destId,
                                      Model model,
                                      HttpSession session) {
        boolean isGroup = "group".equalsIgnoreCase(tripType);
        model.addAttribute("tripType", isGroup ? "group" : "solo");
        model.addAttribute("isGroup", isGroup);
        model.addAttribute("destId", destId);

        // Prepopulate if previously in session
        User user = (User) session.getAttribute("sessionUser");
        if (user == null) {
            user = new User();
            if (isGroup) {
                user.setNumberOfTravellers(2);
            } else {
                user.setNumberOfTravellers(1);
            }
        }
        model.addAttribute("user", user);

        Double budget = (Double) session.getAttribute("sessionBudget");
        model.addAttribute("budget", (budget != null) ? budget : (isGroup ? 20000.0 : 10000.0));

        return "user-details";
    }

    @PostMapping("/user-details")
    public String processUserDetails(@RequestParam("tripType") String tripType,
                                     @RequestParam("fullName") String fullName,
                                     @RequestParam("age") int age,
                                     @RequestParam("gender") String gender,
                                     @RequestParam("startingCity") String startingCity,
                                     @RequestParam(value = "contactNumber", required = false) String contactNumber,
                                     @RequestParam(value = "groupName", required = false) String groupName,
                                     @RequestParam(value = "numberOfTravellers", defaultValue = "1") int numberOfTravellers,
                                     @RequestParam(value = "memberNames", required = false) List<String> memberNames,
                                     @RequestParam(value = "memberAges", required = false) List<Integer> memberAges,
                                     @RequestParam("budget") double budget,
                                     @RequestParam(value = "destId", required = false) String destId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        // --- Server-side Validations ---
        if (fullName == null || fullName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Full Name is mandatory.");
            return "redirect:/user-details?tripType=" + tripType;
        }

        if (age < 1 || age > 120) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide a valid age between 1 and 120.");
            return "redirect:/user-details?tripType=" + tripType;
        }

        if (startingCity == null || startingCity.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Starting City is required.");
            return "redirect:/user-details?tripType=" + tripType;
        }

        if (budget < 1000) {
            redirectAttributes.addFlashAttribute("errorMessage", "Minimum budget must be at least ₹1,000.");
            return "redirect:/user-details?tripType=" + tripType;
        }

        boolean isGroup = "group".equalsIgnoreCase(tripType);
        if (isGroup && numberOfTravellers < 2) {
            redirectAttributes.addFlashAttribute("errorMessage", "Group trips must have at least 2 travellers.");
            return "redirect:/user-details?tripType=" + tripType;
        }

        // Construct User using encapsulation
        User user = new User();
        user.setFullName(fullName);
        user.setAge(age);
        user.setGender(gender);
        user.setStartingCity(startingCity);
        user.setContactNumber(contactNumber);

        if (isGroup) {
            user.setGroupName(groupName);
            user.setNumberOfTravellers(numberOfTravellers);
            if (memberNames != null) {
                user.setMemberNames(memberNames);
            }
            if (memberAges != null) {
                user.setMemberAges(memberAges);
            }
        } else {
            user.setNumberOfTravellers(1);
        }

        // Save in session
        session.setAttribute("sessionUser", user);
        session.setAttribute("sessionBudget", budget);
        session.setAttribute("sessionTripType", isGroup ? "group" : "solo");

        if (destId != null && !destId.trim().isEmpty()) {
            return "redirect:/itinerary/" + destId.trim();
        }

        return "redirect:/destinations";
    }

    // ==========================================
    // 4. DESTINATION RECOMMENDATIONS
    // ==========================================
    @GetMapping("/destinations")
    public String showDestinations(@RequestParam(value = "search", required = false) String search,
                                   @RequestParam(value = "maxBudget", required = false) Double maxBudget,
                                   Model model,
                                   HttpSession session) {

        User user = (User) session.getAttribute("sessionUser");
        Double budget = (Double) session.getAttribute("sessionBudget");

        // Defaults if user navigated directly without filling form
        if (user == null) {
            user = new User("Guest Traveler", 24, "Other", "Bengaluru", "");
            user.setNumberOfTravellers(1);
            session.setAttribute("sessionUser", user);
        }
        if (budget == null) {
            budget = 15000.0;
            session.setAttribute("sessionBudget", budget);
        }

        double effectiveBudget = (maxBudget != null && maxBudget > 0) ? maxBudget : budget;

        List<Destination> recommendedList;
        if (search != null && !search.trim().isEmpty()) {
            recommendedList = destinationService.searchDestinations(search);
        } else {
            recommendedList = destinationService.getRecommendedDestinations(effectiveBudget, user);
        }

        List<Destination> allDestinations = destinationService.getAllDestinations();

        model.addAttribute("user", user);
        model.addAttribute("budget", effectiveBudget);
        model.addAttribute("recommendedDestinations", recommendedList);
        model.addAttribute("allDestinations", allDestinations);
        model.addAttribute("searchQuery", search);
        model.addAttribute("isGroup", user.isGroup());

        // Calculate per person budget for display
        double perPersonBudget = user.isGroup() ? Math.round(effectiveBudget / user.getNumberOfTravellers()) : effectiveBudget;
        model.addAttribute("perPersonBudget", perPersonBudget);

        if (recommendedList.isEmpty()) {
            List<Destination> alternatives = destinationService.getAffordableAlternatives(effectiveBudget, user, 3);
            model.addAttribute("affordableAlternatives", alternatives);
        }

        return "destinations";
    }

    // ==========================================
    // 5. TRIP ITINERARY GENERATION
    // ==========================================
    @GetMapping("/itinerary/{destinationId}")
    public String showItinerary(@PathVariable("destinationId") String destinationId,
                                Model model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Destination destination = destinationService.getDestinationById(destinationId);
        if (destination == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Destination not found.");
            return "redirect:/destinations";
        }

        User user = (User) session.getAttribute("sessionUser");
        Double budget = (Double) session.getAttribute("sessionBudget");

        if (user == null) {
            user = new User("Guest Traveler", 24, "Other", "Bengaluru", "");
            user.setNumberOfTravellers(1);
            session.setAttribute("sessionUser", user);
        }
        if (budget == null) {
            budget = destination.getEstimatedCostPerPerson() + 2000;
            session.setAttribute("sessionBudget", budget);
        }

        // Create polymorphic Trip (SoloTrip or GroupTrip)
        Trip currentTrip = tripService.createTrip(user, destination, budget);
        session.setAttribute("activeTrip", currentTrip);

        double totalCost = currentTrip.calculateTotalCost();
        double remainingBudget = currentTrip.calculateRemainingBudget();
        double perPersonCost = currentTrip.getPerPersonCost();

        model.addAttribute("trip", currentTrip);
        model.addAttribute("destination", destination);
        model.addAttribute("user", user);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("remainingBudget", remainingBudget);
        model.addAttribute("perPersonCost", perPersonCost);
        model.addAttribute("itinerary", currentTrip.getItinerary());
        model.addAttribute("isWithinBudget", currentTrip.isWithinBudget());

        return "itinerary";
    }

    // ==========================================
    // 6. ADVANCE PAYMENT MODULE (ENCAPSULATION)
    // ==========================================
    @GetMapping("/payment")
    public String showPaymentPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Trip activeTrip = (Trip) session.getAttribute("activeTrip");
        if (activeTrip == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a destination and generate an itinerary first.");
            return "redirect:/destinations";
        }

        // Initialize encapsulated Payment object via PaymentService abstraction
        Payment payment = paymentService.initiatePayment();
        session.setAttribute("currentPayment", payment);

        double totalCost = activeTrip.calculateTotalCost();
        double advanceAmount = payment.getAdvanceAmount(); // 200.0
        double remainingBalance = Math.max(0, totalCost - advanceAmount);

        // Pre-generate a sample demo transaction id to make testing convenient
        String sampleDemoTxId = paymentService.generateDemoTransactionId();

        model.addAttribute("trip", activeTrip);
        model.addAttribute("payment", payment);
        model.addAttribute("advanceAmount", advanceAmount);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("remainingBalance", remainingBalance);
        model.addAttribute("sampleDemoTxId", sampleDemoTxId);

        return "payment";
    }

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam("transactionId") String transactionId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Trip activeTrip = (Trip) session.getAttribute("activeTrip");
        Payment payment = (Payment) session.getAttribute("currentPayment");

        if (activeTrip == null || payment == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please plan your trip again.");
            return "redirect:/plan";
        }

        // Validate and process payment strictly through Encapsulation & Abstraction
        boolean success = paymentService.processPayment(payment, transactionId);

        if (!success) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid transaction reference. Must be at least 6 alphanumeric characters (e.g. UPI/DEMO/123456).");
            return "redirect:/payment";
        }

        // Payment successful - create confirmed booking
        Booking booking = bookingService.createBooking(activeTrip, payment);

        // Clean up temporary session trip
        session.removeAttribute("activeTrip");
        session.removeAttribute("currentPayment");

        redirectAttributes.addFlashAttribute("successMessage", "Payment of ₹200 received successfully!");
        return "redirect:/confirmation/" + booking.getBookingId();
    }

    // ==========================================
    // 7. BOOKING CONFIRMATION
    // ==========================================
    @GetMapping("/confirmation/{bookingId}")
    public String showConfirmation(@PathVariable("bookingId") String bookingId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking ID not found: " + bookingId);
            return "redirect:/my-booking";
        }

        model.addAttribute("booking", booking);
        return "confirmation";
    }

    // ==========================================
    // 8. MY BOOKINGS / SEARCH
    // ==========================================
    @GetMapping("/my-booking")
    public String showBookings(@RequestParam(value = "searchId", required = false) String searchId, Model model) {
        if (searchId != null && !searchId.trim().isEmpty()) {
            Booking booking = bookingService.getBooking(searchId.trim());
            model.addAttribute("searchedBooking", booking);
            model.addAttribute("searchId", searchId);
            if (booking == null) {
                model.addAttribute("notFoundMessage", "No booking found with ID: " + searchId);
            }
        }
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    // ==========================================
    // 9. ABOUT US & OOP ARCHITECTURE GUIDE
    // ==========================================
    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }
}

