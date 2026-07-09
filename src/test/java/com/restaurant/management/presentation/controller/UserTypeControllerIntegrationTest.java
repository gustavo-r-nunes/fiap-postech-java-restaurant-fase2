package com.restaurant.management.integration.controller;

import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeControllerIntegrationTest extends AbstractIntegrationTest {

    UserTypeControllerIntegrationTest(
            MenuItemJpaRepository menuItemRepository,
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeJpaRepository userTypeRepository
    ) {
        super(menuItemRepository, restaurantRepository, userRepository, userTypeRepository);
    }

    @Test
    void shouldCreateUserType() {
        Map<?, ?> response = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Cliente"))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertNotNull(response.get("id"));
        assertEquals("Cliente", response.get("name"));
    }

    @Test
    void shouldFindAllUserTypes() {
        restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Cliente"))
                .retrieve()
                .body(Map.class);

        List<?> response = restClient.get()
                .uri("/api/user-types")
                .retrieve()
                .body(List.class);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void shouldFindUserTypeById() {
        Map<?, ?> created = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Dono de Restaurante"))
                .retrieve()
                .body(Map.class);

        Integer id = (Integer) created.get("id");

        Map<?, ?> response = restClient.get()
                .uri("/api/user-types/{id}", id)
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(id, response.get("id"));
        assertEquals("Dono de Restaurante", response.get("name"));
    }

    @Test
    void shouldUpdateUserType() {
        Map<?, ?> created = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Cliente"))
                .retrieve()
                .body(Map.class);

        Integer id = (Integer) created.get("id");

        Map<?, ?> response = restClient.put()
                .uri("/api/user-types/{id}", id)
                .body(Map.of("name", "Cliente Premium"))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(id, response.get("id"));
        assertEquals("Cliente Premium", response.get("name"));
    }

    @Test
    void shouldDeleteUserType() {
        Map<?, ?> created = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Cliente"))
                .retrieve()
                .body(Map.class);

        Integer id = (Integer) created.get("id");

        restClient.delete()
                .uri("/api/user-types/{id}", id)
                .retrieve()
                .toBodilessEntity();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/user-types/{id}", id)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnBadRequestWhenUserTypeNameIsBlank() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/user-types")
                        .body(Map.of("name", ""))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(400, exception.getStatusCode().value());
    }
}