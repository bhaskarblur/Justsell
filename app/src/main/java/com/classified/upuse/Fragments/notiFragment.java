package com.classified.upuse.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classified.upuse.Ad_posterActivity;
import com.classified.upuse.Adapters.notiAdapter;
import com.classified.upuse.Models.homeResponse;
import com.classified.upuse.R;
import com.classified.upuse.databinding.FragmentNotiBinding;
import com.classified.upuse.helpingCode.progressDialog;

import java.util.List;

public class notiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentNotiBinding binding;
    private com.classified.upuse.Adapters.notiAdapter notiAdapter;
    private com.classified.upuse.ViewModels.homefragViewModel homefragViewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private String userid;

    public notiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static notiFragment newInstance(String param1, String param2) {
        notiFragment fragment = new notiFragment();
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
        sharedPreferences= getActivity().getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentNotiBinding.inflate(inflater,container,false);
        View bottombar=getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.GONE);
        ManageData();
        viewfunc();
        return binding.getRoot();
    }

    private void viewfunc() {

        binding.swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getViewModelStore().clear();
                binding.swipelayout.setRefreshing(false);
               // notiFragment homeFragment=new notiFragment();
               // FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
               // transaction.replace(R.id.mainFragment, homeFragment);
              //  transaction.commit();

            }
        });
        binding.backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment homeFragment=new homeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();
            }
        });
    }

    private void ManageData() {
        progressDialog progressdialog = new progressDialog();
        progressdialog.showLoadingDialog(getContext(), "Loading",
                "Loading Feed. Please wait");

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        String city=sharedPreferences.getString("usercity","");
        homefragViewModel=new ViewModelProvider(getActivity()).get(com.classified.upuse.ViewModels.homefragViewModel.class);
        homefragViewModel.initwork(userid,"","",city);
        homefragViewModel.getNotidata().observe(getActivity(), new Observer<List<homeResponse.notiResult>>() {
            @Override
            public void onChanged(List<homeResponse.notiResult> notiResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressdialog.hideLoadingDialog();
                        if(notiResults.size()>0){
                            notiAdapter.notifyDataSetChanged();
                            binding.notirec.setVisibility(View.VISIBLE);
                            binding.nonotiimg.setVisibility(View.INVISIBLE);
                            binding.nonotitxt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            binding.notirec.setVisibility(View.INVISIBLE);
                            binding.nonotiimg.setVisibility(View.VISIBLE);
                            binding.nonotitxt.setVisibility(View.VISIBLE);
                        }
                    }
                },100);
            }
        });

        notiAdapter=new notiAdapter(getContext(),homefragViewModel.getNotidata().getValue());
        LinearLayoutManager llm1=new LinearLayoutManager(getContext());

        binding.notirec.setLayoutManager(llm1);
        binding.notirec.setAdapter(notiAdapter);
        notiAdapter.setontileClick(new notiAdapter.ontileclick() {
            @Override
            public void onItemClick(String ad_id,String prodname) {
                Intent intent= new Intent(getContext(), Ad_posterActivity.class);
                intent.putExtra("ad_id",ad_id);
                intent.putExtra("product_name",prodname);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View bottombar=getActivity().findViewById(R.id.bottomnav);
        bottombar.setVisibility(View.VISIBLE);
    }
}