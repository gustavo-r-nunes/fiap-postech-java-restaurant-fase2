package com.restaurant.management.application.dto.input;

public record UpdateUserInputData(
        Long id,
        String name,
        String email,
        Long userTypeId
) {
}