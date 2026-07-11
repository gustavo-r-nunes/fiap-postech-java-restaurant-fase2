package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateUserTypeInputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.domain.exception.BusinessRuleException;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTypeInteractor implements UserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public UserTypeInteractor(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    @Transactional
    public UserTypeOutputData create(CreateUserTypeInputData inputData) {
        if (userTypeGateway.existsByNameIgnoreCase(inputData.name())) {
            throw new BusinessRuleException("Já existe um tipo de usuário com esse nome");
        }

        UserType userType = new UserType(inputData.name());

        return toOutputData(userTypeGateway.save(userType));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTypeOutputData> findAll() {
        return userTypeGateway.findAll()
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserTypeOutputData findById(Long id) {
        UserType userType = userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado"));

        return toOutputData(userType);
    }

    @Override
    @Transactional
    public UserTypeOutputData update(UpdateUserTypeInputData inputData) {
        UserType userType = userTypeGateway.findById(inputData.id())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado"));

        userTypeGateway.findByNameIgnoreCase(inputData.name())
                .filter(existing -> !existing.getId().equals(inputData.id()))
                .ifPresent(existing -> {
                    throw new BusinessRuleException("Já existe um tipo de usuário com esse nome");
                });

        userType.setName(inputData.name());

        return toOutputData(userTypeGateway.save(userType));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado"));

        userTypeGateway.deleteById(id);
    }

    private UserTypeOutputData toOutputData(UserType userType) {
        return new UserTypeOutputData(
                userType.getId(),
                userType.getName()
        );
    }
}