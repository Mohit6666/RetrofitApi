package com.firebaseconfigration.demo15_07_2020mohit.controller.api;


import com.firebaseconfigration.demo15_07_2020mohit.model.login.MainResponse;
import com.firebaseconfigration.demo15_07_2020mohit.model.registor.BodyParametersResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("registration")
    Call<BodyParametersResponse> registorUser(@Field("firstName") String firstName,
                                              @Field("lastName") String lastName,
                                              @Field("email") String email,
                                              @Field("password") String password,
                                              @Field("countryCode") String countryCode,
                                              @Field("phoneNumber") String phoneNumber,
                                              @Field("deviceToken") String deviceToken,
                                              @Field("socialType") String socialType,
                                              @Field("referralCode") String referralCode,
                                              @Field("udId") String udId);
    @FormUrlEncoded
    @POST("login")
    Call<MainResponse> loginuser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("deviceToken") String deviceToken,
            @Field("socialType") String socialType,
            @Field("socialId") String socialId,
            @Field("userType") String userType);


}

















/*

  @POST("App/Customer/registration")
    Call<SignUpActivity> loginUser(@Body LoginBodyParameters loginBodyParameters);
*/
