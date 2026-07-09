package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInteractorIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private UserTypeUseCase userTypeUseCase;

    UserInteractorIntegrationTest(
            MenuItemJpaRepository menuItemRepository,
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeJpaRepository userTypeRepository
    ) {
        super(menuItemRepository, restaurantRepository, userRepository, userTypeRepository);
    }

    @Test
    void shouldCreateUserUsingRealDatabase() {
        UserTypeOutputData userType = createUserType("Cliente");

        UserOutputData output = userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        userType.id()
                )
        );

        assertNotNull(output.id());
        assertEquals("Maria Cliente", output.name());
        assertEquals("maria.cliente@email.com", output.email());
        assertEquals(userType.id(), output.userTypeId());
        assertEquals("Cliente", output.userTypeName());
        assertTrue(userRepository.existsById(output.id()));
    }

    @Test
    void shouldFindAllUsersUsingRealDatabase() {
        UserTypeOutputData userType = createUserType("Cliente");

        userUseCase.create(new CreateUserInputData(
                "Maria Cliente",
                "maria.cliente@email.com",
                userType.id()
        ));

        userUseCase.create(new CreateUserInputData(
                "José Cliente",
                "jose.cliente@email.com",
                userType.id()
        ));

        List<UserOutputData> result = userUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindUserByIdUsingRealDatabase() {
        UserTypeOutputData userType = createUserType("Cliente");

        UserOutputData created = userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        userType.id()
                )
        );

        UserOutputData found = userUseCase.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Maria Cliente", found.name());
        assertEquals("maria.cliente@email.com", found.email());
        assertEquals("Cliente", found.userTypeName());
    }

    @Test
    void shouldUpdateUserUsingRealDatabase() {
        UserTypeOutputData clientType = createUserType("Cliente");
        UserTypeOutputData ownerType = createUserType("Dono de Restaurante");

        UserOutputData created = userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        clientType.id()
                )
        );

        UserOutputData updated = userUseCase.update(
                new UpdateUserInputData(
                        created.id(),
                        "Maria Atualizada",
                        "maria.atualizada@email.com",
                        ownerType.id()
                )
        );

        assertEquals(created.id(), updated.id());
        assertEquals("Maria Atualizada", updated.name());
        assertEquals("maria.atualizada@email.com", updated.email());
        assertEquals(ownerType.id(), updated.userTypeId());
        assertEquals("Dono de Restaurante", updated.userTypeName());
    }

    @Test
    void shouldDeleteUserUsingRealDatabase() {
        UserTypeOutputData userType = createUserType("Cliente");

        UserOutputData created = userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        userType.id()
                )
        );

        userUseCase.delete(created.id());

        assertFalse(userRepository.existsById(created.id()));
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userUseCase.findById(999L)
        );
    }

    @Test
    void shouldThrowWhenCreatingUserWithNonExistingUserType() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userUseCase.create(
                        new CreateUserInputData(
                                "Maria Cliente",
                                "maria.cliente@email.com",
                                999L
                        )
                )
        );
    }

    @Test
    void shouldThrowWhenUpdatingUserWithNonExistingUserType() {
        UserTypeOutputData userType = createUserType("Cliente");

        UserOutputData created = userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        userType.id()
                )
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> userUseCase.update(
                        new UpdateUserInputData(
                                created.id(),
                                "Maria Atualizada",
                                "maria.atualizada@email.com",
                                999L
                        )
                )
        );
    }

    private UserTypeOutputData createUserType(String name) {
        return userTypeUseCase.create(new CreateUserTypeInputData(name));
    }
}