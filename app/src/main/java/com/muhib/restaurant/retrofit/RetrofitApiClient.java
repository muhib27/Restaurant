package com.muhib.restaurant.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.muhib.restaurant.utils.MySheardPreference;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by RR on 27-Dec-17.
 */

public class RetrofitApiClient {
//    www.champs21.com/wp-json/wp/v2/

    //public static final String BASE_URL = "http://champs21.com/wp-json/wp/v2/";
    //public static final String BASE_URL = "http://woocom.endix.net/";
    //public static final String BASE_URL = " https://api.themoviedb.org/3/";
    public static final String BASE_URL = MySheardPreference.getUserSiteUrl()+ "/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitApiClient() {} // So that nobody can create an object with constructor



    String defaultSiteUrl = "http://woocom.endix.net/";
    String defaultKey = "ck_119af3964b19a5d9b4ccbc435b428ab8a91c6b18";
    String defaultSecret = "cs_681801f5d8fe6f94e39fb2c15f88253cc50f63f3";

    public static synchronized Retrofit getClient(String apiKey, String secret) {
        OAuthInterceptor oAuthInterceptor = new OAuthInterceptor(apiKey, secret);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(oAuthInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
//        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
//        }
        return retrofit;
    }

    public static ApiInterface getApiInterface(String apiKey, String secret){
        return  RetrofitApiClient.getClient(apiKey, secret).create(ApiInterface.class);
    }


    public static ApiInterface getLoginApiInterface(String userId, String password){
        return  RetrofitApiClient.loginClient(userId, password).create(ApiInterface.class);
    }

    public static synchronized Retrofit loginClient(String userId, String password) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(userId, password))
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

//    RandomAPIRequestA service = new Retrofit.Builder()
//            .baseUrl("URL goes here")
//            .client(client)
//            .build()
//            .create(RandomAPIRequestA);
}

