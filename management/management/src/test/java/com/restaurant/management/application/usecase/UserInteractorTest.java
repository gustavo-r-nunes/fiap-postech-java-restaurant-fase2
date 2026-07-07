package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.entity.UserType;
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
class UserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserTypeGateway userTypeGateway;

    @InjectMocks
    private UserInteractor userInteractor;

    @Test
    void shouldCreateUserWhenUserTypeExists() {
        UserType userType = new UserType("Cliente");

        CreateUserInputData inputData = new CreateUserInputData(
                "Maria",
                "maria@email.com",
                1L
        );

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));
        when(userGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserOutputData result = userInteractor.create(inputData);

        assertEquals("Maria", result.name());
        assertEquals("maria@email.com", result.email());
        assertEquals("Cliente", result.userTypeName());

        verify(userTypeGateway).findById(1L);
        verify(userGateway).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUserTypeNotFoundOnCreate() {
        CreateUserInputData inputData = new CreateUserInputData(
                "Maria",
                "maria@email.com",
                99L
        );

        when(userTypeGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userInteractor.create(inputData));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void shouldFindUserById() {
        UserType userType = new UserType("Cliente");
        User user = new User("Maria", "maria@email.com", userType);

        when(userGateway.findById(1L)).thenReturn(Optional.of(user));

        UserOutputData result = userInteractor.findById(1L);

        assertEquals("Maria", result.name());
        assertEquals("Cliente", result.userTypeName());

        verify(userGateway).findById(1L);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userInteractor.findById(99L));
    }

    @Test
    void shouldFindAllUsers() {
        UserType userType = new UserType("Cliente");
        User user = new User("Maria", "maria@email.com", userType);

        when(userGateway.findAll()).thenReturn(List.of(user));

        List<UserOutputData> result = userInteractor.findAll();

        assertEquals(1, result.size());
        assertEquals("Maria", result.getFirst().name());

        verify(userGateway).findAll();
    }

    @Test
    void shouldUpdateUserWhenUserAndTypeExist() {
        UserType oldType = new UserType("Cliente");
        UserType newType = new UserType("Dono de Restaurante");
        User user = new User("Maria", "maria@email.com", oldType);

        UpdateUserInputData inputData = new UpdateUserInputData(
                1L,
                "Maria Atualizada",
                "maria.nova@email.com",
                2L
        );

        when(userGateway.findById(1L)).thenReturn(Optional.of(user));
        when(userTypeGateway.findById(2L)).thenReturn(Optional.of(newType));
        when(userGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserOutputData result = userInteractor.update(inputData);

        assertEquals("Maria Atualizada", result.name());
        assertEquals("maria.nova@email.com", result.email());
        assertEquals("Dono de Restaurante", result.userTypeName());

        verify(userGateway).save(user);
    }

    @Test
    void shouldDeleteUser() {
        UserType userType = new UserType("Cliente");
        User user = new User("Maria", "maria@email.com", userType);

        when(userGateway.findById(1L)).thenReturn(Optional.of(user));

        userInteractor.delete(1L);

        verify(userGateway).findById(1L);
        verify(userGateway).deleteById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeletingNonExistingUser() {
        when(userGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userInteractor.delete(99L));

        verify(userGateway).findById(99L);
        verify(userGateway, never()).deleteById(anyLong());
    }
}