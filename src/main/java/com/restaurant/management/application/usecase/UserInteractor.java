package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.UpdateUserInputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.domain.exception.ResourceNotFoundException;

import java.util.List;

public class UserInteractor implements UserUseCase {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public UserInteractor(
            UserGateway userGateway,
            UserTypeGateway userTypeGateway
    ) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public UserOutputData create(CreateUserInputData inputData) {
        UserType userType = userTypeGateway.findById(inputData.userTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado"));

        User user = new User(
                inputData.name(),
                inputData.email(),
                userType
        );

        return toOutputData(userGateway.save(user));
    }

    @Override
    public List<UserOutputData> findAll() {
        return userGateway.findAll()
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    public UserOutputData findById(Long id) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return toOutputData(user);
    }

    @Override
    public UserOutputData update(UpdateUserInputData inputData) {
        User user = userGateway.findById(inputData.id())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        UserType userType = userTypeGateway.findById(inputData.userTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado"));

        user.setName(inputData.name());
        user.setEmail(inputData.email());
        user.setUserType(userType);

        return toOutputData(userGateway.save(user));
    }

    @Override
    public void delete(Long id) {
        userGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        userGateway.deleteById(id);
    }

    private UserOutputData toOutputData(User user) {
        return new UserOutputData(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType().getId(),
                user.getUserType().getName()
        );
    }
}