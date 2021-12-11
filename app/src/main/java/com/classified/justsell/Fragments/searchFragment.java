package com.classified.justsell.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Ad_posterActivity;
import com.classified.justsell.Ad_userActivity;
import com.classified.justsell.Adapters.adsAdapter;
import com.classified.justsell.Adapters.bannerAdapter;
import com.classified.justsell.Adapters.categoryAdapter;
import com.classified.justsell.Adapters.filtercategoryAdapter;
import com.classified.justsell.Adapters.selfilterAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.homefragViewModel;
import com.classified.justsell.ViewModels.postViewModel;
import com.classified.justsell.databinding.FragmentSearchBinding;
import com.classified.justsell.promote_ad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.bendik.simplerangeview.SimpleRangeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private com.classified.justsell.Adapters.filtercategoryAdapter categoryAdapter;
    private selfilterAdapter filteradapter;
    private String selected_category;
    private List<homeResponse.adsResult> resultList = new ArrayList<>();
    private List<String> filterList = new ArrayList<>();
    private List<String> filterrateList = new ArrayList<>();
    private String filterfield;
    private api_baseurl baseurl=new api_baseurl();
    private String price_sort = "low_to_high";
    private Boolean pricefilter = false;
    private Boolean datefilter = false;
    private Boolean catfilter = false;

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
        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
        city = sharedPreferences.getString("usercity", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        View bottombar = getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.GONE);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
            noInternetFragment nocon = new noInternetFragment();
            FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction1.setCustomAnimations(R.anim.fade_2, R.anim.fade);
            transaction1.replace(R.id.mainFragment, nocon);
            transaction1.addToBackStack("A");
            transaction1.commit();
        } else {
            ManageData();
            viewfuncs();
        }

        return binding.getRoot();
    }

    private void viewfuncs() {

        binding.applyFitler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterList.contains("Budget:")) {
                    pricefilter = true;
                }
                if (filterList.contains("Category:")) {
                    catfilter = true;
                }
                if (filterList.contains("Date:")) {
                    datefilter = true;
                }
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_down);
                binding.fillay.setVisibility(View.INVISIBLE);
                binding.fillay.setAnimation(animation);
            }
        });
        binding.lowHighBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price_sort = "low_to_high";
                binding.lowHighBox.setBackgroundResource(R.drawable.backgroundbg_yellow);
                binding.highLowBox.setBackgroundResource(R.drawable.backgroundbg_white);
            }
        });
        binding.highLowBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price_sort = "high_to_low";
                binding.highLowBox.setBackgroundResource(R.drawable.backgroundbg_yellow);
                binding.lowHighBox.setBackgroundResource(R.drawable.backgroundbg_white);
            }
        });
        binding.priceSeekbar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i) {
                binding.pricebox1.setText(String.valueOf(i * 1000));

                if (!binding.pricebox1.getText().toString().isEmpty() &&
                        !binding.pricebox.getText().toString().isEmpty()) {
                    if (!filterList.contains("Budget:")) {
                        filterList.add("Budget:");
                        filterrateList.add(binding.pricebox1.getText().toString() + "-" +
                                binding.pricebox.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    } else {
                        for (int x = 0; x < filterList.size(); x++) {
                            if (filterList.get(x).equals("Budget:")) {
                                filterrateList.remove(x);
                                filterList.remove(x);

                            }
                        }
                        filterList.add("Budget:");
                        filterrateList.add(binding.pricebox1.getText().toString() + "-" +
                                binding.pricebox.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onEndRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i) {
                binding.pricebox.setText(String.valueOf(i * 1000));
                if (i > 30) {

                    binding.pricebox.setText("30000+");
                }

                if (!binding.pricebox1.getText().toString().isEmpty() &&
                        !binding.pricebox.getText().toString().isEmpty()) {
                    if (!filterList.contains("Budget:")) {
                        filterList.add("Budget:");
                        filterrateList.add(binding.pricebox1.getText().toString() + "-" +
                                binding.pricebox.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    } else {
                        for (int x = 0; x < filterList.size(); x++) {
                            if (filterList.get(x).equals("Budget:")) {
                                filterrateList.remove(x);
                                filterList.remove(x);

                            }
                        }
                        filterList.add("Budget:");
                        filterrateList.add(binding.pricebox1.getText().toString() + "-" +
                                binding.pricebox.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    }
                }


            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList.clear();
                filterrateList.clear();
                filteradapter.notifyDataSetChanged();

                pricefilter = false;
                datefilter = false;
                catfilter = false;
            }
        });
        binding.pricebox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                    if(!filterList.contains("Budget:")) {
//                        filterList.add("Budget:");
//                        filterrateList.add(binding.pricebox.getText().toString() + "-" +
//                                binding.pricebox1.getText().toString());
//                        filteradapter.notifyDataSetChanged();
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "Remove the previous budget filter to add a new one", Toast.LENGTH_SHORT).show();
//                    }

            }
        });

        binding.pricebox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final int[] clickcheck = {0};
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if (clickcheck[0] == 0) {
                    binding.datetxtStart.setText(sdf.format(myCalendar.getTime()));
                } else {
                    binding.datetxtEnd.setText(sdf.format(myCalendar.getTime()));
                }

                if (!binding.datetxtEnd.getText().toString().equals("Select Date") &&
                        !binding.datetxtStart.getText().toString().equals("Select Date")) {

                    if (!filterList.contains("Date:")) {
                        filterList.add("Date:");
                        filterrateList.add(binding.datetxtStart.getText().toString() + "-"
                                + binding.datetxtEnd.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    } else {
                        for (int x = 0; x < filterList.size(); x++) {
                            if (filterList.get(x).equals("Date:")) {
                                filterList.remove(x);
                                filterrateList.remove(x);
                            }
                        }
                        filterList.add("Date:");
                        filterrateList.add(binding.datetxtStart.getText().toString() + "-"
                                + binding.datetxtEnd.getText().toString());
                        filteradapter.notifyDataSetChanged();
                    }
                }

            }

        };


        binding.datepicklayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] = 0;
            }
        });
        binding.datepicklayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] = 1;
            }
        });
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
                homeFragment homeFragment = new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });

        binding.openfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_down);
                binding.fillay.setVisibility(View.VISIBLE);
                binding.fillay.setAnimation(animation);

            }
        });

        binding.closefilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_down);
                binding.fillay.setVisibility(View.INVISIBLE);
                binding.fillay.setAnimation(animation);
            }
        });

        binding.bybudgetTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.bybudgetTxt.setTextColor(getResources().getColor(R.color.back_black, null));
                binding.bydateTxt.setTextColor(Color.parseColor("#5A5A5A"));

                binding.priceLay.setVisibility(View.VISIBLE);
                binding.dateLay.setVisibility(View.INVISIBLE);
            }
        });

        binding.bydateTxt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.bydateTxt.setTextColor(getResources().getColor(R.color.back_black, null));
                binding.bybudgetTxt.setTextColor(Color.parseColor("#5A5A5A"));

                binding.priceLay.setVisibility(View.INVISIBLE);
                binding.dateLay.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ManageData() {
        postViewModel = new ViewModelProvider(getActivity()).get(postViewModel.class);
        postViewModel.initwork();
        postViewModel.getCategorydata().observe(getActivity(), new Observer<List<homeResponse.categoryResult>>() {
            @Override
            public void onChanged(List<homeResponse.categoryResult> categoryResults) {
                if (categoryResults.size() > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }, 100);
                }
            }
        });
        adsAdapter = new adsAdapter(getActivity(), resultList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        binding.searchRec.setLayoutManager(llm1);
        binding.searchRec.setAdapter(adsAdapter);
        binding.searchRec.setVisibility(View.INVISIBLE);
        adsAdapter.setonItemClick(new adsAdapter.onItemClick() {
            @Override
            public void onAdClick(String category_name, String ad_id, String prod_name, String userid) {
                Intent intent=null;
                if (!userid.equals(userid)) {
                    intent = new Intent(getActivity(), Ad_userActivity.class);

                } else {
                    // change this to same user activity
                    intent = new Intent(getActivity(), Ad_posterActivity.class);
                }

                intent.putExtra("cat_name", category_name);
                intent.putExtra("ad_id", ad_id);
                intent.putExtra("product_name", prod_name);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });

        categoryAdapter = new filtercategoryAdapter(getActivity(), postViewModel.categorydata.getValue());
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm2.setOrientation(RecyclerView.HORIZONTAL);
        binding.catRec.setLayoutManager(llm2);
        binding.catRec.setAdapter(categoryAdapter);
        categoryAdapter.setoncardclicklistener(new filtercategoryAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                selected_category = catname;
                if (!filterList.contains("Category:")) {
                    filterList.add("Category:");
                    filterrateList.add(selected_category);
                    filteradapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < filterList.size(); i++) {
                        if (filterList.get(i).equals("Category:")) {
                            filterList.remove(i);
                            filterrateList.remove(i);
                        }
                    }
                    filterList.add("Category:");
                    filterrateList.add(selected_category);
                    filteradapter.notifyDataSetChanged();
                }

            }
        });

        filteradapter = new selfilterAdapter(getActivity(), filterList, filterrateList);
        LinearLayoutManager llm3 = new LinearLayoutManager(getActivity());
        llm3.setOrientation(RecyclerView.HORIZONTAL);
        binding.selFilterRec.setLayoutManager(llm3);
        binding.selFilterRec.setAdapter(filteradapter);
        filteradapter.setoncardclicklistener(new selfilterAdapter.oncardclicklistener() {
            @Override
            public void onremoveclick(int position) {
                if (filterList.get(position).equals("Category:")) {
                    catfilter = false;
                } else if (filterList.get(position).equals("Budget:")) {
                    pricefilter = false;
                } else if (filterList.get(position).equals("Date:")) {
                    datefilter = false;
                }
                filterrateList.remove(position);
                filterList.remove(position);
                filteradapter.notifyDataSetChanged();
            }
        });


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
                } else {
                    binding.searchRec.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    private void searchfun(String query) {
        List<homeResponse.adsResult> searchedList = new ArrayList<>();
        for (homeResponse.adsResult model : hmViewModel.getAdsdata().getValue()) {

            if (filterfield != null && filterfield != "null" && filterfield != "" && !filterfield.equals("All")) {
                if (model.getAd_title().toLowerCase().contains(query.toLowerCase())
                        || model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    if (model.getFeatured_status().equals("1")) {
                        searchedList.add(model);
                    } else {
                        searchedList.add(model);
                    }
                }
            } else {
                if (model.getAd_title().toLowerCase().contains(query.toLowerCase()) ||
                        model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {

                    if (model.getFeatured_status().equals("1")) {
                        searchedList.add(model);
                    } else {
                        searchedList.add(model);
                    }
                }
            }


        }
        adsAdapter.searchList(searchedList);
        if (searchedList.size() < 1) {
            binding.notfoundimg.setVisibility(View.VISIBLE);
            binding.nothingfoundtxt.setVisibility(View.VISIBLE);
        }

    }

    private void getResultFromServer(String keyword) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.ListadsResp> call1 = null;
        if (catfilter.equals(true) && pricefilter.equals(true) && datefilter.equals(true)) {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , selected_category, binding.pricebox1.getText().toString(), binding.pricebox.getText().toString(), price_sort,
                    binding.datetxtStart.getText().toString(), binding.datetxtEnd.getText().toString(),city);


        }
        else if(catfilter.equals(true) && pricefilter.equals(true)) {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , selected_category, binding.pricebox1.getText().toString(), binding.pricebox.getText().toString(), price_sort,
                    null,null,city);

        }
         else if(datefilter.equals(true) && pricefilter.equals(true)) {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , null, binding.pricebox1.getText().toString(), binding.pricebox.getText().toString(), price_sort,
                    binding.datetxtStart.getText().toString(), binding.datetxtEnd.getText().toString(),city);

        }
         else if(catfilter.equals(true) && datefilter.equals(true)) {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , selected_category,null,null,null,
                    binding.datetxtStart.getText().toString(), binding.datetxtEnd.getText().toString(),city);
        }
         else if(catfilter.equals(true)) {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , selected_category,null,null,null,
                    null,null,city);
        }
         else if(datefilter.equals(true)){
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , null, null,null,null,
                    binding.datetxtStart.getText().toString(), binding.datetxtEnd.getText().toString(),city);
         }
         else if(pricefilter.equals(true)){
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , null, binding.pricebox1.getText().toString(), binding.pricebox.getText().toString(), price_sort,
                    null,null,city);
        }
         else {
            call1 = apiWork.search_ads(userid, binding.searchTxt.getText().toString()
                    , null,null,null, null,null,null,city);
        }


        call1.enqueue(new Callback<homeResponse.ListadsResp>() {
            @Override
            public void onResponse(Call<homeResponse.ListadsResp> call, Response<homeResponse.ListadsResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                if (response.body().getResult() != null) {
                    resultList.clear();
                    if (response.body().getResult().size() > 0) {
                        binding.searchRec.setVisibility(View.VISIBLE);
                        binding.notfoundimg.setVisibility(View.INVISIBLE);
                        binding.nothingfoundtxt.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            resultList.add(response.body().getResult().get(i));
                        }
                        adsAdapter.notifyDataSetChanged();
                    } else {
                        binding.searchRec.setVisibility(View.INVISIBLE);
                        binding.notfoundimg.setVisibility(View.VISIBLE);
                        binding.nothingfoundtxt.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<homeResponse.ListadsResp> call, Throwable t) {
                Log.d("Failure", t.getMessage());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View bottombar = getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.VISIBLE);
    }
}