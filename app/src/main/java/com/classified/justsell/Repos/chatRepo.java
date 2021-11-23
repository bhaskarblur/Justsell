package com.classified.justsell.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Models.chatModel;
import com.classified.justsell.Models.homeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatRepo {

    public chatRepo instance;
    public MutableLiveData<List<chatModel.chatResult>> chatModel=new MutableLiveData<>();
    public List<chatModel.chatResult> chatlist = new ArrayList<>();
    public api_baseurl baseurl = new api_baseurl();

    public chatRepo getInstance() {
        if(instance==null) {
            instance=new chatRepo();
        }
        return instance;
    }

    public MutableLiveData<List<chatModel.chatResult>> returnchatModel(String userid) {
        getchatsFromServer(userid);
        if(chatlist==null) {
            chatModel.setValue(null);
        }
        chatModel.setValue(chatlist);
        return chatModel;
    }

    private void getchatsFromServer(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<chatModel.listChats> call2=apiWork.get_allchats(userid);

        call2.enqueue(new Callback<chatModel.listChats>() {
            @Override
            public void onResponse(Call<chatModel.listChats> call, Response<chatModel.listChats> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                chatModel.listChats resp=response.body();

                if(resp.getResult()!=null) {
                    for(int i=0;i<resp.getResult().size();i++) {
                        chatlist.add(resp.getResult().get(i));
                    }
                    chatModel.setValue(chatlist);
                }
            }

            @Override
            public void onFailure(Call<chatModel.listChats> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }

}
