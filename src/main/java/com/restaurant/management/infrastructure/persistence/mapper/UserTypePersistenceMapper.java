package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.infrastructure.persistence.entity.UserTypeJpaEntity;

public class UserTypePersistenceMapper {

    private UserTypePersistenceMapper() {
    }

    public static UserType toDomain(UserTypeJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserType(
                entity.getId(),
                entity.getName()
        );
    }

    public static UserTypeJpaEntity toJpaEntity(UserType domain) {
        if (domain == null) {
            return null;
        }

        return new UserTypeJpaEntity(
                domain.getId(),
                domain.getName()
        );
    }
}