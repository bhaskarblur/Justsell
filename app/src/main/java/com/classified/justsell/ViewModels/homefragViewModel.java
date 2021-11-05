package com.classified.justsell.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.Repos.homefragRepo;

import java.util.List;

public class homefragViewModel extends ViewModel {

    public MutableLiveData<List<homeResponse.bannerResult>> bannerdata;
    public MutableLiveData<List<homeResponse.categoryResult>> categorydata;
    public MutableLiveData<List<homeResponse.adsResult>> adsdata;
    private homefragRepo repo=new homefragRepo();


    public void initwork(String userid, String lat, String longit,String location) {
        if(bannerdata!=null) {
            return;
        }
        if(categorydata!=null) {
            return;
        }
        if(adsdata!=null) {
            return;
        }

        bannerdata= repo.getInstance().getInstance().returnbannerdata();
        categorydata=repo.getInstance().returncategorydata();
        adsdata= repo.getInstance().returnadadata();


    }
    public LiveData<List<homeResponse.bannerResult>> getBannerdata() {
        return bannerdata;
    }


    public LiveData<List<homeResponse.categoryResult>> getCategorydata() {
        return categorydata;
    }


    public LiveData<List<homeResponse.adsResult>> getAdsdata() {
        return adsdata;
    }

}
