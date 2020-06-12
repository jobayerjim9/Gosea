package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.trips.TripStartBody;
import com.gosea.captain.models.trips.TripStartResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        initUi();
    }

    private void initUi() {
        int id = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        int peoples = getIntent().getIntExtra("peoples", -1);
        String timeName = getIntent().getStringExtra("timeName");
        int duration = getIntent().getIntExtra("duration", -1);
        TextView custName = findViewById(R.id.custName);
        TextView totalPeople = findViewById(R.id.totalPeople);
        TextView durationText = findViewById(R.id.duration);
        String placeHolder = getString(R.string.cust_name) + name;
        custName.setText(placeHolder);
        placeHolder = getString(R.string.total_people) + peoples;
        totalPeople.setText(placeHolder);
        placeHolder = getString(R.string.total_duration) + timeName;
        durationText.setText(placeHolder);
        Button startTrip = findViewById(R.id.startTrip);

        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                boolean trip = sharedPreference.getBoolean(getString(R.string.trip_exist), false);
                if (trip) {
                    Toast.makeText(TicketDetailActivity.this, R.string.already_in_trip, Toast.LENGTH_SHORT).show();
                } else {
                    tripStart(id);
                }
            }
        });





    }

    private void tripStart(int id) {
        String a = id + "";
        ApiInterface apiInterface = ApiClient.getClient(TicketDetailActivity.this).create(ApiInterface.class);
        TripStartBody tripStartBody = new TripStartBody(a);
        Call<TripStartResponse> call = apiInterface.startTrip(tripStartBody);
        call.enqueue(new Callback<TripStartResponse>() {
            @Override
            public void onResponse(Call<TripStartResponse> call, Response<TripStartResponse> response) {
                if (response.code() == 200) {
                    Log.d("tripStartResponse", response.code() + "");
                    TripStartResponse tripStartResponse = response.body();
                    if (tripStartResponse != null) {
                        Toast.makeText(TicketDetailActivity.this, R.string.trip_started, Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.trip_exist), true);
                        editor.putInt(getString(R.string.trip_id), tripStartResponse.getId());
                        editor.apply();
                        Intent intent = new Intent(TicketDetailActivity.this, TripDetailsActivity.class);
                        intent.putExtra("id", tripStartResponse.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TicketDetailActivity.this, R.string.trip_not_start, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(TicketDetailActivity.this, R.string.trip_not_start, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<TripStartResponse> call, Throwable t) {
                Toast.makeText(TicketDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}