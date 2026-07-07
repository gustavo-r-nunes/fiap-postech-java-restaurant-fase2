package com.restaurant.management.infrastructure.persistence.gateway;

import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserTypeDatabaseGateway implements UserTypeGateway {

    private final UserTypeJpaRepository repository;

    public UserTypeDatabaseGateway(UserTypeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserType save(UserType userType) {
        var entity = UserTypePersistenceMapper.toJpaEntity(userType);
        var savedEntity = repository.save(entity);
        return UserTypePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return repository.findById(id)
                .map(UserTypePersistenceMapper::toDomain);
    }

    @Override
    public Optional<UserType> findByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name)
                .map(UserTypePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll()
                .stream()
                .map(UserTypePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}