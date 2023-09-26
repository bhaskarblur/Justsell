package com.classified.upuse.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.upuse.Models.homeResponse;
import com.classified.justsell.Repos.profilefragRepo;

import java.util.List;

public class profilefragViewModel extends ViewModel {

    public MutableLiveData<List<homeResponse.adsResult>> myads;
    public MutableLiveData<List<homeResponse.adsResult>> promoteads;
    public MutableLiveData<List<homeResponse.adsResult>> favads;
    public profilefragRepo repo=new profilefragRepo();

    public void initwork(String userid) {
        if(myads!=null) {
            return;
        }
        if(favads!=null) {
            return;
        }

        myads=repo.getInstance().returnmyads(userid);
        promoteads=repo.getInstance().returnpromoteads(userid);
        favads=repo.getInstance().returnfavads(userid);
    }
    public LiveData<List<homeResponse.adsResult>> getMyads() {
        return myads;
    }

    public LiveData<List<homeResponse.adsResult>> getPromoteads() {
        return promoteads;
    }

    public LiveData<List<homeResponse.adsResult>> getFavads() {
        return favads;
    }


}
