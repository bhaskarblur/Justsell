package com.classified.justsell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.imagesAdapter;
import com.classified.justsell.Adapters.textonlyAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.CustomDialogs.askBoost_Dialog;
import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.databinding.ActivityPostBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private textonlyAdapter fuelAdapter;
    private textonlyAdapter transAdapter;
    private textonlyAdapter nowownAdapter;
    private imagesAdapter imagesAdapter;
    private Boolean show_number=false;
    private String fuel;
    private String transmission;
    private String numowner;
    private List<String> imagesList = new ArrayList<>();
    private api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        
        ManageData();
        viewfunc();
    }

    private void ManageData() {
        Intent intent=getIntent();
        String catname=intent.getStringExtra("catname");
        binding.catname.setText(catname);
        List<String> fuelList = new ArrayList<>();
        fuelList.add("Petrol");
        fuelList.add("Diesel");
        fuelList.add("CNG");
        fuelList.add("Electric");
        fuelList.add("Electric & Hybrid");
        fuelList.add("CNG & Hybrid");

        List<String> transList=new ArrayList<>();
        transList.add("Manual");
        transList.add("Automatic");

        List<String> numList=new ArrayList<>();
        numList.add("One");
        numList.add("Two");
        numList.add("Three");
        fuelAdapter = new textonlyAdapter(PostActivity.this, fuelList);
        GridLayoutManager glm = new GridLayoutManager(PostActivity.this, 3);
        binding.fuelRec.setLayoutManager(glm);
        binding.fuelRec.setAdapter(fuelAdapter);
        fuelAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                fuel = catname;
            }
        });

        transAdapter = new textonlyAdapter(PostActivity.this, transList);
        GridLayoutManager glm1 = new GridLayoutManager(PostActivity.this, 1);
        binding.transRec.setLayoutManager(glm1);
        binding.transRec.setAdapter(transAdapter);
        transAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                transmission = catname;
            }
        });

        nowownAdapter = new textonlyAdapter(PostActivity.this, numList);
        GridLayoutManager glm2 = new GridLayoutManager(PostActivity.this, 3);
        binding.numofRec.setLayoutManager(glm2);
        binding.numofRec.setAdapter(nowownAdapter);
        nowownAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                numowner = catname;
            }
        });

        imagesAdapter = new imagesAdapter(PostActivity.this, imagesList);
        LinearLayoutManager llm1 = new LinearLayoutManager(PostActivity.this);
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        binding.automobImg.setLayoutManager(llm1);
        binding.automobImg.setAdapter(imagesAdapter);
        imagesAdapter.setonTileClick(new imagesAdapter.onTileClick() {
            @Override
            public void onRemoveClick(int position) {
                imagesList.remove(position);
                imagesAdapter.notifyDataSetChanged();
            }
        });

    }

    private void viewfunc() {

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
            }

        };
        binding.numberSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    show_number = true;
                } else {
                    show_number = false;
                }
            }
        });
        binding.datepicklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] =0;
            }
        });
        binding.registeruserimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cameraimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postAutomobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 2) {
                    binding.automobImg.setFocusable(true);
                    binding.automobImg.requestFocus();
                    Toast.makeText(PostActivity.this, "Please add atleast 2 images.", Toast.LENGTH_SHORT).show();
                } else if (binding.titleTxt.getText().toString().isEmpty()) {
                    binding.titleTxt.setFocusable(true);
                    binding.titleTxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please enter title.", Toast.LENGTH_SHORT).show();

                } else if (binding.prodnameTxt.getText().toString().isEmpty()) {
                    binding.prodnameTxt.setFocusable(true);
                    binding.prodnameTxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please enter product name.", Toast.LENGTH_SHORT).show();

                } else if (binding.proddescTxt.getText().toString().isEmpty()) {
                    binding.proddescTxt.setFocusable(true);
                    binding.proddescTxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please enter description.", Toast.LENGTH_SHORT).show();

                } else if (fuel == null) {
                    binding.fuelRec.setFocusable(true);
                    binding.fuelRec.requestFocus();
                    Toast.makeText(PostActivity.this, "Please select a fuel type.", Toast.LENGTH_SHORT).show();
                } else if (transmission == null) {
                    binding.transRec.setFocusable(true);
                    binding.transRec.requestFocus();
                    Toast.makeText(PostActivity.this, "lease select a transmission.", Toast.LENGTH_SHORT).show();
                }
                else if (numowner == null) {
                    binding.numofRec.setFocusable(true);
                    binding.numofRec.requestFocus();
                    Toast.makeText(PostActivity.this, "Please select a number of owners..", Toast.LENGTH_SHORT).show();
                }else if (binding.brandTxt.getText().toString().isEmpty()) {
                    binding.brandTxt.setFocusable(true);
                    binding.brandTxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please enter the brand name.", Toast.LENGTH_SHORT).show();
                } else if (binding.prodpriceTxt.getText().toString().isEmpty()) {
                    binding.prodpriceTxt.setFocusable(true);
                    binding.prodpriceTxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please enter the selling price.", Toast.LENGTH_SHORT).show();
                } else if (binding.datetxt.getText().toString().equals("Select Date")) {
                    binding.datetxt.setFocusable(true);
                    binding.datetxt.requestFocus();
                    Toast.makeText(PostActivity.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                }
                else if(binding.proddrivenTxt.getText().toString().isEmpty()) {
                    binding.proddrivenTxt.setFocusable(true);
                    binding.proddrivenTxt.requestFocus();
                   Toast.makeText(PostActivity.this, "Please enter km driven", Toast.LENGTH_SHORT).show();
                }
            else {
                    SharedPreferences sharedPreferences=getSharedPreferences("userlogged",0);
                    String userid=sharedPreferences.getString("userid","");
                    String city=sharedPreferences.getString("usercity","");
//                    Posting API Here
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiWork apiWork = retrofit.create(ApiWork.class);
                    StringBuilder images=new StringBuilder();
                    String base64img;
                    for (int i = 0; i < imagesAdapter.bannerlist.size(); i++) {
                        try {
                            InputStream is = getContentResolver().openInputStream(Uri.parse(imagesAdapter.bannerlist.get(i)));
                            Bitmap image1 = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image1.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                            images.append(base64img + ",");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Call<AdsModel.postadsResp> call=apiWork.post_automobile(userid,binding.prodnameTxt.getText().toString(),
                            binding.titleTxt.getText().toString(),"automobile",binding.proddescTxt.getText().toString(),
                            city,binding.prodpriceTxt.getText().toString(),images.toString().substring(0,images.toString().length()-1),binding.brandTxt.getText().toString(),
                            binding.modelTxt.getText().toString(),binding.datetxt.getText().toString(),fuel,
                            transmission,numowner,binding.proddrivenTxt.getText().toString(),"yes");

                    call.enqueue(new Callback<AdsModel.postadsResp>() {
                        @Override
                        public void onResponse(Call<AdsModel.postadsResp> call, Response<AdsModel.postadsResp> response) {
                            if(!response.isSuccessful()) {
                                Log.d("error code",String.valueOf(response.code()));
                                return;
                            }

                            if(response.body().getResult()!=null) {
                                Bundle bundle=new Bundle();
                                bundle.putString("ad_id",response.body().getResult().getProduct_id());
                                askBoost_Dialog dialog = new askBoost_Dialog();
                                dialog.setArguments(bundle);
                                dialog.show(getSupportFragmentManager(), "dialog");
                            }
                        }

                        @Override
                        public void onFailure(Call<AdsModel.postadsResp> call, Throwable t) {
                            Log.d("Failure",t.getMessage());
                        }
                    });


                }
            }
        });

        binding.backbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });
    }

    private void startCropActivity() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagesList.add(String.valueOf(result.getUri()));
                imagesAdapter.notifyDataSetChanged();
            }

        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}