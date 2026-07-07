package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;

import java.util.List;

public interface UserTypeUseCase {

    UserTypeOutputData create(CreateUserTypeInputData inputData);

    List<UserTypeOutputData> findAll();

    UserTypeOutputData findById(Long id);

    UserTypeOutputData update(UpdateUserTypeInputData inputData);

    void delete(Long id);
}