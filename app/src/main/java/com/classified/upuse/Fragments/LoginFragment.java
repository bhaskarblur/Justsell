package com.classified.upuse.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classified.upuse.R;
import com.classified.upuse.databinding.FragmentLoginBinding;
import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.HomeActivity;
import com.classified.upuse.Models.AuthResponse;
import com.classified.upuse.helpingCode.progressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentLoginBinding lgbinding;
    private Boolean pressed=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
       lgbinding=FragmentLoginBinding.inflate(inflater,container,false);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
       String status=sharedPreferences.getString("userlogged","");

       if(status.equals("yes")) {
           startActivity(new Intent(getActivity(), HomeActivity.class));
           getActivity().finish();
           getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
       }
       viewfuncs();
        return lgbinding.getRoot();
    }

    private void viewfuncs() {
        lgbinding.loginnextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lgbinding.loginnumber.getText().toString().isEmpty()) {
                    lgbinding.loginnumber.setError("Please enter your number!");
                }
                else if(lgbinding.loginnumber.getText().toString().length()<10) {
                    lgbinding.loginnumber.setError("Please enter a valid number!");
                }

                else {
                    // the api call will be done here
                    if(pressed.equals(false)) {
                        pressed=true;
                        api_baseurl baseurl = new api_baseurl();

                        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        ApiWork apiWork = retrofit.create(ApiWork.class);

                        Call<AuthResponse.SendOtp> call = apiWork.sendotp(lgbinding.loginnumber.getText().toString());

                        call.enqueue(new Callback<AuthResponse.SendOtp>() {
                            @Override
                            public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                              com.classified.upuse.helpingCode.progressDialog dialog = new progressDialog();
                              dialog.showLoadingDialog(getContext(), "Otp", "Sending OTP");
                                if (!response.isSuccessful()) {
                                    Log.d("error code", String.valueOf(response.code()));
                                    pressed=false;
                                    return;
                                }

                                AuthResponse.SendOtp resp = response.body();

                                Log.d("message", resp.getMessage());

                                dialog.hideLoadingDialog();
                                if (resp.getMessage().toString().toLowerCase().contains("successfully") ||
                                resp.getMessage().toString().toLowerCase().contains("wuccess")) {
                                    Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_SHORT).show();
                                    OTPFragment df = new OTPFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("number", lgbinding.loginnumber.getText().toString());
                                    df.setArguments(bundle);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                                    transaction.replace(R.id.auth_fragcontainer, df);
                                    transaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "There was an error!", Toast.LENGTH_SHORT).show();
                                    pressed=false;
                                }

                            }

                            @Override
                            public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                                Log.d("Failure", t.getMessage());
                                pressed=false;
                            }
                        });
                    }
                }
            }
        });
    }
}