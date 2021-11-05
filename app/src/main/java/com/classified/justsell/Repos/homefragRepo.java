package com.classified.justsell.Repos;

import androidx.lifecycle.MutableLiveData;

import com.classified.justsell.Models.homeResponse;

import java.util.ArrayList;
import java.util.List;

public class homefragRepo {

    public homefragRepo instance;
    public List<homeResponse.bannerResult> bannerlist = new ArrayList<>();
    public List<homeResponse.categoryResult> categorylist = new ArrayList<>();
    public List<homeResponse.adsResult> adslist = new ArrayList<>();

    public MutableLiveData<List<homeResponse.bannerResult>> bannerdata = new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.categoryResult>> categorydatanew = new MutableLiveData<>();
    public MutableLiveData<List<homeResponse.adsResult>> adsdata = new MutableLiveData<>();

    public homefragRepo getInstance() {
        if (instance == null) {
            instance = new homefragRepo();
        }
        return instance;
    }

    public MutableLiveData<List<homeResponse.bannerResult>> returnbannerdata() {
        getbannersfromserver();
        if (bannerlist == null) {
            bannerdata.setValue(null);
        }
        bannerdata.setValue(bannerlist);
        return bannerdata;
    }

    public MutableLiveData<List<homeResponse.categoryResult>> returncategorydata() {
        getcategoryfromserver();
        if (categorylist == null) {
            categorydatanew.setValue(null);
        }
        categorydatanew.setValue(categorylist);
        return categorydatanew;
    }

    public MutableLiveData<List<homeResponse.adsResult>> returnadadata() {
        getadsfromserver();
        if (adslist == null) {
            adsdata.setValue(null);
        }
        adsdata.setValue(adslist);
        return adsdata;
    }

    private void getadsfromserver() {
        adslist.add(new homeResponse.adsResult("Iphone 12X Max 64GB","https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"
        ,"$399","$500","yes"));
        adslist.add(new homeResponse.adsResult("Iphone 12X Max 64GB","https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"
                ,"$399","$500","no"));
        adsdata.setValue(adslist);
    }

    private void getcategoryfromserver() {
        categorylist.add(new homeResponse.categoryResult("Sports","https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"));
        categorylist.add(new homeResponse.categoryResult("Technology","https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-12-blue-select-2020?wid=940&hei=1112&fmt=png-alpha&.v=1604343704000"));
        categorydatanew.setValue(categorylist);
    }

    private void getbannersfromserver() {
        bannerlist.add(new homeResponse.bannerResult("https://mir-s3-cdn-cf.behance.net/project_modules/1400/45838099680787.5ef86c4b01fb3.png"));
        bannerlist.add(new homeResponse.bannerResult("https://thumbs.dreamstime.com/b/kharkov-ukraine-march-apple-iphone-mini-retail-box-blue-color-yellow-background-banner-photo-212841861.jpg"));
        bannerdata.postValue(bannerlist);
    }

}
