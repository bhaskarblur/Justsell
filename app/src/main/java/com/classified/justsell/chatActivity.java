package com.classified.justsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.ChatAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AuthResponse;
import com.classified.justsell.Models.chatModel;
import com.classified.justsell.ViewModels.singleChatViewModel;
import com.classified.justsell.databinding.ActivityChatBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatActivity extends AppCompatActivity implements TextWatcher, PopupMenu.OnMenuItemClickListener {
    private ActivityChatBinding binding;
    private WebSocket webSocket;
    private String server_path = "http://echo.websocket.org";
    private String user_id;
    private String product_id;
    private String person_id;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private ChatAdapter chatAdapter;
    private singleChatViewModel viewModel;
    private String receiver_img;
    private api_baseurl baseurl = new api_baseurl();
    private List<JSONObject> previousMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ManageData();
        viewfuncs();
    }

    private void viewfuncs() {

//        binding.msgTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(!binding.msgTxt.getText().toString().isEmpty()|| !binding.msgTxt.getText().toString().equals("")) {
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("user_id", user_id);
//                        jsonObject.put("person_id", person_id);
//                        jsonObject.put("message", binding.msgTxt.getText().toString());
//                        webSocket.send(jsonObject.toString());
//                        jsonObject.put("isSent", "yes");
//                        chatAdapter.addItem(jsonObject);
//                        resetmessageEdit();
//                        binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return false;
//            }
//        });
        binding.scrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                binding.scrolldown.setVisibility(View.INVISIBLE);
            }
        });
        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    Calendar calendar=Calendar.getInstance();
                    String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                    jsonObject.put("time",time);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("person_id", person_id);
                    jsonObject.put("message", binding.msgTxt.getText().toString());
                    webSocket.send(jsonObject.toString());
                    jsonObject.put("isSent", "yes");
                    chatAdapter.addItem(jsonObject);
                    resetmessageEdit();
                    binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        binding.menuBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(chatActivity.this, v);
                popupMenu.setOnMenuItemClickListener(chatActivity.this);
                popupMenu.inflate(R.menu.chatoptionmenu);
                MenuItem item = popupMenu.getMenu().findItem(R.id.delete);
                SpannableString s = new SpannableString("Delete");
                item.setTitle(s);
                s.setSpan(new ForegroundColorSpan(Color.parseColor("#F24747")), 0, s.length(), 0);
                popupMenu.show();
            }
        });
    }

    private void ManageData() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        person_id = intent.getStringExtra("person_id");
        product_id = intent.getStringExtra("product_id");
        viewModel = new ViewModelProvider(chatActivity.this).get(singleChatViewModel.class);
        viewModel.initwork(user_id, product_id, person_id);
        binding.scrolldown.setVisibility(View.INVISIBLE);
        viewModel.getChatData().observe(chatActivity.this, new Observer<chatModel.chatResult>() {
            @Override
            public void onChanged(chatModel.chatResult chatResult) {
                if (chatResult != null) {
                    Picasso.get().load(chatResult.getProduct_img()).resize(200, 200)
                            .into(binding.productImage);

                    Picasso.get().load(chatResult.getPerson_img()).resize(150, 150).transform(new CropCircleTransformation())
                            .into(binding.personImage);

                    receiver_img = chatResult.getPerson_img();
                    binding.personName.setText(chatResult.getPerson_name());
                    if (chatResult.getStatus() != null) {
                        if (chatResult.getStatus().equals("online")) {
                            binding.personStatus.setVisibility(View.VISIBLE);
                        } else {
                            binding.personStatus.setText("Offline");
                        }
                    } else {
                        binding.personStatus.setText("Offline");
                    }
                    binding.productTitle.setText(chatResult.getProduct_title());
                    binding.productPrice.setText("Rs " + chatResult.getProduct_price());


                }
            }
        });
        viewModel.getPreviousChats().observe(chatActivity.this, new Observer<List<JSONObject>>() {
            @Override
            public void onChanged(List<JSONObject> jsonObjects) {
                if (jsonObjects.size() > 0) {

                    new Handler().postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void run() {
                            try {
                                previousMessages = jsonObjects;
                                chatAdapter = new ChatAdapter(chatActivity.this, getLayoutInflater(), previousMessages
                                        , receiver_img);

                                LinearLayoutManager llm = new LinearLayoutManager(chatActivity.this);
                                binding.chatsRec.setLayoutManager(llm);
                                binding.chatsRec.setAdapter(chatAdapter);
                                binding.chatsRec.scrollToPosition(chatAdapter.getItemCount() - 1);
                                llm.setSmoothScrollbarEnabled(true);

                                binding.chatsRec.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                    @Override
                                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                                        if(llm.findLastVisibleItemPosition()!=chatAdapter.getItemCount()-1) {

                                                binding.scrolldown.setVisibility(View.VISIBLE);
                                            } else {
                                                binding.scrolldown.setVisibility(View.INVISIBLE);
                                            }
                                    }
                                });

                                Log.d("chatshere", chatAdapter.messages.get(0).getString("isSent"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, 100);
                }

            }
        });

        initiateSocketConnection();

        binding.msgTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount()-1);
                return false;
            }
        });
        binding.msgTxt.addTextChangedListener(this);

    }

    private void resetmessageEdit() {
        binding.msgTxt.removeTextChangedListener(this);
        binding.msgTxt.setText("");
        binding.sendMsg.setVisibility(View.INVISIBLE);
        binding.pickImage.setVisibility(View.VISIBLE);

        binding.msgTxt.addTextChangedListener(this);

    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(server_path).build();
        webSocket = client.newWebSocket(request, new socketListener());
    }


    private void startCropActivity() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


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
                    pickImageFromGallery();
                } else {
                    Toast.makeText(chatActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().isEmpty()) {
            resetmessageEdit();
        } else {
            binding.sendMsg.setVisibility(View.VISIBLE);
            binding.pickImage.setVisibility(View.INVISIBLE);
        }

    }


    public class socketListener extends WebSocketListener {
        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("socketFailure", t.getMessage());
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            runOnUiThread(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", "true");
                    jsonObject.put("seen", jsonObject.getString("seen"));
                    chatAdapter.addItem(jsonObject);
                    binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

                    JSONObject sentcheck = new JSONObject();
                    sentcheck.put("user_id", user_id);
                    sentcheck.put("product_id", product_id);
                    sentcheck.put("person_id", person_id);
                    sentcheck.put("seen", "yes");

                    if (jsonObject.getString("status").equals("online")) {
                        binding.personStatus.setVisibility(View.VISIBLE);
                    } else {
                        binding.personStatus.setText("Offline");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            Log.d("connected", "yes");
            runOnUiThread(() -> {
                Toast.makeText(chatActivity.this, "Socket Connected!", Toast.LENGTH_SHORT).show();

                viewfuncs();
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri imguri = result.getUri();
                try {
                    InputStream is = getContentResolver().openInputStream(imguri);
                    Bitmap image = BitmapFactory.decodeStream(is);
                    sendImage(image, String.valueOf(imguri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private void sendImage(Bitmap image, String imageuri) {
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, by);
        String base64img = android.util.Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            Calendar calendar=Calendar.getInstance();
            String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
            jsonObject.put("time",time);
            jsonObject.put("user_id", user_id);
            jsonObject.put("person_id", person_id);
            jsonObject.put("image", base64img);
            webSocket.send(jsonObject.toString());

            JSONObject fakejson = new JSONObject();
            fakejson.put("time",time);
            fakejson.put("user_id", user_id);
            fakejson.put("person_id", person_id);
            fakejson.put("image", imageuri);
            fakejson.put("isSent", "yes");
            chatAdapter.addItem(fakejson);
            binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(chatActivity.this,R.style.AlertDialogStyle).setTitle("Delete Chat")
                        .setMessage("Are you sure you want to delete the whole chat?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);

                                Call<AuthResponse.SendOtp> call1 = apiWork.delete_singlechat(user_id, product_id, person_id);

                                call1.enqueue(new Callback<AuthResponse.SendOtp>() {
                                    @Override
                                    public void onResponse(Call<AuthResponse.SendOtp> call, retrofit2.Response<AuthResponse.SendOtp> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d("error code", String.valueOf(response.code()));
                                            return;
                                        }

                                        AuthResponse.SendOtp resp = response.body();

                                        if (resp.getCode().equals("200")) {
                                            Toast.makeText(chatActivity.this, "Account Blocked", Toast.LENGTH_SHORT).show();

                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                                        Log.d("Failure", t.getMessage());
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder1.show();
                break;
            case R.id.block:
                AlertDialog.Builder builder = new AlertDialog.Builder(chatActivity.this,R.style.AlertDialogStyle).setTitle("Delete & Block")
                        .setMessage("You are going to block this account, the chat will also be deleted.").setPositiveButton("Block", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl.toString())
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                ApiWork apiWork = retrofit.create(ApiWork.class);

                                Call<AuthResponse.SendOtp> call1 = apiWork.block(user_id, product_id, person_id);

                                call1.enqueue(new Callback<AuthResponse.SendOtp>() {
                                    @Override
                                    public void onResponse(Call<AuthResponse.SendOtp> call, retrofit2.Response<AuthResponse.SendOtp> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d("error code", String.valueOf(response.code()));
                                            return;
                                        }

                                        AuthResponse.SendOtp resp = response.body();

                                        if (resp.getCode().equals("200")) {
                                            Toast.makeText(chatActivity.this, "Account Blocked", Toast.LENGTH_SHORT).show();

                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AuthResponse.SendOtp> call, Throwable t) {
                                        Log.d("Failure", t.getMessage());
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
                break;

        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        getViewModelStore().clear();

    }
}