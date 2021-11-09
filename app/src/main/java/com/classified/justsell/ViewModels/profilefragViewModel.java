package com.classified.justsell.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.justsell.Models.homeResponse;

public class profilefragViewModel extends ViewModel {

    public MutableLiveData<homeResponse .adsResult> myads;
    public MutableLiveData<homeResponse .adsResult> promoteads;
    public MutableLiveData<homeResponse.adsResult> favads;
}
