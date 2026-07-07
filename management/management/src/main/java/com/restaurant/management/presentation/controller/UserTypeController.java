package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.usecase.UserTypeUseCase;
import com.restaurant.management.presentation.dto.request.UserTypeRequest;
import com.restaurant.management.presentation.dto.response.UserTypeResponse;
import com.restaurant.management.presentation.mapper.UserTypePresentationMapper;
import com.restaurant.management.presentation.presenter.UserTypeRestPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Tipos de usuário", description = "Endpoints para gerenciamento dos tipos de usuário")
@RestController
@RequestMapping("/api/user-types")
public class UserTypeController {

    private final UserTypeUseCase userTypeUseCase;
    private final UserTypeRestPresenter presenter;

    public UserTypeController(
            UserTypeUseCase userTypeUseCase,
            UserTypeRestPresenter presenter
    ) {
        this.userTypeUseCase = userTypeUseCase;
        this.presenter = presenter;
    }

    @Operation(summary = "Cadastrar tipo de usuário")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTypeResponse create(@RequestBody @Valid UserTypeRequest request) {
        CreateUserTypeInputData inputData = UserTypePresentationMapper.toCreateInputData(request);
        UserTypeOutputData outputData = userTypeUseCase.create(inputData);

        return presenter.present(outputData);
    }

    @Operation(summary = "Listar tipos de usuário")
    @GetMapping
    public List<UserTypeResponse> findAll() {
        List<UserTypeOutputData> outputData = userTypeUseCase.findAll();

        return presenter.present(outputData);
    }

    @GetMapping("/{id}")
    public UserTypeResponse findById(
            @PathVariable @Positive(message = "O ID do tipo de usuário deve ser positivo") Long id
    ) {
        UserTypeOutputData outputData = userTypeUseCase.findById(id);

        return presenter.present(outputData);
    }

    @PutMapping("/{id}")
    public UserTypeResponse update(
            @PathVariable @Positive(message = "O ID do tipo de usuário deve ser positivo") Long id,
            @RequestBody @Valid UserTypeRequest request
    ) {
        UpdateUserTypeInputData inputData = UserTypePresentationMapper.toUpdateInputData(id, request);
        UserTypeOutputData outputData = userTypeUseCase.update(inputData);

        return presenter.present(outputData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive(message = "O ID do tipo de usuário deve ser positivo") Long id
    ) {
        userTypeUseCase.delete(id);
    }
}