package com.classified.upuse.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.Models.AdsModel;
import com.classified.upuse.R
import com.classified.upuse.imageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
        Picasso.get().load(bannerlist.get(position).getImage()).transform(
                new CropCircleTransformation()
                ).resize(700,350)
                .centerCrop().into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(mcontext, imageActivity.class);
               intent.putExtra("image",bannerlist.get(position).getImage());
               mcontext.startActivity(intent);
               // ((Activity) mcontext).overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);

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
