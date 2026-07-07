package com.restaurant.management.application.dto.input;

public record CreateUserInputData(
        String name,
        String email,
        Long userTypeId
) {
}