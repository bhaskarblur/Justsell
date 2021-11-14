package com.classified.justsell.Repos;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.Models.AdsModel;

public class AdsRepo {

    public AdsRepo instance;

    public MutableLiveData<AdsModel.adsResult> dataModel=new MutableLiveData<>();

    public AdsRepo getInstance() {
        if(instance==null) {
            instance=new AdsRepo();
        }
        return instance;
    }

    public MutableLiveData<AdsModel.adsResult> returndataModel(String ad_id) {
        getdatafromServer();
        return dataModel;
    }

    private void getdatafromServer() {

    }

}
