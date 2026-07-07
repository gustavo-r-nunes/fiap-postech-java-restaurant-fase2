package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.usecase.RestaurantUseCase;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.infrastructure.config.SecurityConfig;
import com.restaurant.management.presentation.exception.GlobalExceptionHandler;
import com.restaurant.management.presentation.presenter.RestaurantRestPresenter;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        RestaurantController.class,
        RestaurantRestPresenter.class,
        GlobalExceptionHandler.class
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantUseCase restaurantUseCase;

    @Test
    void shouldCreateRestaurant() throws Exception {
        RestaurantOutputData outputData = new RestaurantOutputData(
                1L,
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                10L,
                "João Dono"
        );

        when(restaurantUseCase.create(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Cantina Bella",
                  "address": "Rua das Flores, 123",
                  "cuisineType": "Italiana",
                  "openingHours": "11h às 23h",
                  "ownerId": 10
                }
                """;

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cantina Bella"))
                .andExpect(jsonPath("$.address").value("Rua das Flores, 123"))
                .andExpect(jsonPath("$.cuisineType").value("Italiana"))
                .andExpect(jsonPath("$.openingHours").value("11h às 23h"))
                .andExpect(jsonPath("$.ownerId").value(10))
                .andExpect(jsonPath("$.ownerName").value("João Dono"));

        verify(restaurantUseCase).create(any());
    }

    @Test
    void shouldFindAllRestaurants() throws Exception {
        RestaurantOutputData outputData = new RestaurantOutputData(
                1L,
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                10L,
                "João Dono"
        );

        when(restaurantUseCase.findAll()).thenReturn(List.of(outputData));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cantina Bella"))
                .andExpect(jsonPath("$[0].ownerName").value("João Dono"));

        verify(restaurantUseCase).findAll();
    }

    @Test
    void shouldFindRestaurantById() throws Exception {
        RestaurantOutputData outputData = new RestaurantOutputData(
                1L,
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                10L,
                "João Dono"
        );

        when(restaurantUseCase.findById(1L)).thenReturn(outputData);

        mockMvc.perform(get("/api/restaurants/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cantina Bella"));

        verify(restaurantUseCase).findById(1L);
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        RestaurantOutputData outputData = new RestaurantOutputData(
                1L,
                "Cantina Nova",
                "Rua Nova, 456",
                "Brasileira",
                "10h às 22h",
                10L,
                "João Dono"
        );

        when(restaurantUseCase.update(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Cantina Nova",
                  "address": "Rua Nova, 456",
                  "cuisineType": "Brasileira",
                  "openingHours": "10h às 22h",
                  "ownerId": 10
                }
                """;

        mockMvc.perform(put("/api/restaurants/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cantina Nova"))
                .andExpect(jsonPath("$.cuisineType").value("Brasileira"));

        verify(restaurantUseCase).update(any());
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        mockMvc.perform(delete("/api/restaurants/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(restaurantUseCase).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenCreateRestaurantRequestIsInvalid() throws Exception {
        String request = """
                {
                  "name": "",
                  "address": "Rua das Flores, 123",
                  "cuisineType": "Italiana",
                  "openingHours": "11h às 23h",
                  "ownerId": 10
                }
                """;

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail", containsString("name")));
    }

    @Test
    void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
        when(restaurantUseCase.findById(99L))
                .thenThrow(new ResourceNotFoundException("Restaurante não encontrado"));

        mockMvc.perform(get("/api/restaurants/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Restaurante não encontrado"));
    }

    @Test
    void shouldReturnBadRequestWhenOwnerIsInvalid() throws Exception {
        when(restaurantUseCase.create(any()))
                .thenThrow(new IllegalArgumentException("O usuário informado deve ser do tipo Dono de Restaurante"));

        String request = """
                {
                  "name": "Cantina Bella",
                  "address": "Rua das Flores, 123",
                  "cuisineType": "Italiana",
                  "openingHours": "11h às 23h",
                  "ownerId": 10
                }
                """;

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição inválida"))
                .andExpect(jsonPath("$.detail").value("O usuário informado deve ser do tipo Dono de Restaurante"));
    }
}