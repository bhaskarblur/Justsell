package com.classified.upuse.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.upuse.Models.chatModel;
import com.classified.upuse.Repos.singleChatRepo;

import org.json.JSONObject;

import java.util.List;

public class singleChatViewModel extends ViewModel {

    public MutableLiveData<chatModel .chatResult> chatData;
    public MutableLiveData<List<JSONObject>> previousChats;
    public singleChatRepo mrepo=new singleChatRepo();
    public void initwork(String userid,String productid,String personid) {
        if(chatData!=null) {
            return;
        }
        chatData=mrepo.getInstance().returnchatData(userid,productid,personid);
        previousChats=mrepo.getInstance().returnprevChats();

    }

    public LiveData<chatModel.chatResult> getChatData() {
        return chatData;
    }

    public LiveData<List<JSONObject>> getPreviousChats() {
        return previousChats;
    }

}
