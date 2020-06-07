package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsActivity extends AppCompatActivity {
    TextView minuteRemain;
    Button finishTripButton;
    int minute=0;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        initUi();
    }

    private void initUi() {
        minute=getIntent().getIntExtra("minute",0);
        id=getIntent().getIntExtra("id",0);
        minuteRemain=findViewById(R.id.minuteRemain);
        finishTripButton=findViewById(R.id.finishTripButton);
        finishTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTrip(id);
            }
        });
        startCountdown();
    }

    private void finishTrip(int id) {
        ApiInterface apiInterface= ApiClient.getClient(TripDetailsActivity.this).create(ApiInterface.class);
        Call<BasicResponse> call=apiInterface.endTrip(String.valueOf(id));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                SharedPreferences sharedPreferences=getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(TripDetailsActivity.this, R.string.trip_ended_success, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }
    private void startCountdown() {
        new CountDownTimer(minute*60*1000,60000) {

            @Override
            public void onTick(long millisUntilFinished) {
                minute--;
                SharedPreferences sharedPreferences=getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(getString(R.string.trip_minute),minute);
                editor.apply();
                minuteRemain.setText(String.valueOf(minute));
            }

            @Override
            public void onFinish() {
                minuteRemain.setText("0");
                Toast.makeText(TripDetailsActivity.this, "Time Ended!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }


}