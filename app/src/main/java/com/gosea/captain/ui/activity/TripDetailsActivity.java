package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.helper.BackgroundTimer;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.trips.TripTimeRemainingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsActivity extends AppCompatActivity {
    TextView minuteCountdown, secondsCountdown;
    Button finishTripButton, cancelTrip;
    int minute = 0;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        initUi();
    }

    private void initUi() {
        try {
            id = getIntent().getIntExtra("id", 0);
            minuteCountdown = findViewById(R.id.minuteCountdown);
            secondsCountdown = findViewById(R.id.secondsCountdown);
            finishTripButton = findViewById(R.id.finishTripButton);
            cancelTrip = findViewById(R.id.cancelTrip);
            finishTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TripDetailsActivity.this);
                    alertDialogBuilder.setTitle("Are You Sure?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishTrip(id);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialogBuilder.show();

                }
            });

            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
            boolean time = sharedPreferences.getBoolean(getString(R.string.trip_time), false);
            if (!time) {
                getRemainingTime();
            } else {
                startServiceTime();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void getRemainingTime() {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<TripTimeRemainingResponse> call = apiInterface.getRemainingTime(id + "");
        call.enqueue(new Callback<TripTimeRemainingResponse>() {
            @Override
            public void onResponse(Call<TripTimeRemainingResponse> call, Response<TripTimeRemainingResponse> response) {
                TripTimeRemainingResponse timeRemainingResponse = response.body();
                if (timeRemainingResponse != null) {
                    if (timeRemainingResponse.getStatus() == 400) {
                        Toast.makeText(TripDetailsActivity.this, R.string.already_finished, Toast.LENGTH_SHORT).show();
                        finishTrip(id);
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.trip_time), true);
                        editor.putInt(getString(R.string.trip_minute), timeRemainingResponse.getMinutes());
                        editor.putInt(getString(R.string.trip_second), timeRemainingResponse.getSeconds());
                        editor.apply();
                        startServiceTime();
                    }
                }
            }

            @Override
            public void onFailure(Call<TripTimeRemainingResponse> call, Throwable t) {

            }
        });


    }

    JobScheduler jobScheduler;

    private void startServiceTime() {

        ComponentName componentName = new ComponentName(TripDetailsActivity.this, BackgroundTimer.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setPersisted(true)
                .setOverrideDeadline(0)
                .build();
        jobScheduler = (JobScheduler) TripDetailsActivity.this.getSystemService(JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(jobInfo);

        startCountdown();
    }


    private void finishTrip(int id) {
        Log.d("finishTrip", "FinishTripFromTripDetail");
        ApiInterface apiInterface = ApiClient.getClient(TripDetailsActivity.this).create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.endTrip(String.valueOf(id));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.code() != 400) {
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    if (secondTimer1 != null) {
                        secondTimer1.cancel();
                    }
                    stopService(new Intent(TripDetailsActivity.this, BackgroundTimer.class));
                    Toast.makeText(TripDetailsActivity.this, R.string.trip_ended_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TripDetailsActivity.this, "Response " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }


    private CountDownTimer secondTimer1;
    private void startCountdown() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        minute = sharedPreferences.getInt(getString(R.string.trip_minute), 0);
        int sec = sharedPreferences.getInt(getString(R.string.trip_second), 0);
        minuteCountdown.setText(String.valueOf(minute));
        int total = (minute * 60000) + (sec * 1000);
        secondTimer1 = new CountDownTimer(total, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                minute = sharedPreferences.getInt(getString(R.string.trip_minute), 0);
                int sec = sharedPreferences.getInt(getString(R.string.trip_second), 0);
                minuteCountdown.setText(String.valueOf(minute));
                secondsCountdown.setText(String.valueOf(sec));
            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                minute = sharedPreferences.getInt(getString(R.string.trip_minute), 0);
                int sec = sharedPreferences.getInt(getString(R.string.trip_second), 0);
                minuteCountdown.setText(String.valueOf(minute));
                secondsCountdown.setText(String.valueOf(sec));
                finishTrip(id);
            }
        }.start();


//        secondTimer2 = new CountDownTimer(60000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                secondsCountdown.setText(String.valueOf(millisUntilFinished / 1000));
//            }
//
//            @Override
//            public void onFinish() {
//                if (!force) {
//                    if (minute != 0) {
//                        minute--;
//                        minuteCountdown.setText(String.valueOf(minute));
//                        secondTimer2.start();
//                    } else {
//                        minuteCountdown.setText("0");
//                        secondsCountdown.setText("0");
//                    }
//
//                }
//            }
//        };

    }

}