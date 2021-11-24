package com.classified.justsell.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.R;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ChatAdapter extends RecyclerView.Adapter{
    private Context mcontext;
    private final static int TYPE_MSG_SENT=0;
    private final static int TYPE_MSG_RECEIVED=1;
    private final static int TYPE_IMG_SENT=2;
    private final static int TYPE_IMG_RECEIVED=3;
    private String receiver_img;
    private LayoutInflater inflater;
    private List<JSONObject> messages;

    public ChatAdapter(Context mcontext,LayoutInflater inflater, List<JSONObject> messages,String receiver_img) {
        this.mcontext=mcontext;
        this.inflater = inflater;
        this.messages = messages;
        this.receiver_img=receiver_img;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MSG_SENT:
                view=inflater.inflate(R.layout.sender_chat_lay,parent,false);
                return new sentMessageHolder(view);
            case TYPE_IMG_SENT:
                view=inflater.inflate(R.layout.sender_image_lay,parent,false);
                return new sentImageHolder(view);
            case TYPE_MSG_RECEIVED:
                view=inflater.inflate(R.layout.receiver_chat_lay,parent,false);
                return new ReceivedMessageHolder(view);
            case TYPE_IMG_RECEIVED:
                view=inflater.inflate(R.layout.receiver_image_lay,parent,false);
                return new ReceivedImageHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject message=messages.get(position);

        try {
            if(message.getBoolean("isSent")) {
                if(message.has("message")) {
                    sentMessageHolder holder1= (sentMessageHolder) holder;
                    holder1.msg.setText(message.getString("message"));
                }
                else {
                    sentImageHolder holder1= (sentImageHolder) holder;
                    Picasso.get().load(message.getString("image")).resize(250,250).into(holder1.image);

                }
            }
            else {
                if(message.has("message")) {
                    ReceivedMessageHolder holder1= (ReceivedMessageHolder) holder;
                    holder1.msg.setText(message.getString("message"));
                }
                else {
                    ReceivedImageHolder holder1= (ReceivedImageHolder) holder;
                    Picasso.get().load(message.getString("image")).resize(250,250).into(holder1.image);

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
                else {
                    return TYPE_IMG_SENT;
                }
            }
            else {
                if(message.has("message")) {
                    return TYPE_MSG_RECEIVED;
                }
                else {
                    return TYPE_IMG_RECEIVED;
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
        public sentMessageHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.sender_pic);
            msg=itemView.findViewById(R.id.sender_msg);

            SharedPreferences sharedPreferences=mcontext.getSharedPreferences("userlogged",0);

            String userpic=sharedPreferences.getString("userimage","");

            Picasso.get().load(userpic).transform(new CropCircleTransformation()).resize(150,150).into(pic);

        }
    }

    private class sentImageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        ImageView image;
        public sentImageHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.sender_pic);
            image=itemView.findViewById(R.id.sender_image);

            SharedPreferences sharedPreferences=mcontext.getSharedPreferences("userlogged",0);

            String userpic=sharedPreferences.getString("userimage","");

            Picasso.get().load(userpic).transform(new CropCircleTransformation()).resize(150,150).into(pic);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView msg;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.receiver_pic);
            msg=itemView.findViewById(R.id.receiver_msg);

        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        ImageView image;
        public ReceivedImageHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.receiver_pic);
            image=itemView.findViewById(R.id.receiver_img);
        }
    }
}
