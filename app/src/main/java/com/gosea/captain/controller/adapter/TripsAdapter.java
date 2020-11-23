package com.gosea.captain.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gosea.captain.R;
import com.gosea.captain.controller.helper.BackgroundTimer;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.ticket.TicketData;
import com.gosea.captain.models.trips.TripStartBody;
import com.gosea.captain.models.trips.TripStartResponse;
import com.gosea.captain.ui.activity.TicketDetailActivity;
import com.gosea.captain.ui.activity.TripDetailsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private Context context;
    private ArrayList<TicketData> ticketData;
    private String from;

    public TripsAdapter(Context context, ArrayList<TicketData> ticketData, String from) {
        this.context = context;
        this.ticketData = ticketData;
        this.from = from;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TripsViewHolder(LayoutInflater.from(context).inflate(R.layout.trip_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {

        String placeHolder = ticketData.get(position).getOrder().getName();
        holder.custName.setText(placeHolder);
        placeHolder = ticketData.get(position).getOrder().getPeoples() + " " + context.getString(R.string.people);
        holder.totalPeople.setText(placeHolder);
        placeHolder = ticketData.get(position).getOrder().getTimeslot().getName();
        holder.duration.setText(placeHolder);
        placeHolder = ticketData.get(position).getCreated();
        holder.date.setText(placeHolder.substring(0, 10));
        if (from.equals("accepted")) {
            holder.tripButtons.setVisibility(View.VISIBLE);
            holder.startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tripStart(ticketData.get(position).getId());
                }
            });
            holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelTrip(ticketData.get(position).getId());
                }
            });
        } else {
            holder.tripButtons.setVisibility(View.GONE);
        }
    }

    private void cancelTrip(int id) {
        Log.d("finishTrip", "FinishTripFromTripDetail");
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.cancelTrip(String.valueOf(id));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.code() != 400) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.trip_file), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(context, R.string.trip_ended_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Response " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }

    private void tripStart(int id) {
        String a = id + "";
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        TripStartBody tripStartBody = new TripStartBody(a);
        Call<TripStartResponse> call = apiInterface.startTrip(tripStartBody);
        call.enqueue(new Callback<TripStartResponse>() {
            @Override
            public void onResponse(Call<TripStartResponse> call, Response<TripStartResponse> response) {
                if (response.code() == 200) {
                    Log.d("tripStartResponse", response.code() + "");
                    TripStartResponse tripStartResponse = response.body();
                    if (tripStartResponse != null) {
                        Toast.makeText(context, R.string.trip_started, Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.trip_file), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(context.getString(R.string.trip_exist), true);
                        editor.putInt(context.getString(R.string.trip_id), tripStartResponse.getId());
                        editor.apply();
                        Intent intent = new Intent(context, TripDetailsActivity.class);
                        intent.putExtra("id", tripStartResponse.getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, R.string.trip_not_start, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, R.string.trip_not_start, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<TripStartResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketData.size();
    }

    class TripsViewHolder extends RecyclerView.ViewHolder {
        TextView custName, totalPeople, duration, date;
        LinearLayout tripButtons;
        Button startButton, cancelButton;

        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            custName = itemView.findViewById(R.id.custName);
            totalPeople = itemView.findViewById(R.id.totalPeople);
            duration = itemView.findViewById(R.id.duration);
            tripButtons = itemView.findViewById(R.id.tripButtons);
            date = itemView.findViewById(R.id.date);
            startButton = itemView.findViewById(R.id.startButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
