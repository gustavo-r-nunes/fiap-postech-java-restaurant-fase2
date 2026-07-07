package com.restaurant.management.infrastructure.config;

import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.UserType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUserTypes(UserTypeGateway userTypeGateway) {
        return args -> {
            createUserTypeIfNotExists(userTypeGateway, "Dono de Restaurante");
            createUserTypeIfNotExists(userTypeGateway, "Cliente");
        };
    }

    private void createUserTypeIfNotExists(UserTypeGateway gateway, String name) {
        if (!gateway.existsByNameIgnoreCase(name)) {
            gateway.save(new UserType(name));
        }
    }
}