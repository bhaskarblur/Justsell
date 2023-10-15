package com.classified.upuse.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;
import com.classified.upuse.imageActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ChatAdapter extends RecyclerView.Adapter{
    private Context mcontext;
    private final static int TYPE_MSG_SENT=0;
    private final static int TYPE_MSG_RECEIVED=1;
    private final static int TYPE_IMG_SENT=2;
    private final static int TYPE_IMG_RECEIVED=3;
    private String receiver_img;
    public List<JSONObject> messages=new ArrayList<>();

    public ChatAdapter(Context mcontext,List<JSONObject> messages,String receiver_img) {
        this.mcontext=mcontext;
        this.messages = messages;
        this.receiver_img=receiver_img;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MSG_SENT:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_chat_lay,parent,false);
                return new sentMessageHolder(view);
            case TYPE_IMG_SENT:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_image_lay,parent,false);
                return new sentImageHolder(view);
            case TYPE_MSG_RECEIVED:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_chat_lay,parent,false);
                return new ReceivedMessageHolder(view);
            case TYPE_IMG_RECEIVED:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_image_lay,parent,false);
                return new ReceivedImageHolder(view);
            case 7:
                return null;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject message=messages.get(position);
        Log.d("image__", String.valueOf(message.has("image")));
        try {
            if(message.getString("isSent").equals("yes")) {
                if(message.has("message")) {
                    sentMessageHolder holder1= (sentMessageHolder) holder;
                    holder1.msg.setText(message.getString("message"));
                    holder1.time.setText(message.getString("time"));
                    holder1.msg.requestLayout();

                }
                else if(message.has("image")){
                    //convert to base64
                    sentImageHolder holder1= (sentImageHolder) holder;
                    holder1.time.setText(message.getString("time"));
                    final int radius = 20;
                    final int margin = 0;
                    Log.d("imageTest2", message.toString());
                    final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                    Picasso.get().load(message.getString("image").toString()).transform(transformation).into(holder1.image);
                    holder1.image.setRotation(0f);
                    holder1.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent= new Intent(mcontext, imageActivity.class);
                                intent.putExtra("image",message.getString("image"));
                                mcontext.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
            else {
                if(message.has("message")) {
                    ReceivedMessageHolder holder1= (ReceivedMessageHolder) holder;
                    holder1.msg.setText(message.getString("message"));
                    holder1.time.setText(message.getString("time"));
                    holder1.msg.requestLayout();
                }
                else if(message.has("image")){
                    ReceivedImageHolder holder1= (ReceivedImageHolder) holder;
                    holder1.time.setText(message.getString("time"));
                    final int radius = 20;
                    final int margin = 0;
                    Log.d("imageTest2", message.getString("image"));
                    final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                    Picasso.get().load(message.getString("image")).transform(transformation)
                           .into(holder1.image);
                    holder1.image.setRotation(0f);
                    holder1.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent= new Intent(mcontext, imageActivity.class);
                                intent.putExtra("image",message.getString("image"));
                                mcontext.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject message=messages.get(position);
        try {
            if(message.getString("isSent").equals("yes")) {
                if(message.has("message")) {
                    return TYPE_MSG_SENT;
                }
                else if(message.has("image")) {
                    return TYPE_IMG_SENT;
                }
            }
            else {
                if(message.has("message")) {
                    return TYPE_MSG_RECEIVED;
                }
                else if(message.has("image")){
                    return TYPE_IMG_RECEIVED;
                }
                else {
                    Log.d("here","icomehere");
                    return 7;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addItem(JSONObject jsonObject) {
        messages.add(jsonObject);
        notifyDataSetChanged();
    }
    private class sentMessageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView msg;
        TextView time;
        public sentMessageHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                pic = itemView.findViewById(R.id.sender_pic);
                msg = itemView.findViewById(R.id.sender_msg);
                time = itemView.findViewById(R.id.sender_time);
                SharedPreferences sharedPreferences = mcontext.getSharedPreferences("userlogged", 0);

                String userpic = sharedPreferences.getString("userimage", "");
                if (userpic != null && !userpic.isEmpty()) {
                    Picasso.get().load(userpic).transform(new CropCircleTransformation()).resize(480, 480).into(pic);
                }
            }
        }
    }

    private class sentImageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        ImageView image;
        TextView time;
        public sentImageHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                pic = itemView.findViewById(R.id.sender_pic);
                image = itemView.findViewById(R.id.sender_image);
                time = itemView.findViewById(R.id.sender_time);
                SharedPreferences sharedPreferences = mcontext.getSharedPreferences("userlogged", 0);

                String userpic = sharedPreferences.getString("userimage", "");

                if (userpic != null && !userpic.isEmpty()) {
                    Picasso.get().load(userpic).transform(new CropCircleTransformation()).resize(480, 480).into(pic);
                }
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView msg;
        TextView time;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                pic = itemView.findViewById(R.id.receiver_pic);
                msg = itemView.findViewById(R.id.receiver_msg);
                time = itemView.findViewById(R.id.receiver_time);
                if (receiver_img != null) {
                    Picasso.get().load(receiver_img).transform(new CropCircleTransformation()).resize(150, 150).into(pic);
                }
            }
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        ImageView image;
        TextView time;
        public ReceivedImageHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                pic = itemView.findViewById(R.id.receiver_pic);
                image = itemView.findViewById(R.id.receiver_img);
                time = itemView.findViewById(R.id.receiver_time);
                if (receiver_img != null) {
                    Picasso.get().load(receiver_img).transform(new CropCircleTransformation()).resize(150, 150).into(pic);
                }
            }
        }
    }
}
