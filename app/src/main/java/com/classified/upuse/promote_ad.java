package com.classified.upuse;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Toast;

import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.Models.AdsModel;
import com.classified.upuse.Models.AuthResponse;
import com.classified.upuse.Models.homeResponse;
import com.classified.upuse.ViewModels.AdsViewModel;
import com.classified.upuse.databinding.ActivityPromoteAdBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class promote_ad extends AppCompatActivity {
    private ActivityPromoteAdBinding binding;
    private api_baseurl baseurl=new api_baseurl();
    String adid;
    String userid;
    String cost;
    String reach;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private int dailyBudget = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPromoteAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        View decorView = getWindow().getDecorView();
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getWindow().getDecorView().getWindowInsetsController().
                            setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
                break;
        }

        ManageData();
        viewfunc();
    }

    private void ManageData() {
        loadfunc();
        Intent intent=getIntent();
        adid=intent.getStringExtra("ad_id");
        SharedPreferences sharedPreferences=getSharedPreferences("userlogged",0);
         userid=sharedPreferences.getString("userid","");
        AdsViewModel adsViewModel;
        adsViewModel = new ViewModelProvider(promote_ad.this).get(AdsViewModel.class);
        adsViewModel.initwork(adid, "prod_name",userid);

        adsViewModel.getDataModel().observe(promote_ad.this, new Observer<AdsModel.adsResult>() {
            @Override
            public void onChanged(AdsModel.adsResult adsResult) {
                if(adsResult!=null) {
                    binding.adsTitle.setText(adsResult.getAd_title());
                    if(adsResult.getImages().size()>0) {
                        final int radius = 13;
                        final int margin = 0;
                        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                        Picasso.get().load(adsResult.getImages().get(0).getImage()).transform(transformation).resize(200, 200).into(binding.adsImage);
                    }
                    binding.adsPrice.setText("₹ "+adsResult.getSelling_price());
                    binding.adsDescr.setText(adsResult.getDescription());
                    binding.adsDate.setText("Posted on "+adsResult.getPosted_date());
                }
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
                String dateString = myCalendar.getTime().getDate()+"/"+
                        myCalendar.getTime().getMonth()+"/"+myCalendar.getTime().getYear();
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year
                Date d1 = null;
                Date dateObj=null;
                try {
                    d1 = sdf1.parse(dateString);
                    String today=Calendar.getInstance().getTime().getDate()+"/"+
                            Calendar.getInstance().getTime().getMonth()+"/"+Calendar.getInstance().getTime().getYear();
                    dateObj = sdf1.parse(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String myFormat = "dd/MM/yy"; //In which you need put here
                if (d1.before(dateObj)) {
                    Toast.makeText(promote_ad.this, "Please select a date in future.", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    if (clickcheck[0] == 0) {
                        binding.datetxt.setText(sdf.format(myCalendar.getTime()));
                        docalculation();
                    } else {
                        try {
                            d1 = sdf1.parse(binding.datetxt.getText().toString());
                            String enddate=myCalendar.getTime().getDate()+"/"+
                                    myCalendar.getTime().getMonth()+"/"+myCalendar.getTime().getYear();
                            dateObj = sdf1.parse(enddate);

                            if(dateObj.before(d1)) {
                                binding.datetxtEnd.setText("Select Date");
                                Toast.makeText(promote_ad.this, "Please select end date in future of start date.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                binding.datetxtEnd.setText(sdf.format(myCalendar.getTime()));
                                docalculation();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        };

        binding.datepicklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              DatePickerDialog dp =  new DatePickerDialog(promote_ad.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
              dp.getDatePicker().setMinDate(System.currentTimeMillis());
              dp.show();
                clickcheck[0] =0;
            }
        });

        binding.datepicklayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(promote_ad.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMinDate(System.currentTimeMillis());
                dp.show();
                clickcheck[0] =1;
            }
        });
    }

    private void docalculation() {

        if(!binding.datetxtEnd.getText().toString().equals("Select Date") && !binding.datetxt.getText().toString().equals("Select Date")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year
                Date d1 = sdf.parse(binding.datetxt.getText().toString());
                Date d2 = sdf.parse(binding.datetxtEnd.getText().toString());
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(d1.getYear(),d1.getMonth(),d1.getDate());
                Calendar calendar2=Calendar.getInstance();
                calendar2.set(d2.getYear(),d2.getMonth(),d2.getDate());
                long diff=d2.getTime()-d1.getTime();
                if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)>0) {
                    cost = String.valueOf(Math.round((float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) * (float)dailyBudget));
                    binding.prombudTxt.setText("Promotion Budget: Rs " + cost);
                }
                else {
                    binding.datetxtEnd.setText("Select Date");
                    Toast.makeText(promote_ad.this, "Please select end date in future of start date.", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void getReach(String citysel) {
        api_baseurl baseurl = new api_baseurl();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<homeResponse.reachresult> call = apiWork.get_reach(citysel);

        call.enqueue(new Callback<homeResponse.reachresult>() {
            @Override
            public void onResponse(Call<homeResponse.reachresult> call, Response<homeResponse.reachresult> response) {
                if(!response.isSuccessful()){
                    Log.d("error code",String.valueOf(response.code()));
                    return;
                }

                if(response.body().getReach()!=null) {
                    reach=response.body().getReach();
                    binding.estimTxt.setText("Estimated Reach*:    " + reach);
                }
            }

            @Override
            public void onFailure(Call<homeResponse.reachresult> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }
    private void viewfunc() {

        binding.promoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.datetxt.getText().toString().equals("Select Date")) {
                    binding.datepicklay.setFocusable(true);
                    binding.datepicklay.requestFocus();
                    Toast.makeText(promote_ad.this, "Please select a start date.", Toast.LENGTH_SHORT).show();
                }
               else if(binding.datetxtEnd.getText().toString().equals("Select Date")) {
                    binding.datepicklayEnd.setFocusable(true);
                    binding.datepicklayEnd.requestFocus();
                    Toast.makeText(promote_ad.this, "Please select a end date.", Toast.LENGTH_SHORT).show();
                }
               else if(binding.statet.getText().toString().equals("Select State")) {
                    binding.statespin.setFocusable(true);
                    binding.statespin.requestFocus();
                    Toast.makeText(promote_ad.this, "Please select a state.", Toast.LENGTH_SHORT).show();
                }
               else  if(binding.cityet.getText().toString().equals("Select City")) {
                    binding.cityspin.setFocusable(true);
                    binding.cityspin.requestFocus();
                    Toast.makeText(promote_ad.this, "Please select a city.", Toast.LENGTH_SHORT).show();
                }
               else {
                   // Need to add stripe here
                    // And then call promote api on success
                    confirmPromotion();

                }
            }
        });

        binding.budgetSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dailyBudget = i;
                binding.dailybudget.setText("Daily Budget: Rs "+i);
                docalculation();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.backbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadfunc() {
        api_baseurl baseurl = new api_baseurl();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.getstate> call = apiWork.getstate();

        call.enqueue(new Callback<AuthResponse.getstate>() {
            @Override
            public void onResponse(Call<AuthResponse.getstate> call, Response<AuthResponse.getstate> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code_state:", String.valueOf(response.code()));
                    return;
                }

                AuthResponse.getstate statedata = response.body();
                ArrayList<String> statelist = new ArrayList<>();
                statelist.add("Select State");

                if (statedata.getResult() != null) {
                    for (int i = 0; i < statedata.getResult().size(); i++) {
                        statelist.add(statedata.getResult().get(i).getStatename());
                    }
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(promote_ad.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    binding.statespin.setAdapter(listAdapter);
                    binding.statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!parent.getItemAtPosition(position).equals("Select State")) {
                                binding.statet.setText(parent.getItemAtPosition(position).toString());
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);
                                Call<AuthResponse.getcity> call = apiWork.getcity(statedata.getResult().get(position - 1).getStatename());

                                call.enqueue(new Callback<AuthResponse.getcity>() {
                                    @Override
                                    public void onResponse(Call<AuthResponse.getcity> call, Response<AuthResponse.getcity> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d("error code:", String.valueOf(response.code()));
                                            return;
                                        }

                                        AuthResponse.getcity citydata = response.body();
                                        ArrayList<String> citylist = new ArrayList<>();
                                        citylist.add("Select City");
                                        if (citydata.getResult() != null) {
                                            for (int i = 0; i < citydata.getResult().size(); i++) {
                                                citylist.add(citydata.getResult().get(i).getCity());
                                            }
                                            ArrayAdapter<String> citylistAdapter = new ArrayAdapter<String>(promote_ad.this, R.layout.support_simple_spinner_dropdown_item,
                                                    citylist);
                                            binding.cityspin.setAdapter(citylistAdapter);
                                            binding.cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    if (!parent.getItemAtPosition(position).equals("Select City")) {
                                                        binding.cityet.setText(parent.getItemAtPosition(position).toString());
                                                       getReach(binding.cityet.getText().toString());
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<AuthResponse.getcity> call, Throwable t) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AuthResponse.getstate> call, Throwable t) {

            }
        });

    }

    private void confirmPromotion() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.SendOtp> call1 = apiWork.do_promotion(adid,userid,binding.datetxt.getText().toString(),
                binding.datetxtEnd.getText().toString(),binding.cityet.getText().toString(),cost,reach);

        binding.progressBar5.setVisibility(View.VISIBLE);
         binding.promoteBtn.setVisibility(View.INVISIBLE);
        call1.enqueue(new Callback<AuthResponse.SendOtp>() {
            @Override
            public void onResponse(Call<AuthResponse.SendOtp> call, Response<AuthResponse.SendOtp> response) {
                binding.progressBar5.setVisibility(View.INVISIBLE);
                binding.promoteBtn.setVisibility(View.VISIBLE);
                if(!response.isSuccessful()) {
                    Log.d("error code",String.valueOf(response.code()));
                    Toast.makeText(promote_ad.this, "There was an error "+response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthResponse.SendOtp resp=response.body();

                if(resp.getCode().equals("200")) {
                    Toast.makeText(promote_ad.this, "Ad Promoted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(promote_ad.this, adpromoted_successful.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                Log.d("Failure",t.getMessage());
                binding.progressBar5.setVisibility(View.INVISIBLE);
                binding.promoteBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status = new String();
        if (data != null) {
            if (data.getData() != null) {
                status = data.getDataString().toLowerCase();
            }
            if (requestCode == RESULT_OK) {
                try {
                    if (status.equals("success")) {
                        String resper = data.getStringExtra("response");
                        Log.d("response", resper);
                        ArrayList<String> breakresp = new ArrayList<>();
                        breakresp.add(resper);
                        upiPaymentDataOperation(breakresp);
                    } else {
                        Toast.makeText(promote_ad.this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(promote_ad.this, "There Was An Error While Paying.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void upiPaymentDataOperation(ArrayList<String> breakresp) {
        String str = breakresp.get(0);
        Log.d("description:", str);
        String paycancel = "";
        String status = "";
        String approvalrefno = "";
        String resp[] = str.split("&");
        for (int i = 0; i < resp.length; i++) {
            String equalstr[] = resp[i].split("=");
            if (equalstr.length >= 2) {
                if (equalstr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalstr[1].toLowerCase();
                } else if (equalstr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) ||
                        equalstr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalrefno = equalstr[1];
                } else {
                    paycancel = "Payment Cancelled By User.";
                }
            }

            if (status.equals("success")) {
                Log.d("success with RefNo:", approvalrefno);
                Toast.makeText(promote_ad.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                confirmPromotion();
            }
        }

    }

    @Override
    public void finish() {
        super.finish();
        getViewModelStore().clear();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}