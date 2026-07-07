package com.restaurant.management.domain.entity;

public class Restaurant {

    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private User owner;

    public Restaurant(
            Long id,
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.owner = owner;
    }

    public Restaurant(
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        this(null, name, address, cuisineType, openingHours, owner);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public User getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}