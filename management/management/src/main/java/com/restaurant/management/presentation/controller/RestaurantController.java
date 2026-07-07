package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.usecase.RestaurantUseCase;
import com.restaurant.management.presentation.dto.request.RestaurantRequest;
import com.restaurant.management.presentation.dto.response.RestaurantResponse;
import com.restaurant.management.presentation.mapper.RestaurantPresentationMapper;
import com.restaurant.management.presentation.presenter.RestaurantRestPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;
    private final RestaurantRestPresenter presenter;

    public RestaurantController(
            RestaurantUseCase restaurantUseCase,
            RestaurantRestPresenter presenter
    ) {
        this.restaurantUseCase = restaurantUseCase;
        this.presenter = presenter;
    }

    @Operation(summary = "Cadastrar restaurante")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@RequestBody @Valid RestaurantRequest request) {
        CreateRestaurantInputData inputData = RestaurantPresentationMapper.toCreateInputData(request);
        RestaurantOutputData outputData = restaurantUseCase.create(inputData);

        return presenter.present(outputData);
    }

    @Operation(summary = "Listar restaurantes")
    @GetMapping
    public List<RestaurantResponse> findAll() {
        List<RestaurantOutputData> outputData = restaurantUseCase.findAll();

        return presenter.present(outputData);
    }

    @GetMapping("/{id}")
    public RestaurantResponse findById(
            @PathVariable @Positive(message = "O ID do restaurante deve ser positivo") Long id
    ) {
        RestaurantOutputData outputData = restaurantUseCase.findById(id);

        return presenter.present(outputData);
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(
            @PathVariable @Positive(message = "O ID do restaurante deve ser positivo") Long id,
            @RequestBody @Valid RestaurantRequest request
    ) {
        UpdateRestaurantInputData inputData = RestaurantPresentationMapper.toUpdateInputData(id, request);
        RestaurantOutputData outputData = restaurantUseCase.update(inputData);

        return presenter.present(outputData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive(message = "O ID do restaurante deve ser positivo") Long id
    ) {
        restaurantUseCase.delete(id);
    }
}