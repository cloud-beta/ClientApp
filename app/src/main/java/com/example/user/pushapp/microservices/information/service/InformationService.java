package com.example.user.pushapp.microservices.information.service;

import com.example.user.pushapp.microservices.information.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InformationService {

    @GET("/information/places/")
    Call<List<Restaurant>> listRestaurant();

}
