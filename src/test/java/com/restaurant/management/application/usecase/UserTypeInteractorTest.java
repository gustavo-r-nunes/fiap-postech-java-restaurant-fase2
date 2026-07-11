package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.domain.exception.BusinessRuleException;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeInteractorTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @InjectMocks
    private UserTypeInteractor userTypeInteractor;

    @Test
    void shouldCreateUserTypeWhenNameDoesNotExist() {
        CreateUserTypeInputData inputData = new CreateUserTypeInputData("Cliente");

        when(userTypeGateway.existsByNameIgnoreCase("Cliente")).thenReturn(false);
        when(userTypeGateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTypeOutputData result = userTypeInteractor.create(inputData);

        assertEquals("Cliente", result.name());

        verify(userTypeGateway).existsByNameIgnoreCase("Cliente");
        verify(userTypeGateway).save(any(UserType.class));
    }

    @Test
    void shouldNotCreateUserTypeWhenNameAlreadyExists() {
        CreateUserTypeInputData inputData = new CreateUserTypeInputData("Cliente");

        when(userTypeGateway.existsByNameIgnoreCase("Cliente")).thenReturn(true);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> userTypeInteractor.create(inputData)
        );

        assertEquals("Já existe um tipo de usuário com esse nome", exception.getMessage());

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void shouldFindUserTypeById() {
        UserType userType = new UserType("Cliente");

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));

        UserTypeOutputData result = userTypeInteractor.findById(1L);

        assertEquals("Cliente", result.name());

        verify(userTypeGateway).findById(1L);
    }

    @Test
    void shouldThrowWhenUserTypeNotFound() {
        when(userTypeGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeInteractor.findById(99L));
    }

    @Test
    void shouldFindAllUserTypes() {
        UserType userType = new UserType("Cliente");

        when(userTypeGateway.findAll()).thenReturn(List.of(userType));

        List<UserTypeOutputData> result = userTypeInteractor.findAll();

        assertEquals(1, result.size());
        assertEquals("Cliente", result.get(0).name());

        verify(userTypeGateway).findAll();
    }

    @Test
    void shouldUpdateUserTypeWhenNameIsAvailable() {
        UserType userType = new UserType("Cliente");

        UpdateUserTypeInputData inputData = new UpdateUserTypeInputData(
                1L,
                "Dono de Restaurante"
        );

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));
        when(userTypeGateway.findByNameIgnoreCase("Dono de Restaurante")).thenReturn(Optional.empty());
        when(userTypeGateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTypeOutputData result = userTypeInteractor.update(inputData);

        assertEquals("Dono de Restaurante", result.name());

        verify(userTypeGateway).save(userType);
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = new UserType("Cliente");

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));

        userTypeInteractor.delete(1L);

        verify(userTypeGateway).findById(1L);
        verify(userTypeGateway).deleteById(1L);
    }
}