package com.restaurant.management.presentation.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(

        @NotBlank(message = "O nome do item é obrigatório")
        @Size(min = 2, max = 120, message = "O nome do item deve ter entre 2 e 120 caracteres")
        String name,

        @NotBlank(message = "A descrição do item é obrigatória")
        @Size(min = 5, max = 1000, message = "A descrição deve ter entre 5 e 1000 caracteres")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        @DecimalMax(value = "999999.99", message = "O preço deve ser menor ou igual a 999999.99")
        BigDecimal price,

        boolean onlyAvailableInRestaurant,

        @Size(max = 255, message = "O caminho da foto deve ter no máximo 255 caracteres")
        @Pattern(
                regexp = "^(|/.*|https?://.*)$",
                message = "O caminho da foto deve ser vazio, começar com '/' ou ser uma URL http/https"
        )
        String photoPath,

        @NotNull(message = "O restaurante é obrigatório")
        @Positive(message = "O ID do restaurante deve ser positivo")
        Long restaurantId
) {
}