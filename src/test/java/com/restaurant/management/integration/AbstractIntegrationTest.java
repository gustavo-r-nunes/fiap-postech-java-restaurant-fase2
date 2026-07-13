package com.restaurant.management.integration;

import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @LocalServerPort
    protected int port;

    protected RestClient restClient;

    @Autowired
    protected MenuItemJpaRepository menuItemRepository;

    @Autowired
    protected RestaurantJpaRepository restaurantRepository;

    @Autowired
    protected UserJpaRepository userRepository;

    @Autowired
    protected UserTypeJpaRepository userTypeRepository;

    @BeforeEach
    void setUpIntegrationTest() {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        userTypeRepository.deleteAll();

        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }
}