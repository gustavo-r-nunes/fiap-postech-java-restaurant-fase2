package com.restaurant.management.presentation.dto.response;

public record UserResponse(
        Long id,
        String name,
        String email,
        Long userTypeId,
        String userTypeName
) {
}