package com.restaurant.management.infrastructure.persistence.gateway;

import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantDatabaseGateway implements RestaurantGateway {

    private final RestaurantJpaRepository repository;
    private final UserJpaRepository userRepository;

    public RestaurantDatabaseGateway(
            RestaurantJpaRepository repository,
            UserJpaRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        var entity = RestaurantPersistenceMapper.toJpaEntity(restaurant);

        if (restaurant.getOwner() != null && restaurant.getOwner().getId() != null) {
            entity.setOwner(userRepository.getReferenceById(restaurant.getOwner().getId()));
        }

        var savedEntity = repository.save(entity);
        return RestaurantPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id)
                .map(RestaurantPersistenceMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll()
                .stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}