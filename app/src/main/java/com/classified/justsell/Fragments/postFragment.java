package com.classified.justsell.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classified.justsell.Adapters.post_categoryAdapter;
import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.PostActivity;
import com.classified.justsell.PostActivity_all;
import com.classified.justsell.PostActivity_property;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.postViewModel;
import com.classified.justsell.databinding.FragmentPostBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link postFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class postFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentPostBinding binding;
    private post_categoryAdapter adapter;
    private postViewModel viewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public postFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment postFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static postFragment newInstance(String param1, String param2) {
        postFragment fragment = new postFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPostBinding.inflate(inflater,container,false);
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
        }

        return binding.getRoot();
    }

    private void ManageData() {

        viewModel=new ViewModelProvider(getActivity()).get(postViewModel.class);
        viewModel.initwork();
        viewModel.getCategorydata().observe(getActivity(), new Observer<List<homeResponse.categoryResult>>() {
            @Override
            public void onChanged(List<homeResponse.categoryResult> categoryResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(categoryResults.size()>0) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                },100);
            }
        });
        adapter=new post_categoryAdapter(getActivity(),viewModel.getCategorydata().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        binding.catrec.setLayoutManager(llm);
        binding.catrec.setAdapter(adapter);
        adapter.setonItemclick(new post_categoryAdapter.onItemClick() {
            @Override
            public void ontileClick(String catname) {
                if(catname.equals("automobile") || catname.equals("car") || catname.equals("Bike")) {
                    Intent intent=new Intent(getActivity(), PostActivity.class);
                    intent.putExtra("catname",catname);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }
                else if(catname.equals("property") || catname.equals("Property") || catname.equals("Bike")){
                    Intent intent=new Intent(getActivity(), PostActivity_property.class);
                    intent.putExtra("catname",catname);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }
                else {
                    Intent intent=new Intent(getActivity(), PostActivity_all.class);
                    intent.putExtra("catname",catname);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }
            }
        });

    }
}