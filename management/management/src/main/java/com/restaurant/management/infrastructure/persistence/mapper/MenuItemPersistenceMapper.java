package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.entity.MenuItem;
import com.restaurant.management.infrastructure.persistence.entity.MenuItemJpaEntity;

public class MenuItemPersistenceMapper {

    private MenuItemPersistenceMapper() {
    }

    public static MenuItem toDomain(MenuItemJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MenuItem(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isOnlyAvailableInRestaurant(),
                entity.getPhotoPath(),
                RestaurantPersistenceMapper.toDomain(entity.getRestaurant())
        );
    }

    public static MenuItemJpaEntity toJpaEntity(MenuItem domain) {
        if (domain == null) {
            return null;
        }

        return new MenuItemJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPrice(),
                domain.isOnlyAvailableInRestaurant(),
                domain.getPhotoPath(),
                RestaurantPersistenceMapper.toJpaEntity(domain.getRestaurant())
        );
    }
}