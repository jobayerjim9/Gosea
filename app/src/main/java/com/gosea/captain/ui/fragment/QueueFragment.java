package com.gosea.captain.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.FixedQueueAdapter;
import com.gosea.captain.controller.adapter.QueueAdapter;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.queue.FixedQueueReponse;
import com.gosea.captain.models.queue.QueueResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueFragment extends Fragment {
    QueueAdapter queueAdapter;
    ArrayList<QueueResponseModel> queueResponseModels=new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    public QueueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_queue, container, false);
        recyclerView = v.findViewById(R.id.queueRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recyclerView.setVisibility(View.GONE);
                if (tab.getPosition() == 0) {
                    getQueue();
                } else if (tab.getPosition() == 1) {
                    getFixedQueue();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //getQueue();
        return v;
    }

    private void getFixedQueue() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Fixed Queue");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<ArrayList<FixedQueueReponse>> call = apiInterface.getFixedQueue();
        call.enqueue(new Callback<ArrayList<FixedQueueReponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FixedQueueReponse>> call, Response<ArrayList<FixedQueueReponse>> response) {
                ArrayList<FixedQueueReponse> list = response.body();
                progressDialog.dismiss();
                if (list != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d("fixed", list.size() + "");
                    FixedQueueAdapter fixedQueueAdapter = new FixedQueueAdapter(context, list);
                    recyclerView.setAdapter(fixedQueueAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FixedQueueReponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    private void getQueue() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Queue...");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<ArrayList<QueueResponseModel>> call = apiInterface.getQueueList();
        call.enqueue(new Callback<ArrayList<QueueResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueResponseModel>> call, Response<ArrayList<QueueResponseModel>> response) {
                ArrayList<QueueResponseModel> temp = response.body();
                progressDialog.dismiss();
                if (temp != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    queueResponseModels.clear();
                    queueResponseModels.addAll(temp);
                    queueAdapter = new QueueAdapter(context, queueResponseModels);
                    recyclerView.setAdapter(queueAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QueueResponseModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getQueue();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}