package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.usecase.UserTypeUseCase;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeInteractorIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserTypeUseCase userTypeUseCase;

    @Test
    void shouldCreateUserTypeUsingRealDatabase() {
        UserTypeOutputData output = userTypeUseCase.create(
                new CreateUserTypeInputData("Cliente")
        );

        assertNotNull(output.id());
        assertEquals("Cliente", output.name());
        assertTrue(userTypeRepository.existsById(output.id()));
    }

    @Test
    void shouldFindAllUserTypesUsingRealDatabase() {
        userTypeUseCase.create(new CreateUserTypeInputData("Cliente"));
        userTypeUseCase.create(new CreateUserTypeInputData("Dono de Restaurante"));

        List<UserTypeOutputData> result = userTypeUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateUserTypeUsingRealDatabase() {
        UserTypeOutputData created = userTypeUseCase.create(
                new CreateUserTypeInputData("Cliente")
        );

        UserTypeOutputData updated = userTypeUseCase.update(
                new UpdateUserTypeInputData(created.id(), "Cliente Premium")
        );

        assertEquals(created.id(), updated.id());
        assertEquals("Cliente Premium", updated.name());
    }

    @Test
    void shouldDeleteUserTypeUsingRealDatabase() {
        UserTypeOutputData created = userTypeUseCase.create(
                new CreateUserTypeInputData("Cliente")
        );

        userTypeUseCase.delete(created.id());

        assertFalse(userTypeRepository.existsById(created.id()));
    }

    @Test
    void shouldThrowWhenUserTypeDoesNotExist() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> userTypeUseCase.findById(999L)
        );
    }
}