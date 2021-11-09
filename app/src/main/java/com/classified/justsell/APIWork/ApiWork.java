package com.classified.justsell.APIWork;

import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.homeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @FormUrlEncoded
    @POST("profile_update")
    Call<AuthResponse.profile_update> updateprofile2(@Field("user_id") String userid,
                                                    @Field("name") String name,
                                                    @Field("state") String state,
                                                    @Field("city") String city,
                                                    @Field("image") String image,
                                                     @Field("mobile")String mobile);


    @FormUrlEncoded
    @POST("get_user")
    Call<AuthResponse.VerifyOtp> getprofile(@Field("user_id") String userid);

    @GET("banner")
    Call<homeResponse.bannerResp> getBanners();

    @GET("category")
    Call<homeResponse.categoryResp> getCategories();


    @GET("product")
    Call<homeResponse.ListadsResp> getAds(@Query("city") String city);

    @GET("state")
    Call<homeResponse.listofcities> getallcities();

}
