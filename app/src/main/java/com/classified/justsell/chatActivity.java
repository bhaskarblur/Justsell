package com.classified.justsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.classified.justsell.APIWork.ApiWork;
import com.classified.justsell.Adapters.ChatAdapter;
import com.classified.justsell.Constants.api_baseurl;
import com.classified.justsell.Models.AuthResponse;
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

public class chatActivity extends AppCompatActivity implements TextWatcher {
    private ActivityChatBinding binding;
    private WebSocket webSocket;
    private String server_path="http://echo.websocket.org";
    private String user_id;
    private String product_id;
    private String person_id;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private ChatAdapter chatAdapter;
    private String receiver_img;
    private  List<JSONObject>previousMessages=new ArrayList<>();
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().hide();

        ManageData();
        //viewfuncs();
    }

    private void viewfuncs() {

        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                try{
                    jsonObject.put("user_id",user_id);
                    jsonObject.put("person_id",person_id);
                    jsonObject.put("message",binding.msgTxt.getText().toString());
                    webSocket.send(jsonObject.toString());
                    jsonObject.put("isSent",true);
                    chatAdapter.addItem(jsonObject);
                    resetmessageEdit();
                    binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount()-1);

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
    }

    private void ManageData() {

        Intent intent=getIntent();
        user_id=intent.getStringExtra("user_id");
        person_id=intent.getStringExtra("person_id");
        product_id=intent.getStringExtra("product_id");
        chatAdapter=new ChatAdapter(chatActivity.this,getLayoutInflater(),previousMessages
        ,receiver_img);

        LinearLayoutManager llm=new LinearLayoutManager(chatActivity.this);

        binding.chatsRec.setLayoutManager(llm);
        binding.chatsRec.setAdapter(chatAdapter);

        initiateSocketConnection();

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
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(server_path).build();
        webSocket=client.newWebSocket(request,new socketListener());
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
        if(s.toString().trim().isEmpty()) {
            resetmessageEdit();
        }
        else {
            binding.sendMsg.setVisibility(View.VISIBLE);
            binding.pickImage.setVisibility(View.INVISIBLE);
        }

    }


    public class socketListener extends WebSocketListener {
        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("socketFailure",t.getMessage());
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
                    jsonObject.put("isSent",false);
                    chatAdapter.addItem(jsonObject);
                    binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount()-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            Log.d("connected","yes");
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
                    InputStream is=getContentResolver().openInputStream(imguri);
                    Bitmap image= BitmapFactory.decodeStream(is);
                    sendImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private void sendImage(Bitmap image) {
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, by);
        String base64img = android.util.Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("user_id",user_id);
            jsonObject.put("person_id",person_id);
            jsonObject.put("image",base64img);

            webSocket.send(jsonObject.toString());
            jsonObject.put("isSent",true);
            chatAdapter.addItem(jsonObject);
            binding.chatsRec.smoothScrollToPosition(chatAdapter.getItemCount()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

    }
}