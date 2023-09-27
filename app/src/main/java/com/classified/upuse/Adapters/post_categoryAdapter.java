package com.classified.upuse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;
import com.classified.upuse.Models.homeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class post_categoryAdapter extends RecyclerView.Adapter<post_categoryAdapter.viewHolder> {

    private Context mcontext;
    private List<homeResponse .categoryResult> list;
    onItemClick listener;

    public post_categoryAdapter(Context mcontext, List<homeResponse.categoryResult> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.post_category_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.name.setText(list.get(position).getCategory_name());
        Picasso.get().load(list.get(position).getCategory_image()).resize(150           ,150)
                .centerCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        View card;
        ImageView img;
        TextView name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            card=itemView.findViewById(R.id.fieldbg);
            img=itemView.findViewById(R.id.fieldimg);
            name=itemView.findViewById(R.id.fieldname);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.ontileClick(list.get(getAdapterPosition()).getProduct_type(),
                                list.get(getAdapterPosition()).getCategory_name());
                    }
                }
            });
        }
    }
    public interface onItemClick {
        void ontileClick(String catname,String sendcatname);

    }
    public void setonItemclick(onItemClick listener) {
        this.listener=listener;
    }
}
