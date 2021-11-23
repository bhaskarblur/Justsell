package com.classified.justsell.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Models.chatModel;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.Repos.AdsRepo;
import com.classified.justsell.Repos.chatRepo;
import com.classified.justsell.Repos.profilefragRepo;

import java.util.List;

public class chatsViewModel extends ViewModel {
    
    public MutableLiveData<List<chatModel.chatResult>> dataModel;
    public chatRepo repo=new chatRepo();

    public void initwork(String userid) {
        if(dataModel!=null) {
            return;
        }

        dataModel=repo.getInstance().returnchatModel(userid);

    }

    public LiveData<List<chatModel.chatResult>> getDataModel() {
        return dataModel;
    }
}
