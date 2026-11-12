package com.tripmate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates DATA ENCAPSULATION:
 * All fields are private and only accessible via validated getter and setter methods.
 */
public class User {

    private String fullName;
    private int age;
    private String gender;
    private String startingCity;
    private String contactNumber;

    // Group specific attributes
    private String groupName;
    private int numberOfTravellers = 1;
    private List<String> memberNames = new ArrayList<>();
    private List<Integer> memberAges = new ArrayList<>();

    public User() {
    }

    public User(String fullName, int age, String gender, String startingCity, String contactNumber) {
        setFullName(fullName);
        setAge(age);
        setGender(gender);
        setStartingCity(startingCity);
        setContactNumber(contactNumber);
    }

    // Encapsulated getters & setters with validation

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 1 || age > 120) {
            throw new IllegalArgumentException("Please enter a valid age between 1 and 120.");
        }
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            this.gender = "Not Specified";
        } else {
            this.gender = gender.trim();
        }
    }

    public String getStartingCity() {
        return startingCity;
    }

    public void setStartingCity(String startingCity) {
        if (startingCity == null || startingCity.trim().isEmpty()) {
            throw new IllegalArgumentException("Starting city cannot be empty.");
        }
        this.startingCity = startingCity.trim();
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = (contactNumber != null) ? contactNumber.trim() : "";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = (groupName != null) ? groupName.trim() : "";
    }

    public int getNumberOfTravellers() {
        return numberOfTravellers;
    }

    public void setNumberOfTravellers(int numberOfTravellers) {
        if (numberOfTravellers < 1) {
            throw new IllegalArgumentException("Number of travellers must be at least 1.");
        }
        this.numberOfTravellers = numberOfTravellers;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(List<String> memberNames) {
        this.memberNames = (memberNames != null) ? memberNames : new ArrayList<>();
    }

    public List<Integer> getMemberAges() {
        return memberAges;
    }

    public void setMemberAges(List<Integer> memberAges) {
        this.memberAges = (memberAges != null) ? memberAges : new ArrayList<>();
    }

    public boolean isGroup() {
        return numberOfTravellers > 1;
    }
}

