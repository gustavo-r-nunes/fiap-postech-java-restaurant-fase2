package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class RestaurantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String cuisineType;

    @Column(nullable = false)
    private String openingHours;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserJpaEntity owner;

    public RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(
            Long id,
            String name,
            String address,
            String cuisineType,
            String openingHours,
            UserJpaEntity owner
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.owner = owner;
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

    public UserJpaEntity getOwner() {
        return owner;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setOwner(UserJpaEntity owner) {
        this.owner = owner;
    }
}