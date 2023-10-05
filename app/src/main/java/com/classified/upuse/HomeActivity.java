package com.classified.upuse;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.classified.upuse.helpingCode.progressDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.classified.upuse.Fragments.chatFragment;
import com.classified.upuse.Fragments.homeFragment;
import com.classified.upuse.Fragments.postFragment;
import com.classified.upuse.Fragments.profileFragment;
import com.classified.upuse.R;
import com.classified.upuse.databinding.ActivityHomeBinding;
import com.google.firebase.analytics.FirebaseAnalytics;

public class HomeActivity extends AppCompatActivity {
    final int PERMISSION_CODE = 1001;
   private ActivityHomeBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(HomeActivity.this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("userlogged",0);
        mFirebaseAnalytics.setUserProperty("userName",
                sharedPreferences.getString("username",""));
        mFirebaseAnalytics.setUserProperty("userPhone",
                sharedPreferences.getString("usermobile",""));
        mFirebaseAnalytics.setUserProperty("userCity",
                sharedPreferences.getString("usercity",""));

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
             startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                    Toast.makeText(HomeActivity.this,
                            "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        handleBottomNav();
        loadads();
            }
        },500);


    }
    private void loadads() {
        MobileAds.initialize(HomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d("ad init complete", initializationStatus.toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AdView adView = new AdView(HomeActivity.this);
                        adView.setAdSize(AdSize.BANNER);
                        adView.setAdUnitId("ca-app-pub-3736420404472867/4343939301");
                        AdRequest adRequest = new AdRequest.Builder().build();
                        binding.adView.loadAd(adRequest);
                        binding.adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                Log.d("adLoaded", "true");
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                Log.d("adError",adError.getMessage());
                                Toast.makeText(HomeActivity.this, "Error "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdOpened() {
                                Log.d("adOpened", "true");

                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the user is about to return
                                // to the app after tapping on an ad.
                            }
                        });
                    }
                },500);
            }
        });


    }
    private void handleBottomNav() {

        binding.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeicon.setVisibility(View.INVISIBLE);
                binding.homeselectedicon.setVisibility(View.VISIBLE);

                binding.chaticon.setVisibility(View.VISIBLE);
                binding.chatselectedicon.setVisibility(View.INVISIBLE);

                binding.profileicon.setVisibility(View.VISIBLE);
                binding.profileselectedicon.setVisibility(View.INVISIBLE);

                binding.posticon.setVisibility(View.VISIBLE);
                binding.postselectedicon.setVisibility(View.INVISIBLE);

                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();

            }
        });

        binding.posticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeicon.setVisibility(View.VISIBLE);
                binding.homeselectedicon.setVisibility(View.INVISIBLE);

                binding.chaticon.setVisibility(View.VISIBLE);
                binding.chatselectedicon.setVisibility(View.INVISIBLE);

                binding.profileicon.setVisibility(View.VISIBLE);
                binding.profileselectedicon.setVisibility(View.INVISIBLE);

                binding.posticon.setVisibility(View.INVISIBLE);
                binding.postselectedicon.setVisibility(View.VISIBLE);

                postFragment homeFragment=new postFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });

        binding.chaticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeicon.setVisibility(View.VISIBLE);
                binding.homeselectedicon.setVisibility(View.INVISIBLE);

                binding.chaticon.setVisibility(View.INVISIBLE);
                binding.chatselectedicon.setVisibility(View.VISIBLE);

                binding.profileicon.setVisibility(View.VISIBLE);
                binding.profileselectedicon.setVisibility(View.INVISIBLE);

                binding.posticon.setVisibility(View.VISIBLE);
                binding.postselectedicon.setVisibility(View.INVISIBLE);

                chatFragment homeFragment=new chatFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });

        binding.profileicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeicon.setVisibility(View.VISIBLE);
                binding.homeselectedicon.setVisibility(View.INVISIBLE);

                binding.chaticon.setVisibility(View.VISIBLE);
                binding.chatselectedicon.setVisibility(View.INVISIBLE);

                binding.profileicon.setVisibility(View.INVISIBLE);
                binding.profileselectedicon.setVisibility(View.VISIBLE);

                binding.posticon.setVisibility(View.VISIBLE);
                binding.postselectedicon.setVisibility(View.INVISIBLE);

                profileFragment homeFragment=new profileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();

            if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
                // set the home tab as default;
                binding.homeicon.setVisibility(View.INVISIBLE);
                binding.homeselectedicon.setVisibility(View.VISIBLE);

                binding.chaticon.setVisibility(View.VISIBLE);
                binding.chatselectedicon.setVisibility(View.INVISIBLE);

                binding.profileicon.setVisibility(View.VISIBLE);
                binding.profileselectedicon.setVisibility(View.INVISIBLE);

                binding.posticon.setVisibility(View.VISIBLE);
                binding.postselectedicon.setVisibility(View.INVISIBLE);

                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();

            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).
                    setTitle("Exit?").setMessage("Do you want to exit the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    Toast.makeText(this, String.valueOf( grantResults[0]), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}