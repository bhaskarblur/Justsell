package com.classified.upuse.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classified.upuse.R;
import com.classified.upuse.databinding.FragmentLocationBinding;
import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Adapters.cityAdapter;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.Models.homeResponse;
import com.classified.upuse.ViewModels.homefragViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link locationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class locationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentLocationBinding binding;
    private String userid;
    private SharedPreferences sharedPreferences;
    private com.classified.upuse.Adapters.cityAdapter cityAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String lat;
    private String longit;
    private LocationManager locationManager;
    private String city;
    private String state;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    private List<homeResponse.citiesResp> citylist1=new ArrayList<>();
    private homefragViewModel hmViewModel;
    public locationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment locationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static locationFragment newInstance(String param1, String param2) {
        locationFragment fragment = new locationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);

        View bottombar=getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.GONE);
        ConnectivityManager connectivityManager =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
            noInternetFragment nocon=new noInternetFragment();
            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction1.setCustomAnimations(R.anim.fade_2, R.anim.fade);
            transaction1.replace(R.id.mainFragment, nocon);
            transaction1.addToBackStack("A");
            transaction1.commit();
        }
        else {
            ManageData();
            viewfunc();
        }
        return binding.getRoot();
    }

    private void viewfunc() {
        binding.backbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });

        binding.citytext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("usercity",city);
                editor.putString("userstate",state);
                editor.commit();
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });
        binding.locaticon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("usercity",city);
                editor.putString("userstate",state);
                editor.commit();
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });
        binding.curcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("usercity",city);
                editor.putString("userstate",state);
                editor.commit();
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });
    }

    private void ManageData() {
        hmViewModel = new ViewModelProvider(getActivity()).get(homefragViewModel.class);
        hmViewModel.initwork(userid, "0", "0",sharedPreferences.getString("usercity", ""));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hmViewModel.getCitydata().observe(getViewLifecycleOwner(), new Observer<List<homeResponse.citiesResp>>() {
                    @Override
                    public void onChanged(List<homeResponse.citiesResp> citiesResps) {

                        Log.d("citiesLoaded", "true");
                        citylist1.addAll(citiesResps);
                        binding.citysearch.setVisibility(View.VISIBLE);
                        binding.progressBar5.setVisibility(View.GONE);
                        cityAdapter.notifyDataSetChanged();


                    }
                });
            }
        },0);

        getlatlong();
        cityAdapter=new cityAdapter(getActivity(),citylist1);
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        binding.citysearch.setLayoutManager(llm);
        binding.citysearch.setAdapter(cityAdapter);
        binding.citysearch.setVisibility(View.GONE);
        cityAdapter.setonCityclickListener(new cityAdapter.onCityClick() {
            @Override
            public void oncitynameclick(String city, String state) {
                city=city;
                state=state;
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("usercity",city);
                editor.putString("userstate",state);
                editor.commit();
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();

            }
        });

        binding.citysearchTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //got focus
                } else {
                    if(!binding.citysearchTxt.getText().toString().isEmpty()) {
                        binding.citysearch.setVisibility(View.GONE);
                        searchFromServer(binding.citysearchTxt.getText().toString(), 1);
                    }
                    else {
                    }
                }
            }
        });

        binding.citysearch.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;
            int page = 1;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = dy - ydy;
                ydy = dy;

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == citylist1.size() - 1) {
                    //bottom of list!
                    page = page + 1;
                    searchFromServer(!binding.citysearchTxt.getText().toString().isEmpty() ? binding.citysearchTxt.getText().toString()
                            : "", page);
                }

            }
        });
    }

    private void searchFromServer(String query, Integer page) {
        if(query.equals("") && page == 1) {
            binding.citysearch.setVisibility(View.GONE);
            binding.progressBar5.setVisibility(View.VISIBLE);
        }
        api_baseurl baseurl=new api_baseurl();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);
        Call<homeResponse.listofcities> call2=apiWork.getallcities(query.toString(), page);
        call2.enqueue(new Callback<homeResponse.listofcities>() {
            @Override
            public void onResponse(Call<homeResponse.listofcities> call, Response<homeResponse.listofcities> response) {
                if(!response.isSuccessful()){
                    Log.d("Error code",String.valueOf(response.code()));
                    return;
                }

                homeResponse.listofcities resp=response.body();
                if(resp.getResult()!=null) {
                    if(!query.equals("") && page == 1) {
                        citylist1.clear();
                    }
                    for(int i=0; i<resp.getResult().size();i++) {
                        if(!citylist1.contains(resp.getResult().get(i))) {
                            citylist1.add(resp.getResult().get(i));
                        }
                    }
                    binding.citysearch.setVisibility(View.VISIBLE);
                    binding.progressBar5.setVisibility(View.GONE);
                    cityAdapter.notifyDataSetChanged();

                }
                else {
                    binding.citysearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.listofcities> call, Throwable t) {
                Log.d("Failure_city",t.getMessage());
            }
        });
    }

    private void getlatlong() {
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location != null) {
                                    Geocoder geocoder = null;
                                    if (getContext() != null) {
                                        geocoder = new Geocoder(getActivity()
                                                , Locale.getDefault());
                                    }
                                    try {
                                        if (geocoder != null) {
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                            city = addresses.get(0).getLocality();
                                            state =  addresses.get(0).getAdminArea();
                                            binding.curcity.setText(city+", "+state);

                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {

                                    LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                                    LocationCallback locationCallback = new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            super.onLocationResult(locationResult);
                                            Location location1 = locationResult.getLastLocation();
                                            Geocoder geocoder = null;
                                            if (getContext() != null) {
                                                geocoder = new Geocoder(getActivity()
                                                        , Locale.getDefault());
                                            }
                                            try {
                                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                                city = addresses.get(0).getSubLocality();
                                                state = addresses.get(0).getLocality();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, Looper.getMainLooper());
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }


            }
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}