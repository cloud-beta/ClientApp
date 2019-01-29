package com.example.user.pushapp.microservices.book;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookServiceRetrofitClient {
    public static final String BASE_URL = "http://10.0.0.10:8081/";

    private static final class RetrofitClientHolder{
        private static final BookServiceRetrofitClient retrofitClient = new BookServiceRetrofitClient();
    }

    public static BookServiceRetrofitClient getInstance(){
        return RetrofitClientHolder.retrofitClient;
    }

    private Retrofit retrofit;

    private BookServiceRetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class <? extends T> type){
        return retrofit.create(type);
    }
}
