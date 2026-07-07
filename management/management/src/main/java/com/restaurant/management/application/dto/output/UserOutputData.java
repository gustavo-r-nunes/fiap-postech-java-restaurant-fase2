package com.restaurant.management.application.dto.output;

public record UserOutputData(
        Long id,
        String name,
        String email,
        Long userTypeId,
        String userTypeName
) {
}