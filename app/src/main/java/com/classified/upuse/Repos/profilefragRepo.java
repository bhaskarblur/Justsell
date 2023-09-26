package com.classified.justsell.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.Models.homeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profilefragRepo {

    private profilefragRepo instance;
    public List<homeResponse.adsResult> myadslist=new ArrayList<>();
    public List<homeResponse.adsResult> promoteadslist=new ArrayList<>();
    public List<homeResponse.adsResult> favadslist=new ArrayList<>();
    public MutableLiveData<List<homeResponse.adsResult>> myads=new MutableLiveData<>();
    public MutableLiveData<List<homeResponse .adsResult>> promoteads=new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.adsResult>> favads=new MutableLiveData<>();
    public api_baseurl baseurl = new api_baseurl();

    public profilefragRepo getInstance() {
        if(instance==null) {
            instance=new profilefragRepo();
        }
        return instance;
    }

    public MutableLiveData<List<homeResponse.adsResult>> returnmyads(String userid) {
        getmyadsfromServer(userid);
        if(myadslist==null){
            myads.setValue(null);
        }
        myads.setValue(myadslist);
        return myads;
    }

    public MutableLiveData<List<homeResponse.adsResult>> returnfavads(String userid) {
        getfavadsfromServer(userid);
        if(favadslist==null){
            favads.setValue(null);
        }
        favads.setValue(favadslist);
        return favads;
    }

    public MutableLiveData<List<homeResponse.adsResult>> returnpromoteads(String userid) {
        getpromadsfromServer(userid);
        if(promoteadslist==null){
            promoteads.setValue(null);
        }
        promoteads.setValue(promoteadslist);
        return promoteads;
    }

    private void getpromadsfromServer(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.ListadsResp> call3=apiWork.get_myads(userid);

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
                        promoteadslist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getProduct_name());
                    }

                    promoteads.setValue(promoteadslist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.ListadsResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

    private void getfavadsfromServer(String userid) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.ListadsResp> call3=apiWork.getfavAds(userid);

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
                        favadslist.add(resp.getResult().get(i));
                        Log.d("stat1",resp.getResult().get(i).getProduct_name());
                    }

                    favads.setValue(favadslist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.ListadsResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

    private void getmyadsfromServer(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.ListadsResp> call3=apiWork.get_myads(userid);

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
                        myadslist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getProduct_name());
                    }

                    myads.setValue(myadslist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.ListadsResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }
}
