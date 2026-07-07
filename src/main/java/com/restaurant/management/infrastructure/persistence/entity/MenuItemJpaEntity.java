package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean onlyAvailableInRestaurant;

    private String photoPath;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantJpaEntity restaurant;

    public MenuItemJpaEntity() {
    }

    public MenuItemJpaEntity(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean onlyAvailableInRestaurant,
            String photoPath,
            RestaurantJpaEntity restaurant
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.onlyAvailableInRestaurant = onlyAvailableInRestaurant;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
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

    public RestaurantJpaEntity getRestaurant() {
        return restaurant;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setRestaurant(RestaurantJpaEntity restaurant) {
        this.restaurant = restaurant;
    }
}