# ✈️ TripMate – Smart Trip Planner
> **Tagline:** Plan Your Trip, Set Your Budget, Explore Your World.  
> **Type:** Java + Spring Boot Object-Oriented Programming (OOP) College Mini-Project  
> **Key Focus:** **Data Encapsulation**, Inheritance, Polymorphism, and Abstraction.

---

## 🌟 1. Project Overview
**TripMate** is a modern, responsive travel planning web application built with **Java 21/25**, **Spring Boot 3**, **Spring MVC**, and **Thymeleaf**. 

Instead of showing static or unaffordable travel packages, TripMate analyzes the traveler's party size (Solo vs Group) and exact budget in Indian Rupees (₹), runs Java budget-matching logic, recommends suitable Indian destinations, generates day-by-day itineraries, and collects a demo **₹200 advance booking payment** using safe **Data Encapsulation**.

---

## 🧭 2. Complete Website Workflow
1. **Home Page (`/`)**: Hero section, "Plan Your Trip" CTA, popular destination cards, process explanation, and feature highlights.
2. **Trip Type Selection (`/plan`)**: Choose between **Solo Trip** (1 traveler) and **Group Trip** (multiple travelers with shared costs).
3. **User Details & Budget Entry (`/user-details`)**: Collects traveler details (Name, Age, Gender, Starting City) and total travel budget (INR) with quick select chips (₹2,000 to ₹50,000). For group trips, dynamically manages member names and ages.
4. **Destination Recommendations (`/destinations`)**: Preloaded catalog of 15 Indian destinations filtered strictly by budget using Java comparison logic. Includes search and budget filters.
5. **Day-Wise Itinerary (`/itinerary/{destinationId}`)**: Detailed morning/afternoon/evening schedule, places to visit, and complete expense breakdown (Travel, Stay, Food, Local Transport).
6. **Advance Payment Module (`/payment`)**: Showcases **Data Encapsulation** in the `Payment` class. Displays booking summary, ₹200 fixed advance, demo QR code, and demo transaction reference verification.
7. **Booking Confirmation (`/confirmation/{bookingId}`)**: Confirmed booking voucher with unique reference ID (`TM-2026-XXXX`), financial ledger, and print/download button.
8. **My Bookings (`/my-booking`)**: Search booking by ID or list all stored bookings (stored in memory and persisted to `data/bookings.csv`).
9. **OOP Guide & Viva Reference (`/about`)**: Complete interactive guide explaining the 4 OOP concepts with code snippets for evaluator viva.

---

## 🛡️ 3. Object-Oriented Programming (OOP) Concepts

### A. Data Encapsulation (Primary Demonstration)
* **`com.tripmate.model.Payment`**:
  * Fields are strictly **`private`**: `paymentId`, `advanceAmount` (₹200), `paymentStatus`, `transactionId`, `paymentDate`.
  * **No public setters** exist for `paymentStatus` or `advanceAmount`. External classes cannot arbitrarily mutate payment state.
  * State transitions only occur through the validated public method:
    ```java
    public boolean processPayment(String transactionId) {
        if (transactionId != null && transactionId.trim().length() >= 6) {
            this.transactionId = transactionId.trim();
            this.paymentStatus = "SUCCESS";
            this.paymentDate = LocalDateTime.now();
            return true;
        }
        this.paymentStatus = "FAILED";
        return false;
    }
    ```
  * Safe public getters provide read-only access.
* **`User`**, **`Destination`**, **`Trip`**, and **`Booking`** all feature private members with range and validation checks in setters (e.g., age between 1 and 120, non-empty names).

### B. Inheritance
* **`Trip` (Abstract Parent Class)**: Defines common trip properties (`tripId`, `user`, `destination`, `totalBudget`, `itinerary`) and common operations (`calculateRemainingBudget()`).
* **`SoloTrip extends Trip`**: Represents an individual traveler.
* **`GroupTrip extends Trip`**: Represents group travel with shared room and transit dynamics.

