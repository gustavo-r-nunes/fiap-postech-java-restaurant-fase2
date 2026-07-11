package com.restaurant.management.infrastructure.service;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.usecase.UserTypeInteractor;
import com.restaurant.management.application.usecase.UserTypeUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTypeService implements UserTypeUseCase {
    private final UserTypeInteractor interactor;

    public UserTypeService(UserTypeInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    @Transactional public UserTypeOutputData create(CreateUserTypeInputData inputData) {
        return interactor.create(inputData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTypeOutputData> findAll() {
        return interactor.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserTypeOutputData findById(Long id) {
        return interactor.findById(id);
    }

    @Override
    @Transactional
    public UserTypeOutputData update(UpdateUserTypeInputData inputData){
        return interactor.update(inputData);
    }

    @Override
    @Transactional
    public void delete(Long id){
        interactor.delete(id);
    }

    
}
