package com.classified.justsell.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.chatModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class singleChatRepo {
    public singleChatRepo instance;
    public List<JSONObject> previousChatsList=new ArrayList<>();
    public MutableLiveData<chatModel.chatResult> chatData=new MutableLiveData<>();
    public MutableLiveData<List<JSONObject>> previousChats=new MutableLiveData<>();
    public api_baseurl baseurl = new api_baseurl();
    public singleChatRepo getInstance() {
        if(instance==null) {
            instance=new singleChatRepo();
        }
        return instance;
    }

    public MutableLiveData<chatModel.chatResult> returnchatData(String userid,String productid,String personid) {
        getdataFromServer(userid,productid,personid);
        return chatData;
    }

    private void getdataFromServer(String userid, String productid, String personid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<chatModel.insidechatResp> call2=apiWork.get_singlechats(userid,productid,personid);

        call2.enqueue(new Callback<chatModel.insidechatResp>() {
            @Override
            public void onResponse(Call<chatModel.insidechatResp> call, Response<chatModel.insidechatResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorCode",String.valueOf(response.code()));
                    return;
                }
                if(response.body().getResult()!=null) {
                    chatData.setValue(response.body().getResult());

                }
            }

            @Override
            public void onFailure(Call<chatModel.insidechatResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });


    }

}
