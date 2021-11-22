package com.classified.justsell.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.homeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homefragRepo {

    public homefragRepo instance;
    public List<homeResponse.bannerResult> bannerlist = new ArrayList<>();
    public List<homeResponse.categoryResult> categorylist = new ArrayList<>();
    public List<homeResponse.adsResult> adslist = new ArrayList<>();
    public MutableLiveData<List<homeResponse.bannerResult>> bannerdata = new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.categoryResult>> categorydatanew = new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.adsResult>> adsdata = new MutableLiveData<>();
    public List<homeResponse.citiesResp> citylist = new ArrayList<>();
    public MutableLiveData<List<homeResponse.citiesResp>> citydata = new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.notiResult>> notidata=new MutableLiveData<>();
    public List<homeResponse.notiResult> notilist=new ArrayList<>();
    public api_baseurl baseurl = new api_baseurl();

    public homefragRepo getInstance() {
        if (instance == null) {
            instance = new homefragRepo();
        }
        return instance;
    }

    public MutableLiveData<List<homeResponse.bannerResult>> returnbannerdata() {
        getbannersfromserver();
        if (bannerlist == null) {
            bannerdata.setValue(null);
        }
        bannerdata.setValue(bannerlist);
        return bannerdata;
    }

    public MutableLiveData<List<homeResponse.categoryResult>> returncategorydata() {
        getcategoryfromserver();
        if (categorylist == null) {
            categorydatanew.setValue(null);
        }
        categorydatanew.setValue(categorylist);
        return categorydatanew;
    }

    public MutableLiveData<List<homeResponse.adsResult>> returnadadata(String city) {
        getadsfromserver(city);
        if (adslist == null) {
            adsdata.setValue(null);
        }
        adsdata.setValue(adslist);
        return adsdata;
    }

    public MutableLiveData<List<homeResponse.citiesResp>> returncitydata() {
       getcitiesfromServer();
        if (citylist == null) {
            citydata.setValue(null);
        }
        citydata.setValue(citylist);
        return citydata;
    }

    public MutableLiveData<List<homeResponse.notiResult>> returnnotis(String userid) {
        getnotisfromServer(userid);
        if ( notilist == null) {
            notidata.setValue(null);
        }
        notidata.setValue(notilist);
        return notidata;
    }

    private void getnotisfromServer(String userid) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.notiResp> call2=apiWork.get_notifications(userid);

        call2.enqueue(new Callback<homeResponse.notiResp>() {
            @Override
            public void onResponse(Call<homeResponse.notiResp> call, Response<homeResponse.notiResp> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.notiResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        notilist.add(resp.getResult().get(i));
                    }

                    notidata.setValue(notilist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.notiResp> call, Throwable t) {
                Log.d("Failure_city",t.getMessage());
            }
        });





    }


    private void getcitiesfromServer() {
//        citylist.add(new homeResponse.citiesResp("Ludhiana","Punjab"));
//        citylist.add(new homeResponse.citiesResp("Amritsar","Punjab"));
//        citylist.add(new homeResponse.citiesResp("Khanna","Punjab"));
//        citylist.add(new homeResponse.citiesResp("Jalandhar","Punjab"));

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.listofcities> call2=apiWork.getallcities();

        call2.enqueue(new Callback<homeResponse.listofcities>() {
            @Override
            public void onResponse(Call<homeResponse.listofcities> call, Response<homeResponse.listofcities> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.listofcities resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        citylist.add(resp.getResult().get(i));
                    }

                    citydata.setValue(citylist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.listofcities> call, Throwable t) {
                Log.d("Failure_city",t.getMessage());
            }
        });
    }

    private void getadsfromserver(String city) {
//        adslist.add(new homeResponse.adsResult("Iphone 12X Max 64GB", "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"
//                , "$399", "$500", "1"));
//        adslist.add(new homeResponse.adsResult("Iphone 12X Max 64GB", "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"
//                , "$399", "$500", "0"));
//        adsdata.setValue(adslist);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.ListadsResp> call3=apiWork.getAds(city);

        call3.enqueue(new Callback<homeResponse.ListadsResp>() {
            @Override
            public void onResponse(Call<homeResponse.ListadsResp> call, Response<homeResponse.ListadsResp> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.ListadsResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        adslist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getProduct_name());
                    }

                    adsdata.setValue(adslist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.ListadsResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

    private void getcategoryfromserver() {

        categorylist.add(new homeResponse.categoryResult("All","https://cdn-icons-png.flaticon.com/512/1828/1828884.png"));
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.categoryResp> call2=apiWork.getCategories();

        call2.enqueue(new Callback<homeResponse.categoryResp>() {
            @Override
            public void onResponse(Call<homeResponse.categoryResp> call, Response<homeResponse.categoryResp> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.categoryResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        categorylist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getCategory_image());
                    }

                    categorydatanew.setValue(categorylist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.categoryResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

    private void getbannersfromserver() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.bannerResp> call1=apiWork.getBanners();

        call1.enqueue(new Callback<homeResponse.bannerResp>() {
            @Override
            public void onResponse(Call<homeResponse.bannerResp> call, Response<homeResponse.bannerResp> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.bannerResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        bannerlist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getBanner_image());
                    }

                    bannerdata.setValue(bannerlist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.bannerResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

}
