package com.classified.justsell.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Repos.AdsRepo;

public class AdsViewModel extends ViewModel {
    
    public MutableLiveData<AdsModel.adsResult> dataModel;
    public AdsRepo repo=new AdsRepo();

    public void initwork(String ad_id,String prodname) {
        if(dataModel!=null) {
            return;
        }

        dataModel=repo.getInstance().returndataModel(ad_id);
    }


    public LiveData<AdsModel.adsResult> getDataModel() {
        return dataModel;
    }
}
