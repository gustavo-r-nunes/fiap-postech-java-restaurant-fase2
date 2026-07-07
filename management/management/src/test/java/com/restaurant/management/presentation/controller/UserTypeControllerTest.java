package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.usecase.UserTypeUseCase;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.infrastructure.config.SecurityConfig;
import com.restaurant.management.presentation.exception.GlobalExceptionHandler;
import com.restaurant.management.presentation.presenter.UserTypeRestPresenter;
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
        UserTypeController.class,
        UserTypeRestPresenter.class,
        GlobalExceptionHandler.class
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserTypeUseCase userTypeUseCase;

    @Test
    void shouldCreateUserType() throws Exception {
        UserTypeOutputData outputData = new UserTypeOutputData(
                1L,
                "Cliente"
        );

        when(userTypeUseCase.create(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Cliente"
                }
                """;

        mockMvc.perform(post("/api/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cliente"));

        verify(userTypeUseCase).create(any());
    }

    @Test
    void shouldFindAllUserTypes() throws Exception {
        UserTypeOutputData outputData = new UserTypeOutputData(
                1L,
                "Cliente"
        );

        when(userTypeUseCase.findAll()).thenReturn(List.of(outputData));

        mockMvc.perform(get("/api/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cliente"));

        verify(userTypeUseCase).findAll();
    }

    @Test
    void shouldFindUserTypeById() throws Exception {
        UserTypeOutputData outputData = new UserTypeOutputData(
                1L,
                "Cliente"
        );

        when(userTypeUseCase.findById(1L)).thenReturn(outputData);

        mockMvc.perform(get("/api/user-types/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cliente"));

        verify(userTypeUseCase).findById(1L);
    }

    @Test
    void shouldUpdateUserType() throws Exception {
        UserTypeOutputData outputData = new UserTypeOutputData(
                1L,
                "Dono de Restaurante"
        );

        when(userTypeUseCase.update(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Dono de Restaurante"
                }
                """;

        mockMvc.perform(put("/api/user-types/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dono de Restaurante"));

        verify(userTypeUseCase).update(any());
    }

    @Test
    void shouldDeleteUserType() throws Exception {
        mockMvc.perform(delete("/api/user-types/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userTypeUseCase).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String request = """
                {
                  "name": ""
                }
                """;

        mockMvc.perform(post("/api/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail", containsString("name")));
    }

    @Test
    void shouldReturnBadRequestWhenUserTypeAlreadyExists() throws Exception {
        when(userTypeUseCase.create(any()))
                .thenThrow(new IllegalArgumentException("Já existe um tipo de usuário com esse nome"));

        String request = """
                {
                  "name": "Cliente"
                }
                """;

        mockMvc.perform(post("/api/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição inválida"))
                .andExpect(jsonPath("$.detail").value("Já existe um tipo de usuário com esse nome"));
    }

    @Test
    void shouldReturnNotFoundWhenUserTypeDoesNotExist() throws Exception {
        when(userTypeUseCase.findById(99L))
                .thenThrow(new ResourceNotFoundException("Tipo de usuário não encontrado"));

        mockMvc.perform(get("/api/user-types/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Tipo de usuário não encontrado"));
    }
}