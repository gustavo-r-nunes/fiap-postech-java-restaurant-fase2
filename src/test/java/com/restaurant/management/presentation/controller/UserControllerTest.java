package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.usecase.UserUseCase;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.infrastructure.config.SecurityConfig;
import com.restaurant.management.presentation.exception.GlobalExceptionHandler;
import com.restaurant.management.presentation.presenter.UserRestPresenter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({
        UserController.class,
        UserRestPresenter.class,
        GlobalExceptionHandler.class
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUseCase userUseCase;

    @Test
    void shouldCreateUser() throws Exception {
        UserOutputData outputData = new UserOutputData(
                1L,
                "Maria",
                "maria@email.com",
                2L,
                "Cliente"
        );

        when(userUseCase.create(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Maria",
                  "email": "maria@email.com",
                  "userTypeId": 2
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@email.com"))
                .andExpect(jsonPath("$.userTypeId").value(2))
                .andExpect(jsonPath("$.userTypeName").value("Cliente"));

        verify(userUseCase).create(any());
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        UserOutputData outputData = new UserOutputData(
                1L,
                "Maria",
                "maria@email.com",
                2L,
                "Cliente"
        );

        when(userUseCase.findAll()).thenReturn(List.of(outputData));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Maria"))
                .andExpect(jsonPath("$[0].userTypeName").value("Cliente"));

        verify(userUseCase).findAll();
    }

    @Test
    void shouldFindUserById() throws Exception {
        UserOutputData outputData = new UserOutputData(
                1L,
                "Maria",
                "maria@email.com",
                2L,
                "Cliente"
        );

        when(userUseCase.findById(1L)).thenReturn(outputData);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"));

        verify(userUseCase).findById(1L);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserOutputData outputData = new UserOutputData(
                1L,
                "Maria Atualizada",
                "maria.nova@email.com",
                2L,
                "Cliente"
        );

        when(userUseCase.update(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Maria Atualizada",
                  "email": "maria.nova@email.com",
                  "userTypeId": 2
                }
                """;

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria Atualizada"))
                .andExpect(jsonPath("$.email").value("maria.nova@email.com"));

        verify(userUseCase).update(any());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userUseCase).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String request = """
                {
                  "name": "Maria",
                  "email": "email-invalido",
                  "userTypeId": 2
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail", containsString("email")));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userUseCase.findById(99L))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/api/users/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Usuário não encontrado"));
    }
}