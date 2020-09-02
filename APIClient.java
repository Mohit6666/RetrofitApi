package com.firebaseconfigration.demo15_07_2020mohit.controller.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    public static String BASE_URL = "https://quickeats.co.uk/Customer/";

    public static Retrofit getClientMethod(Context context) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientOne = new OkHttpClient.Builder();
        httpClientOne.addInterceptor(logging);

        httpClientOne.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
               // Log.d("@@@@@",""+new SharedPref(context).getString(Constant.AUTH_TOKEN));
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type","application/json")
                        .addHeader("X-API-KEY","bSWKIdfE6K9NDeE5cFTZ6c5898YVVb3ePGE")
                        .addHeader("Authorization","Basic WVdSdGFXND06TVRJek5BPT0=")
                    //    .addHeader("Authorization", new SharedPref(context).getString(Constant.AUTH_TOKEN))

                        .build();
                return chain.proceed(request);

            }
        });
        httpClientOne.connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientOne.build())
                .build();
        return retrofit;


    }
}
