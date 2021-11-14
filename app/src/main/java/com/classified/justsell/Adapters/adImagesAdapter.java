package com.classified.justsell.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Models.AdsModel;
import com.classified.justsell.Models.homeResponse;
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
        View view = LayoutInflater.from(mcontext).inflate(R.layout.banner_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final int radius = 30;
        final int margin = 25;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(bannerlist.get(position).getImage()).transform(transformation).resize(800,450)
                .centerCrop().into(holder.img);

    }

    @Override
    public int getItemCount() {
        if (bannerlist.size() > 4) {
            return 4;
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
