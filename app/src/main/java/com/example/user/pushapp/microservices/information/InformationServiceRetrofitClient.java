package com.example.user.pushapp.microservices.information;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InformationServiceRetrofitClient {
    public static final String BASE_URL = "http://10.0.0.10:8080/";

    private static final class RetrofitClientHolder{
        private static final InformationServiceRetrofitClient retrofitClient = new InformationServiceRetrofitClient();
    }

    public static InformationServiceRetrofitClient getInstance(){
        return RetrofitClientHolder.retrofitClient;
    }

    private Retrofit retrofit;

    private InformationServiceRetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class <? extends T> type){
        return retrofit.create(type);
    }
}
