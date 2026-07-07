package com.restaurant.management.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserTypeRequest(

        @NotBlank(message = "O nome do tipo de usuário é obrigatório")
        @Size(min = 2, max = 80, message = "O nome do tipo de usuário deve ter entre 2 e 80 caracteres")
        String name
) {
}