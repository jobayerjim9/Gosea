package com.gosea.captain.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.QueueAdapter;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.queue.QueueResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueFragment extends Fragment {
    QueueAdapter queueAdapter;
    ArrayList<QueueResponseModel> queueResponseModels=new ArrayList<>();
    Context context;
    public QueueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_queue, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.queueRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        queueAdapter=new QueueAdapter(context,queueResponseModels);
        recyclerView.setAdapter(queueAdapter);
        getQueue();
        return v;
    }

    private void getQueue() {
        ApiInterface apiInterface= ApiClient.getClient(context).create(ApiInterface.class);
        Call<ArrayList<QueueResponseModel>> call=apiInterface.getQueueList();
        call.enqueue(new Callback<ArrayList<QueueResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueResponseModel>> call, Response<ArrayList<QueueResponseModel>> response) {
                ArrayList<QueueResponseModel> temp=response.body();
                if (temp!=null) {
                    queueResponseModels.addAll(temp);
                    queueAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QueueResponseModel>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}