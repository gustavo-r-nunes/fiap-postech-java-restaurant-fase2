package com.restaurant.management.presentation.mapper;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.presentation.dto.request.UserRequest;

public class UserPresentationMapper {

    private UserPresentationMapper() {
    }

    public static CreateUserInputData toCreateInputData(UserRequest request) {
        return new CreateUserInputData(
                request.name(),
                request.email(),
                request.userTypeId()
        );
    }

    public static UpdateUserInputData toUpdateInputData(Long id, UserRequest request) {
        return new UpdateUserInputData(
                id,
                request.name(),
                request.email(),
                request.userTypeId()
        );
    }
}