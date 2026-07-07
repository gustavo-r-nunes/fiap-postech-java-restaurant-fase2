package com.restaurant.management.presentation.controller;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.usecase.MenuItemUseCase;
import com.restaurant.management.presentation.dto.request.MenuItemRequest;
import com.restaurant.management.presentation.dto.response.MenuItemResponse;
import com.restaurant.management.presentation.mapper.MenuItemPresentationMapper;
import com.restaurant.management.presentation.presenter.MenuItemRestPresenter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;
    private final MenuItemRestPresenter presenter;

    public MenuItemController(
            MenuItemUseCase menuItemUseCase,
            MenuItemRestPresenter presenter
    ) {
        this.menuItemUseCase = menuItemUseCase;
        this.presenter = presenter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@RequestBody @Valid MenuItemRequest request) {
        CreateMenuItemInputData inputData = MenuItemPresentationMapper.toCreateInputData(request);
        MenuItemOutputData outputData = menuItemUseCase.create(inputData);

        return presenter.present(outputData);
    }

    @GetMapping
    public List<MenuItemResponse> findAll() {
        List<MenuItemOutputData> outputData = menuItemUseCase.findAll();

        return presenter.present(outputData);
    }

    @GetMapping("/{id}")
    public MenuItemResponse findById(
            @PathVariable @Positive(message = "O ID do item do cardápio deve ser positivo") Long id
    ) {
        MenuItemOutputData outputData = menuItemUseCase.findById(id);

        return presenter.present(outputData);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> findByRestaurantId(
            @PathVariable @Positive(message = "O ID do restaurante deve ser positivo") Long restaurantId
    ) {
        List<MenuItemOutputData> outputData = menuItemUseCase.findByRestaurantId(restaurantId);

        return presenter.present(outputData);
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(
            @PathVariable @Positive(message = "O ID do item do cardápio deve ser positivo") Long id,
            @RequestBody @Valid MenuItemRequest request
    ) {
        UpdateMenuItemInputData inputData = MenuItemPresentationMapper.toUpdateInputData(id, request);
        MenuItemOutputData outputData = menuItemUseCase.update(inputData);

        return presenter.present(outputData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive(message = "O ID do item do cardápio deve ser positivo") Long id
    ) {
        menuItemUseCase.delete(id);
    }
}