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

public class postRepo {

    public postRepo instance;
    public List<homeResponse.categoryResult> catlist=new ArrayList<>();
    public MutableLiveData<List<homeResponse.categoryResult>> categorydata=new MutableLiveData<>();
    public api_baseurl baseurl = new api_baseurl();
    public postRepo getInstance() {
        if(instance==null) {
            instance=new postRepo();
        }
        return instance;
    }
    
    public MutableLiveData<List<homeResponse.categoryResult>> returnallcatdata() {
        getcatsfromServer();
        if(catlist==null) {
            categorydata.setValue(null);
        }
        categorydata.setValue(catlist);
        return categorydata;
    }

    private void getcatsfromServer() {
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
                        catlist.add(resp.getResult().get(i));
                        Log.d("stat",resp.getResult().get(i).getCategory_image());
                    }

                    categorydata.setValue(catlist);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.categoryResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }
}
