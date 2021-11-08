package com.classified.justsell.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.SharedMemory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classified.justsell.Adapters.notiAdapter;
import com.classified.justsell.R;
import com.classified.justsell.databinding.FragmentNotiBinding;

public class notiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentNotiBinding binding;
    private com.classified.justsell.Adapters.notiAdapter notiAdapter;

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
        ManageData();
        viewfunc();
        return binding.getRoot();
    }

    private void viewfunc() {
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

    }
}