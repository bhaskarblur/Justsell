package com.classified.upuse.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.classified.upuse.Models.homeResponse;
import com.classified.justsell.Repos.postRepo;

import java.util.List;

public class postViewModel extends ViewModel {
    public MutableLiveData<List<homeResponse.categoryResult>> categorydata;
    private postRepo repo=new postRepo();

    public void initwork() {
        if(categorydata!=null) {
            return;
        }

        categorydata=repo.getInstance().returnallcatdata();
    }

    public LiveData<List<homeResponse.categoryResult>> getCategorydata() {
        return categorydata;
    }
}
