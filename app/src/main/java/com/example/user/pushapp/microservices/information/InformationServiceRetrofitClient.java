package com.example.user.pushapp.microservices.information;

import com.example.user.pushapp.constant.ServerUrls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InformationServiceRetrofitClient {


    private static final class RetrofitClientHolder{
        private static final InformationServiceRetrofitClient retrofitClient = new InformationServiceRetrofitClient();
    }

    public static InformationServiceRetrofitClient getInstance(){
        return RetrofitClientHolder.retrofitClient;
    }

    private Retrofit retrofit;

    private InformationServiceRetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.INFORMATION_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class <? extends T> type){
        return retrofit.create(type);
    }
}
