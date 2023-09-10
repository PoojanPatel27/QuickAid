package com.example.myapplication.quickaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NgoListAdapter extends RecyclerView.Adapter<NgoListAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrgDetailsModel> List;

    public NgoListAdapter(Context context, ArrayList<OrgDetailsModel> List) {
        this.context = context;
        this.List = List;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.ngolist_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrgDetailsModel orgDetails = List.get(position);
        holder.ngoName.setText(orgDetails.getName());
        holder.ngoCategory.setText(orgDetails.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView ngoName,ngoCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ngoName = itemView.findViewById(R.id.ngoNameTv);
            ngoCategory = itemView.findViewById(R.id.ngoCategoryTv);
        }
    }
}
