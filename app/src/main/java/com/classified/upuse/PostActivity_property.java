package com.classified.upuse;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;
import com.classified.upuse.APIWork.ApiWork;
import com.classified.upuse.Adapters.imagesAdapter;
import com.classified.upuse.Adapters.textonlyAdapter;
import com.classified.upuse.Constants.api_baseurl;
import com.classified.upuse.CustomDialogs.askBoost_Dialog;
import com.classified.upuse.Models.AdsModel;
import com.classified.upuse.databinding.ActivityPostPropertyBinding;
import com.classified.upuse.helpingCode.progressDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity_property extends AppCompatActivity {
    private ActivityPostPropertyBinding binding;
    private textonlyAdapter adsAdapter;
    private textonlyAdapter propAdapter;
    private textonlyAdapter seloneAdapter;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private imagesAdapter imagesAdapter;
    private Boolean show_number=false;
    private String adtype;
    private String proptype;
    private String selone;
    private Boolean posting=false;
    private String lat;
    private String longit;

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;

    private List<String> imagesList = new ArrayList<>();
    private api_baseurl baseurl=new api_baseurl();
    String catname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostPropertyBinding.inflate(getLayoutInflater());
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
        List<String> adtypeList = new ArrayList<>();
        adtypeList.add("Rent");
        adtypeList.add("Sell");
        adtypeList.add("Lease");

        List<String> proptypeList=new ArrayList<>();
        proptypeList.add("Residential");
        proptypeList.add("Commerc.");
        proptypeList.add("Agriculture");

        List<String> seloneList=new ArrayList<>();
        seloneList.add("Plot/Land");
        seloneList.add("Apartment");
        seloneList.add("House");

        adtype=adtypeList.get(0).toString();
        proptype=proptypeList.get(0).toString();
        selone=seloneList.get(0).toString();
        adsAdapter = new textonlyAdapter(PostActivity_property.this, adtypeList);
        LinearLayoutManager glm = new LinearLayoutManager(this);
        glm.setOrientation(RecyclerView.HORIZONTAL);
        binding.adtypeRec.setLayoutManager(glm);
        binding.adtypeRec.setAdapter(adsAdapter);
        adsAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                adtype = catname;
            }
        });

        propAdapter = new textonlyAdapter(PostActivity_property.this, proptypeList);
        LinearLayoutManager glm1 = new LinearLayoutManager(this);
        glm1.setOrientation(RecyclerView.HORIZONTAL);
        binding.proptypeRec.setLayoutManager(glm1);
        binding.proptypeRec.setAdapter(propAdapter);
        propAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                proptype = catname;
            }
        });

        seloneAdapter = new textonlyAdapter(PostActivity_property.this, seloneList);
        LinearLayoutManager glm2 = new LinearLayoutManager(this);
        glm2.setOrientation(RecyclerView.HORIZONTAL);
        binding.numofRec.setLayoutManager(glm2);
        binding.numofRec.setAdapter(seloneAdapter);
        seloneAdapter.setoncardclicklistener(new textonlyAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(String catname) {
                selone = catname;
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
        imagesAdapter = new imagesAdapter(PostActivity_property.this, imagesList);
        LinearLayoutManager llm1 = new LinearLayoutManager(PostActivity_property.this);
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
                    Toast.makeText(PostActivity_property.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.cameraimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesList.size() < 6) {
                    startCropActivity();
                } else {
                    Toast.makeText(PostActivity_property.this, "Cannot add more than 6 images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.postAutomobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat==null || lat.isEmpty()) {
                    Toast.makeText(PostActivity_property.this, "Please allow location permission.", Toast.LENGTH_SHORT).show();
                    getlatlong();
                }
                else if (imagesList.size() < 2) {
                    binding.automobImg.setFocusable(true);
                    binding.automobImg.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please add atleast 2 images.", Toast.LENGTH_SHORT).show();
                } else if (binding.titleTxt.getText().toString().isEmpty()) {
                    binding.titleTxt.setFocusable(true);
                    binding.titleTxt.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please enter title.", Toast.LENGTH_SHORT).show();

                } else if (binding.prodnameTxt.getText().toString().isEmpty()) {
                    binding.prodnameTxt.setFocusable(true);
                    binding.prodnameTxt.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please enter product name.", Toast.LENGTH_SHORT).show();

                } else if (binding.proddescTxt.getText().toString().isEmpty()) {
                    binding.proddescTxt.setFocusable(true);
                    binding.proddescTxt.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please enter description.", Toast.LENGTH_SHORT).show();

                } else if (adtype == null) {
                    binding.adtypeRec.setFocusable(true);
                    binding.adtypeRec.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please select an ad type.", Toast.LENGTH_SHORT).show();
                } else if (proptype == null) {
                    binding.proptypeRec.setFocusable(true);
                    binding.proptypeRec.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please select a property type.", Toast.LENGTH_SHORT).show();
                } else if (selone == null) {
                    binding.numofRec.setFocusable(true);
                    binding.numofRec.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please select any one.", Toast.LENGTH_SHORT).show();
                } else if (binding.prodpriceTxt.getText().toString().isEmpty()) {
                    binding.prodpriceTxt.setFocusable(true);
                    binding.prodpriceTxt.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please enter the selling price.", Toast.LENGTH_SHORT).show();
                } else if (binding.areaTxt.getText().toString().equals("Select Date")) {
                    binding.areaTxt.setFocusable(true);
                    binding.areaTxt.requestFocus();
                    Toast.makeText(PostActivity_property.this, "Please enter the area.", Toast.LENGTH_SHORT).show();
                } else {
//                    progressDialog progressdialog = new progressDialog();
//                    progressdialog.showLoadingDialog(PostActivity_property.this, "Loading",
//                            "Creating Ad. Please wait");
//                    Posting API Here

                    if (posting.equals(false)) {
                        posting = true;
                        binding.progressBar4.setVisibility(View.VISIBLE);
                        binding.postAutomobBtn.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
                        String userid = sharedPreferences.getString("userid", "");
                        String city = sharedPreferences.getString("usercity", "");

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
                        if (show_number.equals(false)) {
                            number_status = "no";
                        } else {
                            number_status = "yes";
                        }
                        Call<AdsModel.postadsResp> call = apiWork.post_property(userid, binding.prodnameTxt.getText().toString(),
                                binding.titleTxt.getText().toString(), "property", binding.proddescTxt.getText().toString(),
                                city, binding.prodpriceTxt.getText().toString(), images.toString().substring(0, images.toString().length() - 1),
                                proptype, adtype, binding.areaTxt.getText().toString(), selone, number_status, catname,lat,longit);

                        call.enqueue(new Callback<AdsModel.postadsResp>() {
                            @Override
                            public void onResponse(Call<AdsModel.postadsResp> call, Response<AdsModel.postadsResp> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("error code", String.valueOf(response.code()));
//                                    progressdialog.hideLoadingDialog();
                                    return;
                                }

                                if (response.body().getResult() != null) {
                                    binding.progressBar4.setVisibility(View.INVISIBLE);
                                    binding.postAutomobBtn.setVisibility(View.VISIBLE);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ad_id", response.body().getResult().getProduct_id());
                                    askBoost_Dialog dialog = new askBoost_Dialog();
                                    dialog.setArguments(bundle);
                                    dialog.setCancelable(false);
                                    dialog.show(getSupportFragmentManager(), "dialog");
                                }
//                                progressdialog.hideLoadingDialog();
                            }

                            @Override
                            public void onFailure(Call<AdsModel.postadsResp> call, Throwable t) {
                                Log.d("Failure", t.getMessage());
                                posting=false;
//                                progressdialog.hideLoadingDialog();
                                binding.progressBar4.setVisibility(View.INVISIBLE);
                                binding.postAutomobBtn.setVisibility(View.VISIBLE);
                            }
                        });


                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            List<Bitmap> bitmaps = new ArrayList<>();

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                if (clipData.getItemCount() + imagesList.size() < 7) {
                    if (clipData.getItemCount() > 1) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            String img = clipData.getItemAt(i).getUri().toString();
                            imagesList.add(img);
                        }
                    } else {
                        String img = clipData.getItemAt(0).toString();
                        imagesList.add(img);
                    }
                    imagesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostActivity_property.this, "You can only pick " + String.valueOf(6 - imagesList.size()) + " more image.", Toast.LENGTH_SHORT).show();
                }
            } else {
                String uri = data.getData().toString();
                imagesList.add(uri);
                imagesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void startCropActivity() {
        if(ActivityCompat.checkSelfPermission(PostActivity_property.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostActivity_property.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
//            return;
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
//                    Toast.makeText(PostActivity_property.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
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