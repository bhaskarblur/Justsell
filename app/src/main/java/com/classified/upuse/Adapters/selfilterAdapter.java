package com.classified.upuse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;

import java.util.List;

public class selfilterAdapter extends RecyclerView.Adapter<selfilterAdapter.viewHolder> {
    private Context mcontext;
    public List<String> filtername;
    public List<String> filterrate;
    private oncardclicklistener listener;
    private Integer checkpos=0;
    private Boolean selected=false;
    public selfilterAdapter(Context mcontext, List<String> filtername,List<String> filterrate) {
        this.mcontext = mcontext;
        this.filtername = filtername;
        this.filterrate=filterrate;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.selfilter_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

       // if(searchfieldModelList.get(position).getCategory_name().equals("All")) {
           // holder.img.setVisibility(View.GONE);
        //}
        holder.fieldname.setText(filtername.get(position));
        holder.filterrate.setText(filterrate.get(position));

    }

    @Override
    public int getItemCount() {
        return filtername.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView fieldname;
        View fieldbg;
        TextView filterrate;
        View remove;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldname=itemView.findViewById(R.id.filter_name);
            filterrate=itemView.findViewById(R.id.filter_rate);
            fieldbg=itemView.findViewById(R.id.filterbg);
            remove=itemView.findViewById(R.id.remove_filter);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        checkpos=position;
                        notifyDataSetChanged();
                        listener.onremoveclick(position);
                    }

                }
            });
        }
    }

    public interface oncardclicklistener{
            void onremoveclick(int position);
    }
    public void setoncardclicklistener(oncardclicklistener listener){
        this.listener=listener;
    }
}
