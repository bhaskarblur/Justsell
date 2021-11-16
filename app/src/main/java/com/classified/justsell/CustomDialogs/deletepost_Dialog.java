package com.classified.justsell.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.HomeActivity;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.R;
import com.classified.justsell.adposted_successful;
import com.classified.justsell.promote_ad;
import com.google.android.gms.common.api.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class deletepost_Dialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.deletepost_dialog,null);
        builder.setView(view);

        View delbtn=view.findViewById(R.id.delbtn_done);
        View cancbtn=view.findViewById(R.id.cancbtn);
        RadioButton reason1=view.findViewById(R.id.radio1);
        RadioButton reason2=view.findViewById(R.id.radio2);
        RadioButton reason3=view.findViewById(R.id.radio3);
        RadioButton reason4=view.findViewById(R.id.radio4);
        api_baseurl baseurl=new api_baseurl();
        Bundle bundle=getArguments();
        String ad_id=bundle.getString("ad_id");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        String userid=sharedPreferences.getString("userid","");
        final String[] reason = new String[1];


        reason1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    reason[0] ="Found buyer on Justsell";
                    reason2.setChecked(false);
                    reason3.setChecked(false);
                    reason4.setChecked(false);
                }
            }
        });

        reason2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    reason[0] ="Didn't find buyer on Justsell";
                    reason1.setChecked(false);
                    reason3.setChecked(false);
                    reason4.setChecked(false);
                }
            }
        });

        reason3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    reason[0] ="Found buyer somewhere else";
                    reason1.setChecked(false);
                    reason2.setChecked(false);
                    reason4.setChecked(false);
                }
            }
        });

        reason4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    reason[0] ="Don't want to sell anymore";
                    reason1.setChecked(false);
                    reason2.setChecked(false);
                    reason3.setChecked(false);
                }
            }
        });

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the ad

                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);

                Call<AuthResponse.SendOtp> call1 = apiWork.delete_post(ad_id,userid,reason[0]);

                call1.enqueue(new Callback<AuthResponse.SendOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                        if(!response.isSuccessful()) {
                            Log.d("error code",String.valueOf(response.code()));
                            return;
                        }

                        AuthResponse.SendOtp resp=response.body();

                        if(resp.getCode().equals("200")) {
                            Toast.makeText(getContext(), "Post Deleted!", Toast.LENGTH_SHORT).show();
                            getActivity().getViewModelStore().clear();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });

        cancbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        return builder.create();

    }
}
