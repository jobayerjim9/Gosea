package com.gosea.captain.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.TripsAdapter;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.ticket.TicketData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedTripFragment extends Fragment {
    TripsAdapter tripsAdapter;
    private ArrayList<TicketData> ticketData = new ArrayList<>();
    private Context context;
    SwipeRefreshLayout swiperefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_accepted_trip, container, false);
        RecyclerView tripsRecycler = v.findViewById(R.id.tripsRecycler);
        swiperefresh = v.findViewById(R.id.swiperefresh);
        tripsRecycler.setLayoutManager(new LinearLayoutManager(context));
        tripsAdapter = new TripsAdapter(getContext(), ticketData, "accepted");
        tripsRecycler.setAdapter(tripsAdapter);
        getDetails();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetails();
            }
        });

        return v;
    }

    private void getDetails() {
        ApiInterface apiInterface= ApiClient.getClient(context).create(ApiInterface.class);
        Call<ArrayList<TicketData>> call=apiInterface.getTripHistory("Accepted");
        call.enqueue(new Callback<ArrayList<TicketData>>() {
            @Override
            public void onResponse(Call<ArrayList<TicketData>> call, Response<ArrayList<TicketData>> response) {
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
                ArrayList<TicketData> temp = response.body();
                ticketData.clear();
                if (temp != null) {
                    ticketData.addAll(temp);
                    tripsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TicketData>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}