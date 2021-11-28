package com.classified.justsell.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.classified.justsell.Adapters.adsAdapter;
import com.classified.justsell.Adapters.bannerAdapter;
import com.classified.justsell.Adapters.categoryAdapter;
import com.classified.justsell.Adapters.selfilterAdapter;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.homefragViewModel;
import com.classified.justsell.ViewModels.postViewModel;
import com.classified.justsell.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class searchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSearchBinding binding;
    private com.classified.justsell.Adapters.adsAdapter adsAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private homefragViewModel hmViewModel;
    private postViewModel postViewModel;
    String userid;
    String city;
    private com.classified.justsell.Adapters.categoryAdapter categoryAdapter;
    private selfilterAdapter filteradapter;
    private String selected_category;
    private List<homeResponse.adsResult> resultList=new ArrayList<>();
    private List<String> filterList=new ArrayList<>();
    private List<String> filterrateList=new ArrayList<>();
    private String filterfield;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
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
        sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
        city=sharedPreferences.getString("usercity","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSearchBinding.inflate(inflater,container,false);

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
            viewfuncs();
        }

        return binding.getRoot();
    }

    private void viewfuncs() {

        binding.pricebox1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        binding.pricebox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });

        binding.openfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_down);
                binding.filterLayout.setVisibility(View.VISIBLE);
                binding.filterLayout.setAnimation(animation);

            }
        });

        binding.closefilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_down);
                binding.filterLayout.setVisibility(View.INVISIBLE);
                binding.filterLayout.setAnimation(animation);
            }
        });

        binding.bybudgetTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.bybudgetTxt.setTextColor(getResources().getColor(R.color.back_black,null));
                binding.bydateTxt.setTextColor(Color.parseColor("#5A5A5A"));

                binding.priceLay.setVisibility(View.VISIBLE);
                binding.dateLay.setVisibility(View.INVISIBLE);
            }
        });

        binding.bydateTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.bydateTxt.setTextColor(getResources().getColor(R.color.back_black,null));
                binding.bybudgetTxt.setTextColor(Color.parseColor("#5A5A5A"));

                binding.priceLay.setVisibility(View.INVISIBLE);
                binding.dateLay.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ManageData() {
        postViewModel=new ViewModelProvider(getActivity()).get(postViewModel.class);
        postViewModel.initwork();
        postViewModel.getCategorydata().observe(getActivity(), new Observer<List<homeResponse.categoryResult>>() {
            @Override
            public void onChanged(List<homeResponse.categoryResult> categoryResults) {
                if(categoryResults.size()>0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            categoryAdapter.notifyDataSetChanged();
                        }
                    },100);
                }
            }
        });
        adsAdapter=new adsAdapter(getActivity(),resultList);
        LinearLayoutManager llm1=new LinearLayoutManager(getActivity());
        binding.searchRec.setLayoutManager(llm1);
        binding.searchRec.setAdapter(adsAdapter);
        binding.searchRec.setVisibility(View.INVISIBLE);

        categoryAdapter=new categoryAdapter(getActivity(),postViewModel.categorydata.getValue());
        LinearLayoutManager llm2=new LinearLayoutManager(getActivity());
        llm2.setOrientation(RecyclerView.HORIZONTAL);
        binding.catRec.setLayoutManager(llm2);
        binding.catRec.setAdapter(categoryAdapter);

        filteradapter=new selfilterAdapter(getActivity(),filterList,filterrateList);
        LinearLayoutManager llm3=new LinearLayoutManager(getActivity());
        llm2.setOrientation(RecyclerView.HORIZONTAL);
        binding.selFilterRec.setLayoutManager(llm3);
        binding.selFilterRec.setAdapter(filteradapter);


        binding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.searchRec.setVisibility(View.INVISIBLE);
                binding.notfoundimg.setVisibility(View.INVISIBLE);
                binding.nothingfoundtxt.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().isEmpty()) {
                    binding.searchRec.setVisibility(View.VISIBLE);
                    binding.notfoundimg.setVisibility(View.INVISIBLE);
                    binding.nothingfoundtxt.setVisibility(View.INVISIBLE);
                   getResultFromServer(s.toString());
                }
                else  {
                    binding.searchRec.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    private void searchfun(String query) {
        List<homeResponse.adsResult> searchedList=new ArrayList<>();
        for(homeResponse.adsResult model:hmViewModel.getAdsdata().getValue()){

            if(filterfield!=null && filterfield!="null" && filterfield!="" && !filterfield.equals("All")) {
                if(model.getAd_title().toLowerCase().contains(query.toLowerCase())
                || model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    if(model.getFeatured_status().equals("1")) {
                        searchedList.add(model);
                    }
                    else {
                        searchedList.add(model);
                    }
                }
            }
            else{
                if(model.getAd_title().toLowerCase().contains(query.toLowerCase()) ||
                model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    if(model.getFeatured_status().equals("1")) {
                        searchedList.add(model);
                    }
                    else {
                        searchedList.add(model);
                    }
                }
            }


        }
        adsAdapter.searchList(searchedList);
        if(searchedList.size()<1) {
            binding.notfoundimg.setVisibility(View.VISIBLE);
            binding.nothingfoundtxt.setVisibility(View.VISIBLE);
        }

    }

    private void getResultFromServer(String keyword) {
        
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        View bottombar=getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.VISIBLE);
    }
}