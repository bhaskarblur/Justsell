package com.classified.justsell.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.chatModel;

import org.json.JSONException;
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
        Log.d("product_id",productid);
        Log.d("person_id",personid);
        getdataFromServer(userid,productid,personid);
        return chatData;
    }
    public MutableLiveData<List<JSONObject>> returnprevChats() {
        return previousChats;
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
                    if(response.body().getMessages()!=null) {
                        for (int i = 0; i < response.body().getMessages().size(); i++) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("time",response.body().getMessages().get(i).getTime());
                                jsonObject.put("user_id", response.body().getMessages().get(i).getUser_id());
                                jsonObject.put("product_id", response.body().getMessages().get(i).getProduct_id());
                                jsonObject.put("person_id", response.body().getMessages().get(i).getPerson_id());
                                jsonObject.put("message", response.body().getMessages().get(i).getMessage());
                                jsonObject.put("image", response.body().getMessages().get(i).getImage());
                                jsonObject.put("isSent", response.body().getMessages().get(i).getSent());
                                jsonObject.put("seen", "yes");
                                previousChatsList.add(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user_id", "2");
//                            jsonObject.put("product_id", "8");
//                            jsonObject.put("person_id", "4");
//                            jsonObject.put("message", "Bhai yeh dekh");
//                            jsonObject.put("isSent", "yes");
//                            jsonObject.put("seen", "yes");
//                            previousChatsList.add(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user_id", "2");
//                            jsonObject.put("product_id", "8");
//                            jsonObject.put("person_id", "4");
//                            jsonObject.put("image", "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/macbook-air-gold-select-201810?wid=904&hei=840&fmt=jpeg&qlt=80&.v=1633027804000");
//                            jsonObject.put("isSent", "yes");
//                            jsonObject.put("seen", "yes");
//                            previousChatsList.add(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user_id", "2");
//                            jsonObject.put("product_id", "8");
//                            jsonObject.put("person_id", "4");
//                            jsonObject.put("image", "https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-13-family-select-2021?wid=940&hei=1112&fmt=jpeg&qlt=80&.v=1629842667000");
//                            jsonObject.put("isSent", "no");
//                            jsonObject.put("seen", "yes");
//                            previousChatsList.add(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user_id", "2");
//                            jsonObject.put("product_id", "8");
//                            jsonObject.put("person_id", "4");
//                            jsonObject.put("message", "wow boi congo yrr, happy for you");
//                            jsonObject.put("isSent", "no");
//                            jsonObject.put("seen", "yes");
//                            previousChatsList.add(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user_id", "2");
//                            jsonObject.put("product_id", "8");
//                            jsonObject.put("person_id", "4");
//                            jsonObject.put("message", "Thoinks");
//                            jsonObject.put("isSent", "yes");
//                            jsonObject.put("seen", "yes");
//                            previousChatsList.add(jsonObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//

                        previousChats.setValue(previousChatsList);
                    }
                }
            }

            @Override
            public void onFailure(Call<chatModel.insidechatResp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });


    }

}
