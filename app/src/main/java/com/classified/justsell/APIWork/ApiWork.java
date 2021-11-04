package com.classified.justsell.APIWork;

import com.classified.justsell.Models.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiWork {

    @FormUrlEncoded
    @POST("Login")
    Call<AuthResponse.SendOtp> sendotp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("otp")
    Call<AuthResponse.VerifyOtp> login(@Field("mobile") String mobile,
    @Field("otp") String otp);

    @GET("all-state")
    Call<AuthResponse.getstate> getstate();

    @FormUrlEncoded
    @POST("get-city")
    Call<AuthResponse.getcity> getcity(@Field("state_id") String stateid);

    @FormUrlEncoded
    @POST("profile_update")
    Call<AuthResponse.profile_update> updateprofile(@Field("user_id") String userid,
                                                    @Field("name") String name,
                                                    @Field("state") String state,
                                                    @Field("city") String city,
                                                    @Field("image") String image);
}
