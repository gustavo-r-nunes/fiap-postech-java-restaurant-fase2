package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.infrastructure.persistence.entity.RestaurantJpaEntity;

public class RestaurantPersistenceMapper {

    private RestaurantPersistenceMapper() {
    }

    public static Restaurant toDomain(RestaurantJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType(),
                entity.getOpeningHours(),
                UserPersistenceMapper.toDomain(entity.getOwner())
        );
    }

    public static RestaurantJpaEntity toJpaEntity(Restaurant domain) {
        if (domain == null) {
            return null;
        }

        return new RestaurantJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getAddress(),
                domain.getCuisineType(),
                domain.getOpeningHours(),
                UserPersistenceMapper.toJpaEntity(domain.getOwner())
        );
    }
}