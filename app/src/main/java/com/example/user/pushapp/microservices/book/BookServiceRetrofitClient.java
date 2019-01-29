package com.example.user.pushapp.microservices.book;

import com.example.user.pushapp.constant.ServerUrls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookServiceRetrofitClient {


    private static final class RetrofitClientHolder{
        private static final BookServiceRetrofitClient retrofitClient = new BookServiceRetrofitClient();
    }

    public static BookServiceRetrofitClient getInstance(){
        return RetrofitClientHolder.retrofitClient;
    }

    private Retrofit retrofit;

    private BookServiceRetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.BOOK_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T create(Class <? extends T> type){
        return retrofit.create(type);
    }
}
