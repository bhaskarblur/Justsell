package com.classified.justsell.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.chatslistAdapter;
import com.classified.justsell.AuthActivity;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.chatModel;
import com.classified.justsell.R;
import com.classified.justsell.ViewModels.chatsViewModel;
import com.classified.justsell.chatActivity;
import com.classified.justsell.databinding.FragmentChatBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentChatBinding binding;
    private chatslistAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private api_baseurl baseurl=new api_baseurl();
    private com.classified.justsell.ViewModels.chatsViewModel chatsViewModel;

    public chatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chatFragment newInstance(String param1, String param2) {
        chatFragment fragment = new chatFragment();
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
        binding=FragmentChatBinding.inflate(inflater,container,false);

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

       // ManageData();
        Viewfuncs();


        return binding.getRoot();
    }

    private void ManageData() {
        String userid;
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
        chatsViewModel=new ViewModelProvider(getActivity()).get(com.classified.justsell.ViewModels.chatsViewModel.class);
        chatsViewModel.initwork(userid);
        chatsViewModel.getDataModel().observe(getActivity(), new Observer<List<chatModel.chatResult>>() {
            @Override
            public void onChanged(List<chatModel.chatResult> chatResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (chatResults.size() > 0) {
                            binding.chatRec.setVisibility(View.VISIBLE);
                            binding.nonotiimg2.setVisibility(View.INVISIBLE);
                            binding.nonotitxt2.setVisibility(View.INVISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            }, 100);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.chatRec.setVisibility(View.INVISIBLE);
                                    binding.nonotiimg2.setVisibility(View.VISIBLE);
                                    binding.nonotitxt2.setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        }
                    }
                },1000);
            }

        });
        adapter=new chatslistAdapter(getActivity(),chatsViewModel.getDataModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        binding.chatRec.setLayoutManager(llm);
        binding.chatRec.setAdapter(adapter);
        adapter.setonItemclick(new chatslistAdapter.onItemClick() {
            @Override
            public void ontileClick(String userid, String person_id, String product_id) {
                Intent intent=new Intent(getActivity(), chatActivity.class);
                intent.putExtra("user_id",userid);
                intent.putExtra("person_id",person_id);
                intent.putExtra("product_id",product_id);
                getActivity().getViewModelStore().clear();
                chatFragment chatFragment=new chatFragment();
                getFragmentManager().beginTransaction().remove(chatFragment).commitAllowingStateLoss();
                getActivity().getViewModelStore().clear();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

            }
        });
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.VerifyOtp> call = apiWork.getprofile(userid);

        call.enqueue(new Callback<AuthResponse.VerifyOtp>() {
            @Override
            public void onResponse(Call<AuthResponse.VerifyOtp> call, Response<AuthResponse.VerifyOtp> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code", String.valueOf(response.code()));
                    return;
                }

                AuthResponse.VerifyOtp resp = response.body();

                if (resp.getResult() != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userlogged", "yes");
                    editor.putString("userimage", resp.getResult().getImage());
                    editor.putString("userid", resp.getResult().getId());
                    editor.putString("usermobile", resp.getResult().getMobile());
                    editor.putString("username", resp.getResult().getName());
                    editor.commit();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(getActivity(), AuthActivity.class));
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse.VerifyOtp> call, Throwable t) {
                Log.d("Failure", t.getMessage());
            }
        });
    }

    private void Viewfuncs() {

        binding.swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getViewModelStore().clear();
                binding.swipelayout.setRefreshing(false);
                chatFragment homeFragment=new chatFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragment, homeFragment);
                transaction.commit();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        ManageData();
    }
}