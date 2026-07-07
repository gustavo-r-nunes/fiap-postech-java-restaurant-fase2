package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.usecase.MenuItemUseCase;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.presentation.exception.GlobalExceptionHandler;
import com.restaurant.management.presentation.presenter.MenuItemRestPresenter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        MenuItemController.class,
        MenuItemRestPresenter.class,
        GlobalExceptionHandler.class
})
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuItemUseCase menuItemUseCase;

    @Test
    void shouldCreateMenuItem() throws Exception {
        MenuItemOutputData outputData = new MenuItemOutputData(
                1L,
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                10L,
                "Cantina Bella"
        );

        when(menuItemUseCase.create(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Pizza Margherita",
                  "description": "Pizza com molho de tomate, mussarela e manjericão",
                  "price": 49.90,
                  "onlyAvailableInRestaurant": false,
                  "photoPath": "/images/pizza-margherita.jpg",
                  "restaurantId": 10
                }
                """;

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza Margherita"))
                .andExpect(jsonPath("$.description").value("Pizza com molho de tomate, mussarela e manjericão"))
                .andExpect(jsonPath("$.price").value(49.90))
                .andExpect(jsonPath("$.onlyAvailableInRestaurant").value(false))
                .andExpect(jsonPath("$.photoPath").value("/images/pizza-margherita.jpg"))
                .andExpect(jsonPath("$.restaurantId").value(10))
                .andExpect(jsonPath("$.restaurantName").value("Cantina Bella"));

        verify(menuItemUseCase).create(any());
    }

    @Test
    void shouldFindAllMenuItems() throws Exception {
        MenuItemOutputData outputData = new MenuItemOutputData(
                1L,
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                10L,
                "Cantina Bella"
        );

        when(menuItemUseCase.findAll()).thenReturn(List.of(outputData));

        mockMvc.perform(get("/api/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza Margherita"))
                .andExpect(jsonPath("$[0].restaurantName").value("Cantina Bella"));

        verify(menuItemUseCase).findAll();
    }

    @Test
    void shouldFindMenuItemById() throws Exception {
        MenuItemOutputData outputData = new MenuItemOutputData(
                1L,
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                10L,
                "Cantina Bella"
        );

        when(menuItemUseCase.findById(1L)).thenReturn(outputData);

        mockMvc.perform(get("/api/menu-items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza Margherita"));

        verify(menuItemUseCase).findById(1L);
    }

    @Test
    void shouldFindMenuItemsByRestaurantId() throws Exception {
        MenuItemOutputData outputData = new MenuItemOutputData(
                1L,
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                10L,
                "Cantina Bella"
        );

        when(menuItemUseCase.findByRestaurantId(10L)).thenReturn(List.of(outputData));

        mockMvc.perform(get("/api/menu-items/restaurant/{restaurantId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].restaurantId").value(10))
                .andExpect(jsonPath("$[0].restaurantName").value("Cantina Bella"));

        verify(menuItemUseCase).findByRestaurantId(10L);
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {
        MenuItemOutputData outputData = new MenuItemOutputData(
                1L,
                "Pizza Nova",
                "Descrição nova",
                new BigDecimal("59.90"),
                true,
                "/images/pizza-nova.jpg",
                10L,
                "Cantina Bella"
        );

        when(menuItemUseCase.update(any())).thenReturn(outputData);

        String request = """
                {
                  "name": "Pizza Nova",
                  "description": "Descrição nova",
                  "price": 59.90,
                  "onlyAvailableInRestaurant": true,
                  "photoPath": "/images/pizza-nova.jpg",
                  "restaurantId": 10
                }
                """;

        mockMvc.perform(put("/api/menu-items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza Nova"))
                .andExpect(jsonPath("$.price").value(59.90))
                .andExpect(jsonPath("$.onlyAvailableInRestaurant").value(true));

        verify(menuItemUseCase).update(any());
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        mockMvc.perform(delete("/api/menu-items/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(menuItemUseCase).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String request = """
                {
                  "name": "",
                  "description": "Pizza com molho de tomate, mussarela e manjericão",
                  "price": 49.90,
                  "onlyAvailableInRestaurant": false,
                  "photoPath": "/images/pizza-margherita.jpg",
                  "restaurantId": 10
                }
                """;

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail", containsString("name")));
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsInvalid() throws Exception {
        String request = """
                {
                  "name": "Pizza Margherita",
                  "description": "Pizza com molho de tomate, mussarela e manjericão",
                  "price": 0,
                  "onlyAvailableInRestaurant": false,
                  "photoPath": "/images/pizza-margherita.jpg",
                  "restaurantId": 10
                }
                """;

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail", containsString("price")));
    }

    @Test
    void shouldReturnNotFoundWhenMenuItemDoesNotExist() throws Exception {
        when(menuItemUseCase.findById(99L))
                .thenThrow(new ResourceNotFoundException("Item do cardápio não encontrado"));

        mockMvc.perform(get("/api/menu-items/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Item do cardápio não encontrado"));
    }
}