### C. Polymorphism (Dynamic Method Dispatch)
* Abstract method `public abstract double calculateTotalCost();` in `Trip`.
* **`SoloTrip`**: Calculates cost as `destination.getEstimatedCostPerPerson()`.
* **`GroupTrip`**: Implements group sharing savings:
  * Hotel rooms: Shared double occupancy `Math.ceil(travellers / 2.0)`.
  * Local transport: Scaled vehicle factor rather than multiplying individual fares.
* Invoked polymorphically:
  ```java
  Trip trip = tripService.createTrip(user, destination, budget);
  double total = trip.calculateTotalCost(); // Dynamic dispatch at runtime
  ```

### D. Abstraction
* **Interface `PaymentService`**: Defines the contract for payment processing (`initiatePayment()`, `validateTransactionReference()`, `processPayment()`).
* **Implementation `DemoPaymentService`**: Provides demo UPI simulation for the project while allowing real gateways (Razorpay/Stripe) to be swapped in without changing controller code.
* **Abstract class `Trip`**: Hides common trip management behind an abstract interface.

---

## 📂 4. Project Structure
```
c:\java mini\
├── pom.xml
├── mvn.cmd
├── README.md
├── data\
│   └── bookings.csv
├── src\main\java\com\tripmate\
│   ├── TripMateApplication.java
│   ├── model\
│   │   ├── User.java
│   │   ├── Trip.java (Abstract)
│   │   ├── SoloTrip.java
│   │   ├── GroupTrip.java
│   │   ├── Destination.java
│   │   ├── ItineraryDay.java
│   │   ├── Payment.java (Data Encapsulation Showcase)
│   │   └── Booking.java
│   ├── service\
│   │   ├── PaymentService.java (Interface)
│   │   ├── DemoPaymentService.java
│   │   ├── DestinationService.java
│   │   ├── TripService.java
│   │   └── BookingService.java
│   └── controller\
│       └── TripController.java
└── src\main\resources\
    ├── application.properties
    ├── static\
    │   ├── css\style.css
    │   └── js\script.js
    └── templates\
        ├── fragments.html
        ├── index.html
        ├── trip-selection.html
        ├── user-details.html
        ├── destinations.html
        ├── itinerary.html
        ├── payment.html
        ├── confirmation.html
        ├── bookings.html
        └── about.html
```

---

## 🚀 5. How to Run the Application

### Option 1: Using the Maven Wrapper (Recommended)
Open a terminal in `C:\java mini` and run:
```cmd
.\mvn.cmd spring-boot:run
```

### Option 2: Using the Packaged JAR
```cmd
java -jar target\tripmate-planner-1.0.0.jar
```

### Option 3: From Any IDE (IntelliJ IDEA / Eclipse / Antigravity IDE)
1. Open folder `C:\java mini` as a Maven project.
2. Run `com.tripmate.TripMateApplication.java`.

### Open in Browser:
```
http://localhost:8080
```

---

## 🎯 6. Viva / Evaluation Talking Points
1. **How Encapsulation is Proven:** Open [Payment.java](file:///C:/java%20mini/src/main/java/com/tripmate/model/Payment.java). Show your teacher that `paymentStatus` is `private` and has NO setter. Show how `processPayment()` validates the transaction ID before updating status.
2. **How Polymorphism is Proven:** Open [SoloTrip.java](file:///C:/java%20mini/src/main/java/com/tripmate/model/SoloTrip.java) and [GroupTrip.java](file:///C:/java%20mini/src/main/java/com/tripmate/model/GroupTrip.java). Both override `calculateTotalCost()`. Notice how `GroupTrip` shares hotel rooms among 2 people each (`Math.ceil(travellers / 2.0)`).
3. **No Database Dependency:** Data is managed in-memory with `HashMap` and `ArrayList`, with automatic persistence to `data/bookings.csv`.

