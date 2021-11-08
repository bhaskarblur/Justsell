package com.classified.justsell.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classified.justsell.Adapters.adsAdapter;
import com.classified.justsell.Adapters.bannerAdapter;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.homefragViewModel;
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
    String userid;
    String city;
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
        ManageData();
        viewfuncs();
        return binding.getRoot();
    }

    private void viewfuncs() {
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
    }

    private void ManageData() {
        hmViewModel=new ViewModelProvider(getActivity()).get(homefragViewModel.class);
        hmViewModel.initwork(userid,"0","0",city);
        adsAdapter=new adsAdapter(getActivity(),hmViewModel.getAdsdata().getValue());
        LinearLayoutManager llm1=new LinearLayoutManager(getActivity());
        binding.searchRec.setLayoutManager(llm1);
        binding.searchRec.setAdapter(adsAdapter);
        binding.searchRec.setVisibility(View.INVISIBLE);
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
                    searchfun(s.toString());
                }
                else  {
                    binding.searchRec.setVisibility(View.INVISIBLE);
                }
            }

        });
        hmViewModel.getAdsdata().observe(getActivity(), new Observer<List<homeResponse.adsResult>>() {
            @Override
            public void onChanged(List<homeResponse.adsResult> adsResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(adsResults.size()>0) {
                            adsAdapter.notifyDataSetChanged();
                        }
                    }
                },100);
            }
        });
    }

    private void searchfun(String query) {
        List<homeResponse.adsResult> searchedList=new ArrayList<>();
        for(homeResponse.adsResult model:hmViewModel.getAdsdata().getValue()){

            if(filterfield!=null && filterfield!="null" && filterfield!="" && !filterfield.equals("All")) {
                if(model.getAd_title().toLowerCase().contains(query.toLowerCase())
                || model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    searchedList.add(model);
                }
            }
            else{
                if(model.getAd_title().toLowerCase().contains(query.toLowerCase()) ||
                model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    searchedList.add(model);
                }
            }

            if(searchedList.size()<1) {
                binding.notfoundimg.setVisibility(View.VISIBLE);
                binding.nothingfoundtxt.setVisibility(View.VISIBLE);
            }


        }
        adsAdapter.searchList(searchedList);
    }
}