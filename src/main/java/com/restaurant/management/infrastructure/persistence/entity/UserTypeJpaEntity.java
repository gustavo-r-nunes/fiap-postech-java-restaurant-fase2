package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_types")
public class UserTypeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public UserTypeJpaEntity() {
    }

    public UserTypeJpaEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserTypeJpaEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}