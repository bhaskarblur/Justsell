package com.classified.justsell.helpingCode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class helpingcode {


    // getting location code
//    private String lat;
//    private String longit;
//    private LocationManager locationManager;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private String mLastLocation;
//    @SuppressLint("MissingPermission")



//    @SuppressLint("MissingPermission")
//    private void getlatlong() {
//        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(dataloaded.equals(false)) {
//                    dataloaded=true;
//                    hmbinding.homefragscroll.setVisibility(View.INVISIBLE);
//                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
//                    hmbinding.retrybtn.setVisibility(View.VISIBLE);
//                    hmbinding.rettxt.setVisibility(View.VISIBLE);
//
//                }
//            }
//        },6000);
//        if(getContext()!=null) {
//            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//                LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                        .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
//                fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//                        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                                if (dataloaded.equals(false)) {
//                                    Location location = task.getResult();
//                                    if (location != null) {
//                                        dataloaded = true;
//                                        lat = String.valueOf(location.getLatitude());
//                                        longit = String.valueOf(location.getLongitude());
//                                        hmViewModel.getlocation(lat, longit);
//                                        Geocoder geocoder = null;
//                                        if(getContext()!=null) {
//                                            geocoder = new Geocoder(getActivity()
//                                                    , Locale.getDefault());
//                                        }
//                                        try {
//                                            if(geocoder!=null) {
//                                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//                                                hmbinding.locattext.setText(addresses.get(0).getLocality());
//                                            }
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        if(hmViewModel.getnbyshopModel()!=null) {
//                                            hmViewModel.getnbyshopModel().observe(getActivity(), new Observer<List<nbyshopsModel>>() {
//                                                @Override
//                                                public void onChanged(List<nbyshopsModel> nbyshopsModels) {
//                                                    new Handler().postDelayed(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            if (nbyshopsModels.size() > 0) {
//                                                                nbadapter.notifyDataSetChanged();
//                                                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
//                                                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
//                                                            }
//                                                        }
//                                                    }, 100);
//                                                }
//                                            });
//                                            loadnearbyshoprec();
//                                        }
//                                    } else {
//
//                                        LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
//
//                                        LocationCallback locationCallback = new LocationCallback() {
//                                            @Override
//                                            public void onLocationResult(LocationResult locationResult) {
//                                                super.onLocationResult(locationResult);
//                                                Location location1 = locationResult.getLastLocation();
//                                                lat = String.valueOf(location1.getLatitude());
//                                                longit = String.valueOf(location1.getLongitude());
//                                                hmViewModel.getlocation(lat, longit);
//                                                Geocoder geocoder = null;
//                                                if(getContext()!=null) {
//                                                    geocoder = new Geocoder(getActivity()
//                                                            , Locale.getDefault());
//                                                }
//                                                try {
//                                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//                                                    hmbinding.locattext.setText(addresses.get(0).getLocality());
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                hmViewModel.getnbyshopModel().observe(getActivity(), new Observer<List<nbyshopsModel>>() {
//                                                    @Override
//                                                    public void onChanged(List<nbyshopsModel> nbyshopsModels) {
//                                                        new Handler().postDelayed(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                if (nbyshopsModels.size() > 0) {
//                                                                    nbadapter.notifyDataSetChanged();
//                                                                    hmbinding.homefragscroll.setVisibility(View.VISIBLE);
//                                                                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
//                                                                }
//                                                            }
//                                                        }, 100);
//                                                    }
//                                                });
//                                                loadnearbyshoprec();
//                                            }
//                                        };
//                                    }
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }, Looper.getMainLooper());
//                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                    getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                }
//
//
//            }
//        }
//
//    }


    //banner rotation

//    private void rotatebanner() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (bannerAdapter.getItemCount() > 0) {
//                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 0) {
//                        hmbinding.bannerrv.setCurrentItem(1, true);
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                        return;
//                    }
//                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 1) {
//                        hmbinding.bannerrv.setCurrentItem(2, true);
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                        return;
//                    }
//                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 2) {
//                        hmbinding.bannerrv.setCurrentItem(3, true);
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                        return;
//                    }
//
//                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 2) {
//                        hmbinding.bannerrv.setCurrentItem(0, true);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                        return;
//                    }
//                    if (hmbinding.bannerrv.getCurrentItem() == bannerAdapter.getItemCount() - 1) {
//                        hmbinding.bannerrv.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                        return;
//
//                    } else {
//                        hmbinding.bannerrv.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        rotatebanner();
//                    }
//                }
//
//
//            }
//        }, 5000);
//    }



//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(@NonNull String provider) {
//
//    }

}
