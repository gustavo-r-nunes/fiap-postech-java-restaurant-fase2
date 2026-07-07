package com.restaurant.management.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(

        @NotBlank(message = "O nome do restaurante é obrigatório")
        @Size(min = 2, max = 120, message = "O nome do restaurante deve ter entre 2 e 120 caracteres")
        String name,

        @NotBlank(message = "O endereço do restaurante é obrigatório")
        @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
        String address,

        @NotBlank(message = "O tipo de cozinha é obrigatório")
        @Size(min = 2, max = 80, message = "O tipo de cozinha deve ter entre 2 e 80 caracteres")
        String cuisineType,

        @NotBlank(message = "O horário de funcionamento é obrigatório")
        @Size(min = 3, max = 120, message = "O horário de funcionamento deve ter entre 3 e 120 caracteres")
        String openingHours,

        @NotNull(message = "O proprietário do restaurante é obrigatório")
        @Positive(message = "O ID do proprietário deve ser positivo")
        Long ownerId
) {
}