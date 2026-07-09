package com.restaurant.management.presentation.controller;

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

class UserControllerIntegrationTest extends AbstractIntegrationTest {

    UserControllerIntegrationTest(
            MenuItemJpaRepository menuItemRepository,
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeJpaRepository userTypeRepository
    ) {
        super(menuItemRepository, restaurantRepository, userRepository, userTypeRepository);
    }

    @Test
    void shouldCreateUser() {
        Integer userTypeId = createUserType("Cliente");

        Map<?, ?> response = restClient.post()
                .uri("/api/users")
                .body(Map.of(
                        "name", "Maria Cliente",
                        "email", "maria.cliente@email.com",
                        "userTypeId", userTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertNotNull(response.get("id"));
        assertEquals("Maria Cliente", response.get("name"));
        assertEquals("maria.cliente@email.com", response.get("email"));
        assertEquals(userTypeId, response.get("userTypeId"));
        assertEquals("Cliente", response.get("userTypeName"));
    }

    @Test
    void shouldFindAllUsers() {
        Integer userTypeId = createUserType("Cliente");
        createUser("Maria Cliente", "maria.cliente@email.com", userTypeId);

        List<?> response = restClient.get()
                .uri("/api/users")
                .retrieve()
                .body(List.class);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void shouldFindUserById() {
        Integer userTypeId = createUserType("Cliente");
        Integer userId = createUser("Maria Cliente", "maria.cliente@email.com", userTypeId);

        Map<?, ?> response = restClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(userId, response.get("id"));
        assertEquals("Maria Cliente", response.get("name"));
        assertEquals("maria.cliente@email.com", response.get("email"));
        assertEquals(userTypeId, response.get("userTypeId"));
        assertEquals("Cliente", response.get("userTypeName"));
    }

    @Test
    void shouldUpdateUser() {
        Integer clientTypeId = createUserType("Cliente");
        Integer ownerTypeId = createUserType("Dono de Restaurante");
        Integer userId = createUser("Maria Cliente", "maria.cliente@email.com", clientTypeId);

        Map<?, ?> response = restClient.put()
                .uri("/api/users/{id}", userId)
                .body(Map.of(
                        "name", "Maria Atualizada",
                        "email", "maria.atualizada@email.com",
                        "userTypeId", ownerTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(userId, response.get("id"));
        assertEquals("Maria Atualizada", response.get("name"));
        assertEquals("maria.atualizada@email.com", response.get("email"));
        assertEquals(ownerTypeId, response.get("userTypeId"));
        assertEquals("Dono de Restaurante", response.get("userTypeName"));
    }

    @Test
    void shouldDeleteUser() {
        Integer userTypeId = createUserType("Cliente");
        Integer userId = createUser("Maria Cliente", "maria.cliente@email.com", userTypeId);

        restClient.delete()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .toBodilessEntity();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/users/{id}", userId)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() {
        Integer userTypeId = createUserType("Cliente");

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/users")
                        .body(Map.of(
                                "name", "Maria Cliente",
                                "email", "email-invalido",
                                "userTypeId", userTypeId
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenUserTypeDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/users")
                        .body(Map.of(
                                "name", "Maria Cliente",
                                "email", "maria.cliente@email.com",
                                "userTypeId", 999
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/users/{id}", 999)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    private Integer createUserType(String name) {
        Map<?, ?> userType = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", name))
                .retrieve()
                .body(Map.class);

        assertNotNull(userType);
        return (Integer) userType.get("id");
    }

    private Integer createUser(String name, String email, Integer userTypeId) {
        Map<?, ?> user = restClient.post()
                .uri("/api/users")
                .body(Map.of(
                        "name", name,
                        "email", email,
                        "userTypeId", userTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(user);
        return (Integer) user.get("id");
    }
}