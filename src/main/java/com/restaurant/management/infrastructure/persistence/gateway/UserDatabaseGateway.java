package com.restaurant.management.infrastructure.persistence.gateway;

import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDatabaseGateway implements UserGateway {

    private final UserJpaRepository repository;
    private final UserTypeJpaRepository userTypeRepository;

    public UserDatabaseGateway(
            UserJpaRepository repository,
            UserTypeJpaRepository userTypeRepository
    ) {
        this.repository = repository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User save(User user) {
        var entity = UserPersistenceMapper.toJpaEntity(user);

        if (user.getUserType() != null && user.getUserType().getId() != null) {
            entity.setUserType(userTypeRepository.getReferenceById(user.getUserType().getId()));
        }

        var savedEntity = repository.save(entity);
        return UserPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}