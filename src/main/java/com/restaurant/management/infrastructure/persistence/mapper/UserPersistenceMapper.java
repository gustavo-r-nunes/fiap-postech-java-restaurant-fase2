package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.entity.User;
import com.restaurant.management.infrastructure.persistence.entity.UserJpaEntity;

public class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                UserTypePersistenceMapper.toDomain(entity.getUserType())
        );
    }

    public static UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) {
            return null;
        }

        return new UserJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getEmail(),
                UserTypePersistenceMapper.toJpaEntity(domain.getUserType())
        );
    }
}