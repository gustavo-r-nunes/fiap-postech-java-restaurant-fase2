package com.restaurant.management.presentation.mapper;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.presentation.dto.request.UserTypeRequest;

public class UserTypePresentationMapper {

    private UserTypePresentationMapper() {
    }

    public static CreateUserTypeInputData toCreateInputData(UserTypeRequest request) {
        return new CreateUserTypeInputData(
                request.name()
        );
    }

    public static UpdateUserTypeInputData toUpdateInputData(Long id, UserTypeRequest request) {
        return new UpdateUserTypeInputData(
                id,
                request.name()
        );
    }
}