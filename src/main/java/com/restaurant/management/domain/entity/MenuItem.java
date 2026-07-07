package com.restaurant.management.domain.entity;

import java.math.BigDecimal;

public class MenuItem {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean onlyAvailableInRestaurant;
    private String photoPath;
    private Restaurant restaurant;

    public MenuItem(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean onlyAvailableInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.onlyAvailableInRestaurant = onlyAvailableInRestaurant;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
    }

    public MenuItem(
            String name,
            String description,
            BigDecimal price,
            boolean onlyAvailableInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        this(null, name, description, price, onlyAvailableInRestaurant, photoPath, restaurant);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isOnlyAvailableInRestaurant() {
        return onlyAvailableInRestaurant;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOnlyAvailableInRestaurant(boolean onlyAvailableInRestaurant) {
        this.onlyAvailableInRestaurant = onlyAvailableInRestaurant;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}