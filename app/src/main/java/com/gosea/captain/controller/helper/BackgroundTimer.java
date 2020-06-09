package com.gosea.captain.controller.helper;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.ui.activity.TripDetailsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundTimer extends JobService {
    int minute, sec;
    private CountDownTimer secondTimer1, secondTimer2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean trip;

    @Override
    public boolean onStartJob(JobParameters params) {
        sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        minute = sharedPreferences.getInt(getString(R.string.trip_minute), 0);
        sec = sharedPreferences.getInt(getString(R.string.trip_second), 0);
        trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
        startCountdown();
        return trip;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("Finished", "Background Task Finished");
        return true;

    }

    private void startCountdown() {
        sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.trip_exist), false)) {
            editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.trip_time), true);
            editor.apply();

            secondTimer2 = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
                    if (trip) {
                        sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putInt(getString(R.string.trip_second), (int) (millisUntilFinished / 1000));
                        editor.apply();
                        Log.d("countdown", minute + " " + (int) (millisUntilFinished / 1000));
                    } else {
                        secondTimer2.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
                    if (trip) {
                        minute--;
                        if (minute != -1) {
                            sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putInt(getString(R.string.trip_minute), minute);
                            editor.apply();
                            secondTimer2.start();
                        } else {
                            sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putBoolean(getString(R.string.trip_time), false);
                            editor.apply();
                            int id = sharedPreferences.getInt(getString(R.string.trip_id), 0);
                            finishTrip(id);

                        }
                    }


                }
            };
            secondTimer1 = new CountDownTimer(sec * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
                    if (trip) {
                        sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putInt(getString(R.string.trip_second), (int) (millisUntilFinished / 1000));
                        editor.apply();
                    } else {
                        secondTimer1.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
                    if (trip) {
                        minute--;
                        sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putInt(getString(R.string.trip_minute), minute);
                        editor.apply();
                        secondTimer2.start();
                    }

                }
            }.start();

        } else {
            secondTimer1.cancel();
            secondTimer2.cancel();
        }


    }

    private void finishTrip(int id) {
        Log.d("finishTrip", "FinishTripFromBackground");
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.endTrip(String.valueOf(id));
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.code() != 400) {
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

    }
}
