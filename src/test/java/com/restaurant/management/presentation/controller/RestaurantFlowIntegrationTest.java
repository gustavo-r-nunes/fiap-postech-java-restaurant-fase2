package com.restaurant.management.presentation.controller;

import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;

import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RestaurantFlowIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @Autowired
    private MenuItemJpaRepository menuItemRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private UserTypeJpaRepository userTypeRepository;

    @BeforeEach
    void setUp() {
        menuItemRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
        userRepository.deleteAll();
        userTypeRepository.deleteAll();

        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateCompleteRestaurantFlow() {
        Map<?, ?> userType = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Dono de Restaurante"))
                .retrieve()
                .body(Map.class);

        assertNotNull(userType);
        Integer userTypeId = (Integer) userType.get("id");

        Map<?, ?> user = restClient.post()
                .uri("/api/users")
                .body(Map.of(
                        "name", "João Dono",
                        "email", "joao.dono@email.com",
                        "userTypeId", userTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(user);
        Integer ownerId = (Integer) user.get("id");

        Map<?, ?> restaurant = restClient.post()
                .uri("/api/restaurants")
                .body(Map.of(
                        "name", "Cantina Bella",
                        "address", "Rua das Flores, 123",
                        "cuisineType", "Italiana",
                        "openingHours", "11h às 23h",
                        "ownerId", ownerId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(restaurant);
        Integer restaurantId = (Integer) restaurant.get("id");

        Map<?, ?> menuItem = restClient.post()
                .uri("/api/menu-items")
                .body(Map.of(
                        "name", "Pizza Margherita",
                        "description", "Pizza com molho de tomate, mussarela e manjericão",
                        "price", new BigDecimal("49.90"),
                        "onlyAvailableInRestaurant", false,
                        "photoPath", "/images/pizza-margherita.jpg",
                        "restaurantId", restaurantId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(menuItem);
        assertEquals("Pizza Margherita", menuItem.get("name"));
        assertEquals("Pizza Margherita", menuItem.get("name"));
        assertEquals("Pizza com molho de tomate, mussarela e manjericão", menuItem.get("description"));
        assertEquals(false, menuItem.get("onlyAvailableInRestaurant"));
        assertEquals("/images/pizza-margherita.jpg", menuItem.get("photoPath"));
        assertEquals(restaurantId, menuItem.get("restaurantId"));
        assertEquals("Cantina Bella", menuItem.get("restaurantName"));
    }

    @Test
    void shouldNotCreateRestaurantWhenUserIsClient() {
        Map<?, ?> userType = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", "Cliente"))
                .retrieve()
                .body(Map.class);

        assertNotNull(userType);
        Integer userTypeId = (Integer) userType.get("id");

        Map<?, ?> user = restClient.post()
                .uri("/api/users")
                .body(Map.of(
                        "name", "Maria Cliente",
                        "email", "maria.cliente@email.com",
                        "userTypeId", userTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(user);
        Integer ownerId = (Integer) user.get("id");

        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> restClient.post()
                .uri("/api/restaurants")
                .body(Map.of(
                        "name", "Restaurante Indevido",
                        "address", "Rua Teste, 123",
                        "cuisineType", "Brasileira",
                        "openingHours", "10h às 22h",
                        "ownerId", ownerId
                ))
                .retrieve()
                .toBodilessEntity());

        assertEquals(400, exception.getStatusCode().value());

        assertNotNull(exception);
    }

    @Test
    void shouldNotCreateMenuItemWhenRestaurantDoesNotExist() {
        RestClientResponseException exception = assertThrows(RestClientResponseException.class, () -> restClient.post()
                .uri("/api/menu-items")
                .body(Map.of(
                        "name", "Pizza Margherita",
                        "description", "Pizza com molho de tomate, mussarela e manjericão",
                        "price", new BigDecimal("49.90"),
                        "onlyAvailableInRestaurant", false,
                        "photoPath", "/images/pizza-margherita.jpg",
                        "restaurantId", 999
                ))
                .retrieve()
                .toBodilessEntity());

        assertEquals(404, exception.getStatusCode().value());
    }
}