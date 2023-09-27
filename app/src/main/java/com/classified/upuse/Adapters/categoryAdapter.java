package com.classified.upuse.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.classified.upuse.R;
import com.classified.upuse.Models.homeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.viewHolder> {
    private Context mcontext;
    private List<homeResponse.categoryResult> searchfieldModelList;
    private oncardclicklistener listener;
    private Integer checkpos=0;
    private Boolean selected=false;
    public categoryAdapter(Context mcontext, List<homeResponse.categoryResult> searchfieldModelList) {
        this.mcontext = mcontext;
        this.searchfieldModelList = searchfieldModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.category_layout,parent,false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        holder.fieldname.setText(searchfieldModelList.get(position).getCategory_name());
        Picasso.get().load(searchfieldModelList.get(position).getCategory_image()).resize(150           ,150)
                .centerCrop().into(holder.img);
        if(checkpos==position){
            holder.fieldbg.setBackgroundResource(R.drawable.fieldselbg);

        }
        else if(checkpos!=position) {
            holder.fieldbg.setBackgroundResource(R.drawable.fieldnotselbg);
        }
        if(searchfieldModelList.get(position).getCategory_name().contains("All")) {
            Picasso.get().load(R.drawable.applogo).into(holder.img);
            holder.img.setColorFilter(ContextCompat.getColor(mcontext, R.color.primary_color));
        }

    }

    @Override
    public int getItemCount() {
        return searchfieldModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView fieldname;
        View fieldbg;
        ImageView img;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldname=itemView.findViewById(R.id.fieldname);
            img=itemView.findViewById(R.id.fieldimg);
            fieldbg=itemView.findViewById(R.id.fieldbg);
            fieldbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        checkpos=position;
                        notifyDataSetChanged();
                        listener.oncardclick(searchfieldModelList.get(position).getCategory_name());
                    }

                }
            });
        }
    }

    public interface oncardclicklistener{
            void oncardclick(String catname);
    }
    public void setoncardclicklistener(oncardclicklistener listener){
        this.listener=listener;
    }
}
