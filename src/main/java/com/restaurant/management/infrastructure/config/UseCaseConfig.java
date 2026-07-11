package com.restaurant.management.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.restaurant.management.application.gateway.MenuItemGateway;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.application.gateway.UserTypeGateway;
import com.restaurant.management.application.usecase.MenuItemInteractor;
import com.restaurant.management.application.usecase.RestaurantInteractor;
import com.restaurant.management.application.usecase.UserInteractor;
import com.restaurant.management.application.usecase.UserTypeInteractor;

@Configuration
public class UseCaseConfig {

    @Bean
    public UserTypeInteractor userTypeInteractor(UserTypeGateway gateway){
        return new UserTypeInteractor(gateway);
    }

    @Bean
    public UserInteractor userInteractor(UserGateway usergateway, UserTypeGateway userTypeGateway){
        return new UserInteractor(usergateway, userTypeGateway);
    }

    @Bean
    public RestaurantInteractor restaurantInteractor(RestaurantGateway restaurantGateway, UserGateway userGateway){
        return new RestaurantInteractor(restaurantGateway, userGateway);
    }

    @Bean
    public MenuItemInteractor menuItemInteractor(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway){
        return new MenuItemInteractor(menuItemGateway, restaurantGateway);
    }
    
}
