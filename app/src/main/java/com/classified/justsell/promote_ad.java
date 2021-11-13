package com.classified.justsell;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.databinding.ActivityPromoteAdBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class promote_ad extends AppCompatActivity {
    private ActivityPromoteAdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPromoteAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        ManageData();
        viewfunc();
    }

    private void ManageData() {
        loadfunc();
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
                if(clickcheck[0]==0) {
                    binding.datetxt.setText(sdf.format(myCalendar.getTime()));
                }
                else {
                    binding.datetxtEnd.setText(sdf.format(myCalendar.getTime()));
                }
            }

        };

        binding.datepicklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(promote_ad.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] =0;
            }
        });

        binding.datepicklayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(promote_ad.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] =1;
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
                   // Api Call
                    startActivity(new Intent(promote_ad.this, adposted_successful.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                   finish();
                }
            }
        });
    }

    private void loadfunc() {
        api_baseurl baseurl = new api_baseurl();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl_market)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<AuthResponse.getstate> call = apiWork.getstate();

        call.enqueue(new Callback<AuthResponse.getstate>() {
            @Override
            public void onResponse(Call<AuthResponse.getstate> call, Response<AuthResponse.getstate> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code:", String.valueOf(response.code()));
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
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl_market)
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);
                                Call<AuthResponse.getcity> call = apiWork.getcity(statedata.getResult().get(position - 1).getId());

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}