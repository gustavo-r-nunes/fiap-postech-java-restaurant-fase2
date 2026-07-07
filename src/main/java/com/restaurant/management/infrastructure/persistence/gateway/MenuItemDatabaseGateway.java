package com.restaurant.management.infrastructure.persistence.gateway;

import com.restaurant.management.application.gateway.MenuItemGateway;
import com.restaurant.management.domain.entity.MenuItem;
import com.restaurant.management.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemDatabaseGateway implements MenuItemGateway {

    private final MenuItemJpaRepository repository;
    private final RestaurantJpaRepository restaurantRepository;

    public MenuItemDatabaseGateway(
            MenuItemJpaRepository repository,
            RestaurantJpaRepository restaurantRepository
    ) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        var entity = MenuItemPersistenceMapper.toJpaEntity(menuItem);

        if (menuItem.getRestaurant() != null && menuItem.getRestaurant().getId() != null) {
            entity.setRestaurant(restaurantRepository.getReferenceById(menuItem.getRestaurant().getId()));
        }

        var savedEntity = repository.save(entity);
        return MenuItemPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id)
                .map(MenuItemPersistenceMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAll() {
        return repository.findAll()
                .stream()
                .map(MenuItemPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return repository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(MenuItemPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}