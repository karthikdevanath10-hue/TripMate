package com.tripmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TripMate - Smart Trip Planner
 * College Mini-Project demonstrating Java OOP Concepts:
 * - Data Encapsulation
 * - Inheritance
 * - Polymorphism
 * - Abstraction
 */
@SpringBootApplication
public class TripMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripMateApplication.class, args);
        System.out.println("=================================================");
        System.out.println("TripMate – Smart Trip Planner is running!");
        System.out.println("Open your browser at: http://localhost:8080");
        System.out.println("=================================================");
    }
}

