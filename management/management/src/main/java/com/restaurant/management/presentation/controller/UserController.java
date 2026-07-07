package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.usecase.UserUseCase;
import com.restaurant.management.presentation.dto.request.UserRequest;
import com.restaurant.management.presentation.dto.response.UserResponse;
import com.restaurant.management.presentation.mapper.UserPresentationMapper;
import com.restaurant.management.presentation.presenter.UserRestPresenter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserRestPresenter presenter;

    public UserController(
            UserUseCase userUseCase,
            UserRestPresenter presenter
    ) {
        this.userUseCase = userUseCase;
        this.presenter = presenter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRequest request) {
        CreateUserInputData inputData = UserPresentationMapper.toCreateInputData(request);
        UserOutputData outputData = userUseCase.create(inputData);

        return presenter.present(outputData);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        List<UserOutputData> outputData = userUseCase.findAll();

        return presenter.present(outputData);
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable @Positive(message = "O ID do usuário deve ser positivo") Long id) {
        UserOutputData outputData = userUseCase.findById(id);
        return presenter.present(outputData);
    }

    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable @Positive(message = "O ID do usuário deve ser positivo") Long id,
            @RequestBody @Valid UserRequest request
    ) {
        UpdateUserInputData inputData = UserPresentationMapper.toUpdateInputData(id, request);
        UserOutputData outputData = userUseCase.update(inputData);

        return presenter.present(outputData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = "O ID do usuário deve ser positivo") Long id) {
        userUseCase.delete(id);
    }


}