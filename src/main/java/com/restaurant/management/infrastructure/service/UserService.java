package com.restaurant.management.infrastructure.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.application.usecase.UserInteractor;
import com.restaurant.management.application.usecase.UserUseCase;

@Service
public class UserService implements UserUseCase{

    private final UserInteractor interactor;

    public UserService(UserGateway userGateway, UserTypeGateway userTypeGateway){
        this.interactor = new UserInteractor(userGateway, userTypeGateway);
    }

    @Override
    @Transactional
    public UserOutputData create(CreateUserInputData inputData){
        return interactor.create(inputData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOutputData> findAll() {
        return interactor.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserOutputData findById(Long id) {
        return interactor.findById(id);
    }

    @Override
    @Transactional
    public UserOutputData update(UpdateUserInputData inputData){
        return interactor.update(inputData);
    }

    @Override
    @Transactional
    public void delete(Long id){
        interactor.delete(id);
    }
    
}
