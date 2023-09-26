package com.classified.upuse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.viewHolder> {

    private Context mcontext;
    public List<String> bannerlist;
    onTileClick listener;
    public imagesAdapter(Context mcontext, List<String> bannerlist) {
        this.mcontext = mcontext;
        this.bannerlist = bannerlist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.images_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final int radius = 20;
        final int margin = 5;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(bannerlist.get(position)).transform(transformation).resize(300,300)
                .centerCrop().into(holder.img);

    }

    @Override
    public int getItemCount() {
        return bannerlist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        View removebtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.addedimg);
            removebtn=itemView.findViewById(R.id.remimg);

            removebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    public interface onTileClick {
        void onRemoveClick(int position);
    }

    public void setonTileClick(onTileClick listener) {
        this.listener=listener;
    }
}
