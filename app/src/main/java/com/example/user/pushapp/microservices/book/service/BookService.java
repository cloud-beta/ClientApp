package com.example.user.pushapp.microservices.book.service;

import com.example.user.pushapp.microservices.book.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookService {

    @GET("/book/")
    Call<List<Book>> listBooks(@Query("phone") int phone);

    @Headers("Accept: application/json")
    @POST("/book/")
    Call<Void> bookRestaurant(@Body Book book);

}
