package com.classified.upuse;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.classified.upuse.R;
import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Adapters.adImagesAdapter;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.CustomDialogs.deletepost_Dialog;
import com.classified.upuse.Models.AdsModel;
import com.classified.upuse.Models.AuthResponse;
import com.classified.upuse.Models.homeResponse;
import com.classified.upuse.ViewModels.AdsViewModel;
import com.classified.upuse.databinding.ActivityAdPosterBinding;
import com.classified.upuse.databinding.ActivityAdUserBinding;
import com.classified.upuse.databinding.ActivityPostPropertyBinding;
import com.classified.upuse.helpingCode.progressDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ad_posterActivity extends AppCompatActivity {
    private ActivityAdPosterBinding binding;
    private Integer pos = 0;
    private com.classified.upuse.Adapters.adImagesAdapter adImagesAdapter;
    private com.classified.upuse.Adapters.adsAdapter adsAdapter;
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
        binding=ActivityAdPosterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        View decorView = getWindow().getDecorView();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ManageData();
        viewfuncs();
    }

    private void viewfuncs() {

        binding.imageView8.setOnClickListener(new View.OnClickListener() {
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
                            Log.d("error code1", String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp = response.body();

                        if (!resp.getMessage().isEmpty()) {
                            //
                            Toast.makeText(Ad_posterActivity.this, "Ad removed from favourite.", Toast.LENGTH_SHORT).show();
                            binding.imageView6.setVisibility(View.VISIBLE);
                            binding.imageView8.setVisibility(View.INVISIBLE);
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
                            Log.d("error code2", String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp = response.body();

                        if (!resp.getMessage().isEmpty()) {
                            //
                            Toast.makeText(Ad_posterActivity.this, "Ad added to favourite.", Toast.LENGTH_SHORT).show();
                            binding.imageView6.setVisibility(View.INVISIBLE);
                            binding.imageView8.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                    }
                });
            }
        });


        binding.prombtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Ad_posterActivity.this,promote_ad.class);
                intent.putExtra("ad_id",adid);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

            }
        });

        binding.backbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("ad_id",adid);
                deletepost_Dialog dialog = new deletepost_Dialog();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

    }


    private void ManageData() {
        progressDialog progressdialog = new progressDialog();
        progressdialog.showLoadingDialog(this, "Loading",
                "Loading. Please wait");
        pos = 0;
        Intent intent = getIntent();
        adid = intent.getStringExtra("ad_id");
        Log.d("ad_id", adid);
        prod_name = intent.getStringExtra("product_name");
        cat_name = intent.getStringExtra("cat_name");
        SharedPreferences sharedPreferences=getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
        adsViewModel = new ViewModelProvider(Ad_posterActivity.this).get(AdsViewModel.class);
        adsViewModel.initwork(adid, prod_name,userid);

        adsViewModel.getFavads().observe(Ad_posterActivity.this, new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                for(int i=0;i<adsResults.size();i++) {
                    if(adsResults.get(i).getAd_id().equals(adid)) {
                        binding.imageView6.setVisibility(View.INVISIBLE);
                        binding.imageView8.setVisibility(View.VISIBLE);
                        Log.d("favourite","yes "+adsResults.get(i).getAd_id()+","+adid);
                        break;
                    }
                    else {
                        binding.imageView6.setVisibility(View.VISIBLE);
                        binding.imageView8.setVisibility(View.INVISIBLE);
                        Log.d("favourite","no");
                        Log.d("favourite","no "+adsResults.get(i).getAd_id()+","+adid);
                    }
                }
            }
        });
        adsViewModel.getDataModel().observe(Ad_posterActivity.this, new Observer<AdsModel.adsResult>() {
            @Override
            public void onChanged(AdsModel.adsResult adsResult) {
                if (adsResult != null) {
                    if (adsResult.getImages().size() > 0) {
                        bannerlist.clear();
                        bannerlist = adsResult.getImages();
                        adImagesAdapter = new adImagesAdapter(Ad_posterActivity.this, bannerlist);
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
                    if(adsResult.getPost_by_image()!=null) {
                        Picasso.get().load(adsResult.getPost_by_image()).transform(new CropCircleTransformation()).resize(150, 150)
                                .into(binding.postimage);
                    }
                    binding.postname.setText(adsResult.getPost_by());
                    if(adsResult.getPost_by_number()!=null) {
                        binding.postnumber.setText(adsResult.getPost_by_number());
                    }
                    else {
                        binding.postnumber.setText("Number not shared.");
                    }
                    binding.promDateTxt.setText("Ad ending on "+adsResult.getPromotion_enddate());

                    if(adsResult.getLatitude()!=null && adsResult.getLongitude()!=null) {
                        loadmat(Double.parseDouble(adsResult.getLatitude()),Double.parseDouble(adsResult.getLongitude()));
                    }
                    if (adsResult.getProduct_type().toString().equals("automobile"))
                    {
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


                    } else if (adsResult.getProduct_type().toString().equals("property"))
                    {

                        binding.line1.setText("Ad Type");
                        binding.line2.setText("Property Type");
                        binding.line3.setText("Land Type");
                        binding.line4.setText("Area");


                        binding.line1Data.setText(adsResult.getAd_type());
                        binding.line2Data.setText(adsResult.getProperty_type());
                        binding.line3Data.setText(adsResult.getLand_type());
                        binding.line4Data.setText(adsResult.getArea());
                        binding.line5.setVisibility(View.GONE);
                        binding.line6.setVisibility(View.GONE);
                        binding.line7.setVisibility(View.GONE);
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
                        binding.line5.setVisibility(View.GONE);
                        binding.line6.setVisibility(View.GONE);
                        binding.line7.setVisibility(View.GONE);
                        binding.line5Data.setVisibility(View.GONE);
                        binding.line6Data.setVisibility(View.GONE);
                        binding.line7Data.setVisibility(View.GONE);
                    }
                }
                progressdialog.hideLoadingDialog();
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
    private void loadmat(double sellat, double sellongit) {
        final String[] sellerlat = new String[1];
        final String[] sellerlong = new String[1];
        MapFragment supportMapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.google_map);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        googleMap.setMinZoomPreference(18);
                        LatLng latLng = new LatLng(sellat, sellongit);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                .title("Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.locaticonyellow));
                        sellerlat[0] =String.valueOf(sellat);
                        sellerlong[0] =String.valueOf(sellongit);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        }, 100);


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