package com.gosea.captain.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gosea.captain.R;
import com.gosea.captain.models.ticket.TicketData;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private Context context;
    private ArrayList<TicketData> ticketData;

    public TripsAdapter(Context context, ArrayList<TicketData> ticketData) {
        this.context = context;
        this.ticketData = ticketData;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TripsViewHolder(LayoutInflater.from(context).inflate(R.layout.trip_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        String placeHolder = ticketData.get(position).getOrder().getName();
        holder.custName.setText(placeHolder);
        placeHolder = ticketData.get(position).getOrder().getPeoples() + "";
        holder.totalPeople.setText(placeHolder);
        placeHolder = ticketData.get(position).getOrder().getTimeslot().getName();
        holder.duration.setText(placeHolder);
        placeHolder = ticketData.get(position).getCreated();
        holder.date.setText(placeHolder.substring(0, 10));

    }

    @Override
    public int getItemCount() {
        return ticketData.size();
    }

    class TripsViewHolder extends RecyclerView.ViewHolder
    {
        TextView custName,totalPeople,duration,date;

        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            custName=itemView.findViewById(R.id.custName);
            totalPeople=itemView.findViewById(R.id.totalPeople);
            duration=itemView.findViewById(R.id.duration);

            date=itemView.findViewById(R.id.date);

        }
    }
}
