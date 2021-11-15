package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.adImagesAdapter;
import com.classified.justsell.Adapters.adsAdapter;
import com.classified.justsell.Adapters.bannerAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Fragments.profileFragment;
import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.ViewModels.AdsViewModel;
import com.classified.justsell.databinding.ActivityAdUserBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ad_userActivity extends AppCompatActivity {
    private ActivityAdUserBinding binding;
    private Integer pos = 0;
    private com.classified.justsell.Adapters.adImagesAdapter adImagesAdapter;
    private com.classified.justsell.Adapters.adsAdapter adsAdapter;
    private List<AdsModel.imagesres> bannerlist = new ArrayList<>();
    private AdsViewModel adsViewModel;
    private api_baseurl baseurl=new api_baseurl();
    String adid;
    String prod_name;
    String cat_name;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        ManageData();
        viewfuncs();
    }

    private void viewfuncs() {

        binding.backbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);
                Log.d("data",adid+","+userid);
                Call<AuthResponse.SendOtp> call3 = apiWork.remove_favourite(adid, userid);
                call3.enqueue(new Callback<AuthResponse.SendOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("error code", String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp = response.body();

                        if (resp.getCode().equals("200")) {
                            //
                            Toast.makeText(Ad_userActivity.this, "Ad removed from favourite.", Toast.LENGTH_SHORT).show();
                            binding.imageView6.setVisibility(View.VISIBLE);
                            binding.imageView9.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                    }
                });
            }
        });
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);
                Log.d("data",adid+","+userid);
                Call<AuthResponse.SendOtp> call3 = apiWork.add_favourite(adid, userid);
                call3.enqueue(new Callback<AuthResponse.SendOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("error code", String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp = response.body();

                        if (resp.getCode().equals("200")) {
                            //
                            Toast.makeText(Ad_userActivity.this, "Ad added to favourite.", Toast.LENGTH_SHORT).show();
                            binding.imageView6.setVisibility(View.INVISIBLE);
                            binding.imageView9.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                    }
                });
            }
        });



        binding.chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chat button!
            }
        });

    }

    private void ManageData() {
        pos = 0;
        Intent intent = getIntent();
         adid = intent.getStringExtra("ad_id");
         prod_name = intent.getStringExtra("product_name");
        cat_name = intent.getStringExtra("cat_name");
        SharedPreferences sharedPreferences=getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
        adsViewModel = new ViewModelProvider(Ad_userActivity.this).get(AdsViewModel.class);
        adsViewModel.initwork(adid, prod_name,userid);

        adsViewModel.getFavads().observe(Ad_userActivity.this, new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                for(int i=0;i<adsResults.size();i++) {
                    if(adsResults.get(i).getAd_id().equals(adid)) {
                        binding.imageView6.setVisibility(View.INVISIBLE);
                        binding.imageView9.setVisibility(View.VISIBLE);

                        break;
                    }
                    else {
                        binding.imageView6.setVisibility(View.VISIBLE);
                        binding.imageView9.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        adsViewModel.getDataModel().observe(Ad_userActivity.this, new Observer<AdsModel.adsResult>() {
            @Override
            public void onChanged(AdsModel.adsResult adsResult) {
                if (adsResult != null) {
                    if (adsResult.getImages().size() > 0) {
                        bannerlist.clear();
                        bannerlist = adsResult.getImages();
                        adImagesAdapter = new adImagesAdapter(Ad_userActivity.this, bannerlist);
                        binding.bannerrv.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        binding.bannerrv.setAdapter(adImagesAdapter);
                        binding.bannerrv.setCurrentItem(0);
                        rotatebanner();
                        if(adsResult.getImages().size()<3) {
                            binding.onbprog11.setVisibility(View.INVISIBLE);
                            binding.onbprog12.setVisibility(View.INVISIBLE);
                            binding.onbprog13.setVisibility(View.INVISIBLE);
                            binding.onbprog14.setVisibility(View.INVISIBLE);
                        }
                        else if(adsResult.getImages().size()<4) {
                            binding.onbprog12.setVisibility(View.INVISIBLE);
                            binding.onbprog13.setVisibility(View.INVISIBLE);
                            binding.onbprog14.setVisibility(View.INVISIBLE);
                        }
                        else if(adsResult.getImages().size()<5) {
                            binding.onbprog13.setVisibility(View.INVISIBLE);
                            binding.onbprog14.setVisibility(View.INVISIBLE);
                        }
                        else if(adsResult.getImages().size()<6) {
                            binding.onbprog14.setVisibility(View.INVISIBLE);
                        }
                    }

                    binding.prodpriceTxt.setText("Rs "+adsResult.getSelling_price());
                    binding.prodtitleTxt.setText(adsResult.getAd_title());
                    binding.proddescTxt.setText(adsResult.getDescription());
                    binding.viewscount.setText(adsResult.getAd_views());
                    Picasso.get().load(adsResult.getPost_by_image()).transform(new CropCircleTransformation()).resize(150,150)
                            .into(binding.postimage);
                    binding.postname.setText(adsResult.getPost_by());
                    if (cat_name.equals("Automobile") || cat_name.equals("Car") || cat_name.equals("Bike")) {

                        binding.line1.setText("Brand");
                        binding.line2.setText("Model");
                        binding.line3.setText("Date Of Purchase");
                        binding.line4.setText("Fuel Type");
                        binding.line5.setText("Transmission");
                        binding.line6.setText("Km Driven");
                        binding.line7.setText("Number Of Owners");

                        binding.line1Data.setText(adsResult.getBrand());
                        binding.line2Data.setText(adsResult.getModel());
                        binding.line3Data.setText(adsResult.getPurchase_date());
                        binding.line4Data.setText(adsResult.getFuel_type());
                        binding.line5Data.setText(adsResult.getTransmission());
                        binding.line6Data.setText(adsResult.getKmdriven());
                        binding.line7Data.setText(adsResult.getNumber_of_owners());


                    } else if (cat_name.equals("Property") || cat_name.equals("House") || cat_name.equals("Land")) {

                        binding.line1.setText("Ad Type");
                        binding.line2.setText("Property Type");
                        binding.line3.setText("Land Type");
                        binding.line4.setText("Area");


                        binding.line1Data.setText(adsResult.getAd_type());
                        binding.line2Data.setText(adsResult.getProperty_type());
                        binding.line3Data.setText(adsResult.getLand_type());
                        binding.line4Data.setText(adsResult.getArea());
                        binding.line5Data.setVisibility(View.GONE);
                        binding.line6Data.setVisibility(View.GONE);
                        binding.line7Data.setVisibility(View.GONE);


                    } else {

                        binding.line1.setText("Condition");
                        binding.line2.setText("In Warranty");
                        binding.line3.setText("Brand");
                        binding.line4.setText("Date Of Purchase");

                        binding.line1Data.setText(adsResult.getCondition());
                        binding.line2Data.setText(adsResult.getIn_warranty());
                        binding.line3Data.setText(adsResult.getBrand());
                        binding.line4Data.setText(adsResult.getPurchase_date());
                        binding.line5Data.setVisibility(View.GONE);
                        binding.line6Data.setVisibility(View.GONE);
                        binding.line7Data.setVisibility(View.GONE);
                    }
                }
            }
        });

        binding.bannerrv.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#FFD057"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 1) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#FFD057"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 2) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#FFD057"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 3) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#FFD057"));
                }
                else if (position == 4) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog13.getBackground().setTint(Color.parseColor("#FFD057"));
                }
                else if (position == 5) {
                    binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog13.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    binding.onbprog14.getBackground().setTint(Color.parseColor("#FFD057"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });



    }


    private void rotatebanner() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adImagesAdapter.getItemCount() > 0) {
                    if (adImagesAdapter.getItemCount() > binding.bannerrv.getCurrentItem() && binding.bannerrv.getCurrentItem() == 0) {
                        binding.bannerrv.setCurrentItem(1, true);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#FFD057"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (adImagesAdapter.getItemCount() > binding.bannerrv.getCurrentItem() && binding.bannerrv.getCurrentItem() == 1) {
                        binding.bannerrv.setCurrentItem(2, true);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#FFD057"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (adImagesAdapter.getItemCount() > binding.bannerrv.getCurrentItem() && binding.bannerrv.getCurrentItem() == 2) {
                        binding.bannerrv.setCurrentItem(3, true);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#FFD057"));
                        rotatebanner();
                        return;
                    }

                    if (adImagesAdapter.getItemCount() > binding.bannerrv.getCurrentItem() && binding.bannerrv.getCurrentItem() == 2) {
                        binding.bannerrv.setCurrentItem(0, true);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#FFD057"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (binding.bannerrv.getCurrentItem() == adImagesAdapter.getItemCount() - 1) {
                        binding.bannerrv.setCurrentItem(0);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#FFD057"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;

                    } else {
                        binding.bannerrv.setCurrentItem(0);
                        binding.onbprog9.getBackground().setTint(Color.parseColor("#FFD057"));
                        binding.onbprog10.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog11.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        binding.onbprog12.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                    }
                }


            }
        }, 5000);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        getViewModelStore().clear();
    }

}