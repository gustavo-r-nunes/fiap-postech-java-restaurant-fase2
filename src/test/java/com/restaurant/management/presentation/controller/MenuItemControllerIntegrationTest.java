package com.restaurant.management.presentation.controller;

import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldCreateMenuItem() {
        Integer restaurantId = createRestaurant();

        Map<?, ?> response = restClient.post()
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

        assertNotNull(response);
        assertNotNull(response.get("id"));
        assertEquals("Pizza Margherita", response.get("name"));
        assertEquals("Pizza com molho de tomate, mussarela e manjericão", response.get("description"));
        assertEquals(false, response.get("onlyAvailableInRestaurant"));
        assertEquals("/images/pizza-margherita.jpg", response.get("photoPath"));
        assertEquals(restaurantId, response.get("restaurantId"));
        assertEquals("Cantina Bella", response.get("restaurantName"));
    }

    @Test
    void shouldFindAllMenuItems() {
        Integer restaurantId = createRestaurant();
        createMenuItem(restaurantId);

        List<?> response = restClient.get()
                .uri("/api/menu-items")
                .retrieve()
                .body(List.class);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void shouldFindMenuItemById() {
        Integer restaurantId = createRestaurant();
        Integer menuItemId = createMenuItem(restaurantId);

        Map<?, ?> response = restClient.get()
                .uri("/api/menu-items/{id}", menuItemId)
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(menuItemId, response.get("id"));
        assertEquals("Pizza Margherita", response.get("name"));
        assertEquals(restaurantId, response.get("restaurantId"));
        assertEquals("Cantina Bella", response.get("restaurantName"));
    }

    @Test
    void shouldFindMenuItemsByRestaurantId() {
        Integer restaurantId = createRestaurant();
        createMenuItem(restaurantId);

        List<?> response = restClient.get()
                .uri("/api/menu-items/restaurant/{restaurantId}", restaurantId)
                .retrieve()
                .body(List.class);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void shouldUpdateMenuItem() {
        Integer restaurantId = createRestaurant();
        Integer menuItemId = createMenuItem(restaurantId);

        Map<?, ?> response = restClient.put()
                .uri("/api/menu-items/{id}", menuItemId)
                .body(Map.of(
                        "name", "Pizza Calabresa",
                        "description", "Pizza com calabresa, cebola e mussarela",
                        "price", new BigDecimal("59.90"),
                        "onlyAvailableInRestaurant", true,
                        "photoPath", "/images/pizza-calabresa.jpg",
                        "restaurantId", restaurantId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(menuItemId, response.get("id"));
        assertEquals("Pizza Calabresa", response.get("name"));
        assertEquals("Pizza com calabresa, cebola e mussarela", response.get("description"));
        assertEquals(true, response.get("onlyAvailableInRestaurant"));
        assertEquals("/images/pizza-calabresa.jpg", response.get("photoPath"));
        assertEquals(restaurantId, response.get("restaurantId"));
    }

    @Test
    void shouldDeleteMenuItem() {
        Integer restaurantId = createRestaurant();
        Integer menuItemId = createMenuItem(restaurantId);

        restClient.delete()
                .uri("/api/menu-items/{id}", menuItemId)
                .retrieve()
                .toBodilessEntity();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/menu-items/{id}", menuItemId)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenRestaurantDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
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
                        .toBodilessEntity()
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsInvalid() {
        Integer restaurantId = createRestaurant();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/menu-items")
                        .body(Map.of(
                                "name", "Pizza Margherita",
                                "description", "Pizza com molho de tomate, mussarela e manjericão",
                                "price", BigDecimal.ZERO,
                                "onlyAvailableInRestaurant", false,
                                "photoPath", "/images/pizza-margherita.jpg",
                                "restaurantId", restaurantId
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/menu-items/{id}", 999)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    private Integer createRestaurant() {
        Integer ownerTypeId = createUserType("Dono de Restaurante");
        Integer ownerId = createUser("João Dono", "joao.dono@email.com", ownerTypeId);

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
        return (Integer) restaurant.get("id");
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

    private Integer createMenuItem(Integer restaurantId) {
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
        return (Integer) menuItem.get("id");
    }
}