package com.classified.justsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.classified.justsell.Fragments.chatFragment;
import com.classified.justsell.Fragments.homeFragment;
import com.classified.justsell.Fragments.noInternetFragment;
import com.classified.justsell.Fragments.postFragment;
import com.classified.justsell.Fragments.profileFragment;
import com.classified.justsell.R;
import com.classified.justsell.databinding.ActivityHomeBinding;

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
             //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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