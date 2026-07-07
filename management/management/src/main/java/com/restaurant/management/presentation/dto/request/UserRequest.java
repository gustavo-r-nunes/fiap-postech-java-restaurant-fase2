package com.restaurant.management.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "O nome do usuário é obrigatório")
        @Size(min = 2, max = 120, message = "O nome do usuário deve ter entre 2 e 120 caracteres")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ter um formato válido")
        @Size(max = 160, message = "O e-mail deve ter no máximo 160 caracteres")
        String email,

        @NotNull(message = "O tipo de usuário é obrigatório")
        @Positive(message = "O ID do tipo de usuário deve ser positivo")
        Long userTypeId
) {
}