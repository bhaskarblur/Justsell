package com.classified.upuse.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.HomeActivity;
import com.classified.upuse.Models.AuthResponse;
import com.classified.upuse.R;;
import com.classified.upuse.databinding.FragmentOTPBinding;
import com.classified.upuse.helpingCode.progressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Boolean waitCount = false;
    private FragmentOTPBinding lgbinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String number;
    private String token;
    private Boolean pressed = false;

    public OTPFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OTPFragment newInstance(String param1, String param2) {
        OTPFragment fragment = new OTPFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString("number");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lgbinding = FragmentOTPBinding.inflate(inflater, container, false);

        ManageUI();
        viewfuncs();
        getfirebaseToken();
        return lgbinding.getRoot();

    }
    private void getfirebaseToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("token", msg);
                    }
                });
    }

    private void viewfuncs() {
        lgbinding.changeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment df = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.auth_fragcontainer, df);
                transaction.commit();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitCount = true;
            }
        }, 10000);

        lgbinding.resendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waitCount.equals(true)) {
                    api_baseurl baseurl = new api_baseurl();

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiWork apiWork = retrofit.create(ApiWork.class);

                    Call<AuthResponse.SendOtp> call = apiWork.sendotp(number);

                    call.enqueue(new Callback<AuthResponse.SendOtp>() {
                        @Override
                        public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {

                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }

                            AuthResponse.SendOtp resp = response.body();

                            Log.d("message", resp.getMessage());

                            if (resp.getMessage().toString().contains("success")) {
                                OTPFragment df = new OTPFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("number", number);
                                df.setArguments(bundle);
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade_2);
                                transaction.replace(R.id.auth_fragcontainer, df);
                                transaction.commit();
                            } else {
                                Toast.makeText(getContext(), "There was an error!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                            Log.d("Failure", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please wait for 10 seconds to send another otp.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lgbinding.otpcontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lgbinding.otpbox1.getText().toString().isEmpty() ||
                        lgbinding.otpbox2.getText().toString().isEmpty() ||
                        lgbinding.otpbox3.getText().toString().isEmpty() ||
                        lgbinding.otpbox4.getText().toString().isEmpty()) {

                    Toast.makeText(getActivity(), "Please enter a valid OTP!", Toast.LENGTH_SHORT).show();
                } else {
                    // the api call will be done here

                    if (pressed.equals(false)) {
                        pressed = true;

                        api_baseurl baseurl = new api_baseurl();

                        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        ApiWork apiWork = retrofit.create(ApiWork.class);

                        String otphere = lgbinding.otpbox1.getText().toString() + lgbinding.otpbox2.getText().toString()
                                + lgbinding.otpbox3.getText().toString() + lgbinding.otpbox4.getText().toString();
                        Call<AuthResponse.VerifyOtp> call = apiWork.login(number, otphere,token);

                        com.classified.upuse.helpingCode.progressDialog dialog = new progressDialog();
                        dialog.showLoadingDialog(getContext(), "Verifying", "Verifying OTP");
                        call.enqueue(new Callback<AuthResponse.VerifyOtp>() {
                            @Override
                            public void onResponse(Call<AuthResponse.VerifyOtp> call, Response<AuthResponse.VerifyOtp> response) {

                                if (!response.isSuccessful()) {
                                    Log.d("error code", String.valueOf(response.code()));
                                    Toast.makeText(getActivity(), String.valueOf(response.message()), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                AuthResponse.VerifyOtp resp = response.body();

                                Log.d("message", resp.getMessage());

                                dialog.hideLoadingDialog();
                                pressed = false;
                                try {
                                    if (resp.getMessage().toString().contains("success")) {
                                        if (Objects.equals(resp.getResult().getUser_type(), "new user")) {
                                            // send for profile update
                                            Bundle bundle = new Bundle();
                                            bundle.putString("userid", resp.getResult().getId());
                                            registerProfile df = new registerProfile();
                                            df.setArguments(bundle);
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                                            transaction.replace(R.id.auth_fragcontainer, df);
                                            transaction.commit();
                                        } else if (Objects.equals(resp.getResult().getUser_type(), "old user")){
                                            // send to home
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("userlogged", "yes");
                                            editor.putString("userimage", resp.getResult().getImage());
                                            editor.putString("userid", resp.getResult().getId());
                                            editor.putString("username", resp.getResult().getName().toString());
                                            editor.putString("userstate", resp.getResult().getState().toString());
                                            editor.putString("usercity", resp.getResult().getCity().toString());
                                            editor.putString("usermobile", resp.getResult().getMobile());
                                            editor.commit();

                                            startActivity(new Intent(getActivity(), HomeActivity.class));
                                            getActivity().finish();
                                            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                        }
                                    } else if (resp.getMessage().toString().contains("wrong")) {
                                        Toast.makeText(getContext(), "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "There was an error!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Throwable err) {
                                    Log.d("Error otp", err.getMessage().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<AuthResponse.VerifyOtp> call, Throwable t) {
                                Log.d("Failure", t.getMessage());
                                pressed = false;
                            }
                        });
                    }
                }
            }
        });

//        lgbinding.otpcontButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerProfile df = new registerProfile();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
//                transaction.replace(R.id.auth_fragcontainer, df);
//                transaction.commit();
//            }
//        });
    }

    private void ManageUI() {

        if (number != null) {
            lgbinding.tellnumbTxt.setText("Code was sent at +91 " + number.toString().substring(0, 5) + "*****");
        }

        lgbinding.changeTxt.setPaintFlags(lgbinding.changeTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        CountDownTimer countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long Minutes = millisUntilFinished / (60 * 1000);
                long Seconds = millisUntilFinished / 1000 % 60;

                String count = getColoredSpanned(String.valueOf(Minutes) + ":" + String.valueOf(Seconds), "#12C524");
                lgbinding.otpCounttxt.setText(HtmlCompat.fromHtml(count, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFinish() {
                lgbinding.otpCounttxt2.setText("OTP expired!");
                lgbinding.otpCounttxt.setVisibility(View.INVISIBLE);
            }
        };
        countDownTimer.start();

        lgbinding.otpbox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lgbinding.otpbox2.requestFocus();
                }
            }
        });

        lgbinding.otpbox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lgbinding.otpbox3.requestFocus();
                } else {
                    lgbinding.otpbox1.requestFocus();
                }
            }
        });

        lgbinding.otpbox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lgbinding.otpbox4.requestFocus();
                } else {
                    lgbinding.otpbox2.requestFocus();
                }
            }
        });

        lgbinding.otpbox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lgbinding.otpcontButton.requestFocus();
                } else {
                    lgbinding.otpbox3.requestFocus();
                }
            }
        });
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}