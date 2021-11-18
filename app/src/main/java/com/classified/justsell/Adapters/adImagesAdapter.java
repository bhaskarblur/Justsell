package com.classified.justsell.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Fragments.chatFragment;
import com.classified.justsell.see_imageDialog;
import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class adImagesAdapter extends RecyclerView.Adapter<adImagesAdapter.viewHolder> {

    private Context mcontext;
    private List<AdsModel.imagesres> bannerlist;

    public adImagesAdapter(Context mcontext, List<AdsModel.imagesres> bannerlist) {
        this.mcontext = mcontext;
        this.bannerlist = bannerlist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adimages_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final int radius = 17;
        final int margin = 17;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(bannerlist.get(position).getImage()).transform(transformation).resize(800,450)
                .centerCrop().into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("image",bannerlist.get(position).getImage());
                see_imageDialog dialog=new see_imageDialog();
                FragmentManager manager = ((AppCompatActivity)mcontext).getSupportFragmentManager();
                dialog.setArguments(bundle);
                dialog.show(manager,"dialog");

            }
        });

    }

    @Override
    public int getItemCount() {
        if (bannerlist.size() > 6) {
            return 6;
        } else {
            return bannerlist.size();
        }

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.bannerimg);
        }
    }
}
