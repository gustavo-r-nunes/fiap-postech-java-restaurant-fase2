package com.restaurant.management.infrastructure.service;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.application.usecase.RestaurantInteractor;
import com.restaurant.management.application.usecase.RestaurantUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService implements RestaurantUseCase{

    private final RestaurantInteractor interactor;

    public RestaurantService(RestaurantGateway restaurantGateway, UserGateway userGateway){
        this.interactor = new RestaurantInteractor(restaurantGateway, userGateway);
    }

    @Override
    @Transactional
    public RestaurantOutputData create(CreateRestaurantInputData inputData){
        return interactor.create(inputData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantOutputData> findAll() {
        return interactor.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantOutputData findById(Long id) {
        return interactor.findById(id);
    }

    @Override
    @Transactional
    public RestaurantOutputData update(UpdateRestaurantInputData inputData){
        return interactor.update(inputData);
    }

    @Override
    @Transactional
    public void delete(Long id){
        interactor.delete(id);
    }
    
}
