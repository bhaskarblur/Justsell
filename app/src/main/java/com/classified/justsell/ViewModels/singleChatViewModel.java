package com.classified.justsell.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.chatModel;

import org.json.JSONObject;

import java.util.List;

public class singleChatViewModel extends ViewModel {

    public MutableLiveData<chatModel .chatResult> chatData;
    public MutableLiveData<List<JSONObject>> previousChats;

    public void initwork(String userid,String productid,String personid) {
        if(chatData!=null) {
            return;
        }


    }

    public LiveData<chatModel.chatResult> getChatData() {
        return chatData;
    }

    public LiveData<List<JSONObject>> getPreviousChats() {
        return previousChats;
    }

}
