package com.classified.justsell;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.imagesAdapter;
import com.classified.justsell.Adapters.textonlyAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.CustomDialogs.askBoost_Dialog;
import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.databinding.ActivityPostAllBinding;
import com.classified.justsell.databinding.ActivityPostPropertyBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity_all extends AppCompatActivity {
    private ActivityPostAllBinding binding;
    private textonlyAdapter condAdapter;
    private Boolean warrantystat = false;
    private Boolean show_number=false;
    private String condition;
    private imagesAdapter imagesAdapter;
    private List<String> imagesList = new ArrayList<>();
    String catname;
    private Boolean posting=false;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private String lat;
    private String longit;

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;

    api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ManageData();
        viewfunc();
        getlatlong();
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if(getContext()!=null) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location != null) {

                                    lat = String.valueOf(location.getLatitude());
                                    longit = String.valueOf(location.getLongitude());

                                } else {

                                    LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                                    LocationCallback locationCallback = new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            super.onLocationResult(locationResult);
                                            Location location1 = locationResult.getLastLocation();
                                            lat = String.valueOf(location1.getLatitude());
                                            longit = String.valueOf(location1.getLongitude());

                                        }
                                    };
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, Looper.getMainLooper());
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }


            }
        }

    }
    private void ManageData() {
        Intent intent=getIntent();
        catname=intent.getStringExtra("catname");
        binding.catname.setText(catname);
        List<String> condList = new ArrayList<>();
        condList.add("New");
        condList.add("Old");

        condition=condList.get(0).toString();
        condAdapter = new textonlyAdapter(PostActivity_all.this, condList);
        GridLayoutManager glm = new GridLayoutManager(PostActivity_all.this, 3);
        binding.condRec.setLayoutManager(glm);
        binding.condRec.setAdapter(condAdapter);
        condAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                condition = catname;
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
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(clickcheck[0]==0) {
                    binding.datetxt.setText(sdf.format(myCalendar.getTime()));
                }
            }

        };
        binding.datepicklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostActivity_all.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                clickcheck[0] =0;
            }
        });

        binding.warrantySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    warrantystat = true;
                } else {
                    warrantystat = false;
                }
            }
        });

        binding.numberSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    show_number = true;
                } else {
                    show_number = false;
                }
            }
        });

        imagesAdapter = new imagesAdapter(PostActivity_all.this, imagesList);
        LinearLayoutManager llm1 = new LinearLayoutManager(PostActivity_all.this);
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

        binding.registeruserimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_all.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cameraimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_all.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat==null || lat.isEmpty()) {
                    Toast.makeText(PostActivity_all.this, "Please allow location permission.", Toast.LENGTH_SHORT).show();
                    getlatlong();
                }
               else if (imagesList.size() < 2) {
                    binding.automobImg.setFocusable(true);
                    binding.automobImg.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please add atleast 2 images.", Toast.LENGTH_SHORT).show();
                } else if (binding.titleTxt.getText().toString().isEmpty()) {
                    binding.titleTxt.setFocusable(true);
                    binding.titleTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter title.", Toast.LENGTH_SHORT).show();

                } else if (binding.prodnameTxt.getText().toString().isEmpty()) {
                    binding.prodnameTxt.setFocusable(true);
                    binding.prodnameTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter product name.", Toast.LENGTH_SHORT).show();

                } else if (binding.proddescTxt.getText().toString().isEmpty()) {
                    binding.proddescTxt.setFocusable(true);
                    binding.proddescTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter description.", Toast.LENGTH_SHORT).show();

                } else if (condition == null) {
                    binding.condRec.setFocusable(true);
                    binding.condRec.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please select a condition.", Toast.LENGTH_SHORT).show();
                } else if (warrantystat == null) {
                    binding.warrantySwitch.setFocusable(true);
                    binding.warrantySwitch.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please tell about the warranty.", Toast.LENGTH_SHORT).show();
                } else if (binding.brandTxt.getText().toString().isEmpty()) {
                    binding.brandTxt.setFocusable(true);
                    binding.brandTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter the brand name.", Toast.LENGTH_SHORT).show();
                } else if (binding.spTxt.getText().toString().isEmpty()) {
                    binding.spTxt.setFocusable(true);
                    binding.spTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter the selling price.", Toast.LENGTH_SHORT).show();
                } else if (binding.cpTxt.getText().toString().isEmpty()) {
                    binding.cpTxt.setFocusable(true);
                    binding.cpTxt.requestFocus();
                    Toast.makeText(PostActivity_all.this, "Please enter the cost price.", Toast.LENGTH_SHORT).show();
                } else {
//                    Posting API Here
                    if (posting.equals(false)) {
                        posting = true;
                        binding.progressBar3.setVisibility(View.VISIBLE);
                        binding.postAllbtn.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
                        String userid = sharedPreferences.getString("userid", "");
                        String city = sharedPreferences.getString("usercity", "");
//                    Posting API Here
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        ApiWork apiWork = retrofit.create(ApiWork.class);
                        StringBuilder images = new StringBuilder();
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

                        String number_status;
                        String warranty;
                        if (show_number.equals(false)) {
                            number_status = "no";
                        } else {
                            number_status = "yes";
                        }
                        if (warrantystat.equals(false)) {
                            warranty = "No";
                        } else {
                            warranty = "Yes";
                        }

                        Call<AdsModel.postadsResp> call = apiWork.post_other(userid, binding.prodnameTxt.getText().toString(),
                                binding.titleTxt.getText().toString(), "other", binding.proddescTxt.getText().toString(),
                                binding.cpTxt.getText().toString(), city, binding.spTxt.getText().toString(), images.toString().substring(0, images.toString().length() - 1)
                                , condition, warranty, binding.brandTxt.getText().toString(), binding.datetxt.getText().toString(),
                                number_status, catname, lat,longit);

                        call.enqueue(new Callback<AdsModel.postadsResp>() {
                            @Override
                            public void onResponse(Call<AdsModel.postadsResp> call, Response<AdsModel.postadsResp> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("error code", String.valueOf(response.code()));
                                    posting=false;
                                    return;
                                }

                                if (response.body().getResult() != null) {
                                    binding.progressBar3.setVisibility(View.INVISIBLE);
                                    binding.postAllbtn.setVisibility(View.VISIBLE);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ad_id", response.body().getResult().getProduct_id());
                                    askBoost_Dialog dialog = new askBoost_Dialog();
                                    dialog.setArguments(bundle);
                                    dialog.setCancelable(false);
                                    dialog.show(getSupportFragmentManager(), "dialog");
                                }
                            }

                            @Override
                            public void onFailure(Call<AdsModel.postadsResp> call, Throwable t) {
                                Log.d("Failure", t.getMessage());
                                posting=false;
                                binding.progressBar3.setVisibility(View.INVISIBLE);
                                binding.postAllbtn.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }
            }
        });

        binding.backbtn4.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK) {
            List<Bitmap> bitmaps=new ArrayList<>();

            ClipData clipData=data.getClipData();
            if(clipData!=null) {
                if(clipData.getItemCount()+imagesList.size()<7) {
                    if(clipData.getItemCount()>1) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            String img = clipData.getItemAt(i).getUri().toString();
                            imagesList.add(img);
                        }
                    }
                    else {
                        String img=clipData.getItemAt(0).toString();
                        imagesList.add(img);
                    }
                    imagesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(PostActivity_all.this, "You can only pick "+String.valueOf(6-imagesList.size())+" more image.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                String uri=data.getData().toString();
                imagesList.add(uri);
                imagesAdapter.notifyDataSetChanged();
            }
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                imagesList.add(String.valueOf(result.getUri()));
//                imagesAdapter.notifyDataSetChanged();
//            }
//
//        }
//    }
//

    private void startCropActivity() {
        if(ActivityCompat.checkSelfPermission(PostActivity_all.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostActivity_all.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            return;
        }

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        startActivityForResult(intent,1);


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_all.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}