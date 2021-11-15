package com.classified.justsell.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.Repos.AdsRepo;
import com.classified.justsell.Repos.profilefragRepo;

import java.util.List;

public class AdsViewModel extends ViewModel {
    
    public MutableLiveData<AdsModel.adsResult> dataModel;
    public AdsRepo repo=new AdsRepo();
    public MutableLiveData<List<homeResponse.adsResult>> favads;
    public profilefragRepo mrepo=new profilefragRepo();

    public LiveData<List<homeResponse.adsResult>> getFavads() {
        return favads;
    }


    public void initwork(String ad_id,String prodname,String userid) {
        if(dataModel!=null) {
            return;
        }

        dataModel=repo.getInstance().returndataModel(ad_id);
        favads=mrepo.getInstance().returnfavads(userid);

    }


    public LiveData<AdsModel.adsResult> getDataModel() {
        return dataModel;
    }
}
