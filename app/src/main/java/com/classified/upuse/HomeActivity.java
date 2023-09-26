package com.classified.upuse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.classified.upuse.Fragments.chatFragment;
import com.classified.upuse.Fragments.homeFragment;
import com.classified.upuse.Fragments.postFragment;
import com.classified.upuse.Fragments.profileFragment;
import com.classified.upuse.R;
import com.classified.upuse.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    final int PERMISSION_CODE = 1001;
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
             startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        handlePermissions();
        handleBottomNav();
        loadads();


        //startActivity(new Intent(HomeActivity.this,chatActivity.class));

    }
    private void loadads() {
        MobileAds.initialize(HomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                List<String> testdev_id=new ArrayList<>();
//                testdev_id.add("E0E6437ED479B9EC814E40CDFE6D5D47");
//                RequestConfiguration configuration =
//                        new RequestConfiguration.Builder().setTestDeviceIds(testdev_id).build();
//                MobileAds.setRequestConfiguration(configuration);

                AdView adView = new AdView(HomeActivity.this);
                adView.setAdSize(AdSize.BANNER);
                // test banner id ca-app-pub-3940256099942544/6300978111
                // banner ad id ca-app-pub-8346863949141411/9276270004
                adView.setAdUnitId("ca-app-pub-8346863949141411/9276270004");
                AdRequest adRequest = new AdRequest.Builder().build();
                binding.adView.loadAd(adRequest);

                binding.adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.d("adError",adError.getMessage());
                        Toast.makeText(HomeActivity.this, "Error "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.

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
        },1000);

    }
    private void handlePermissions() {

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
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}