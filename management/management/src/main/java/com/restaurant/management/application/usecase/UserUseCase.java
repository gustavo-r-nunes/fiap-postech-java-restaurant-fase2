package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;

import java.util.List;

public interface UserUseCase {

    UserOutputData create(CreateUserInputData inputData);

    List<UserOutputData> findAll();

    UserOutputData findById(Long id);

    UserOutputData update(UpdateUserInputData inputData);

    void delete(Long id);
}