package com.restaurant.management.presentation.presenter;

import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.presentation.dto.response.UserTypeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTypeRestPresenter {

    public UserTypeResponse present(UserTypeOutputData outputData) {
        return new UserTypeResponse(
                outputData.id(),
                outputData.name()
        );
    }

    public List<UserTypeResponse> present(List<UserTypeOutputData> outputDataList) {
        return outputDataList.stream()
                .map(this::present)
                .toList();
    }
}