package com.classified.upuse.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.upuse.Models.AdsModel;
import com.classified.upuse.Models.homeResponse;
import com.classified.justsell.Repos.profilefragRepo;
import com.classified.upuse.Repos.AdsRepo;

import java.util.List;

public class AdsViewModel extends ViewModel {
    
    public MutableLiveData<AdsModel.adsResult> dataModel;
    public AdsRepo repo=new AdsRepo();
    public MutableLiveData<List<homeResponse.adsResult>> favads;
    public profilefragRepo mrepo=new profilefragRepo();
    public MutableLiveData<List<homeResponse.adsResult>> adsdata;
    public LiveData<List<homeResponse.adsResult>> getFavads() {
        return favads;
    }


    public void initwork(String ad_id,String prodname,String userid) {
        if(dataModel!=null) {
            return;
        }

        dataModel=repo.getInstance().returndataModel(ad_id,userid);
        favads=mrepo.getInstance().returnfavads(userid);
        adsdata= repo.getInstance().returnadadata(ad_id,userid);
    }

    public LiveData<List<homeResponse.adsResult>> getAdsdata() {
        return adsdata;
    }

    public LiveData<AdsModel.adsResult> getDataModel() {
        return dataModel;
    }
}
