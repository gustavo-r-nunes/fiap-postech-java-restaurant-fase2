package com.restaurant.management.presentation.presenter;

import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRestPresenter {

    public UserResponse present(UserOutputData outputData) {
        return new UserResponse(
                outputData.id(),
                outputData.name(),
                outputData.email(),
                outputData.userTypeId(),
                outputData.userTypeName()
        );
    }

    public List<UserResponse> present(List<UserOutputData> outputDataList) {
        return outputDataList.stream()
                .map(this::present)
                .toList();
    }
}