package com.gosea.captain.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gosea.captain.R;
import com.gosea.captain.models.queue.FixedQueueReponse;
import com.gosea.captain.models.queue.QueueResponseModel;

import java.util.ArrayList;

public class FixedQueueAdapter extends RecyclerView.Adapter<FixedQueueAdapter.QueueViewHolder> {
    private Context context;
    private ArrayList<FixedQueueReponse> queueModels;

    public FixedQueueAdapter(Context context, ArrayList<FixedQueueReponse> queueModels) {
        this.context = context;
        this.queueModels = queueModels;
    }


    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QueueViewHolder(LayoutInflater.from(context).inflate(R.layout.queue_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {
        holder.position.setText(String.valueOf(queueModels.get(position).getAsign_q()));
        holder.name.setText(queueModels.get(position).getUser().getFirst_name() + " " + queueModels.get(position).getUser().getLast_name());
        holder.boatName.setText(queueModels.get(position).getBoat().getArbicname());
    }

    @Override
    public int getItemCount() {
        return queueModels.size();
    }

    class QueueViewHolder extends RecyclerView.ViewHolder {
        TextView position, name, boatName, left_q;

        public QueueViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            boatName = itemView.findViewById(R.id.boatName);
        }
    }

}