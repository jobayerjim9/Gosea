package com.gosea.captain.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.R;
import com.gosea.captain.models.QueueModel;
import com.gosea.captain.models.queue.QueueResponseModel;

import java.util.ArrayList;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {
    private Context context;
    private ArrayList<QueueResponseModel> queueModels;

    public QueueAdapter(Context context, ArrayList<QueueResponseModel> queueModels) {
        this.context = context;
        this.queueModels = queueModels;
    }

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QueueViewHolder(LayoutInflater.from(context).inflate(R.layout.queue_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {
        holder.position.setText(String.valueOf(queueModels.get(position).getQue_no()));
        holder.name.setText(queueModels.get(position).getCaptain().getUser().getUsername());
        holder.boatName.setText(queueModels.get(position).getCaptain().getBoat().getName());

    }

    @Override
    public int getItemCount() {
        return queueModels.size();
    }

    class QueueViewHolder extends RecyclerView.ViewHolder {
        TextView position, name, boatName;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            boatName = itemView.findViewById(R.id.boatName);
        }
    }

}
