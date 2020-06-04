package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.trips.TripStartBody;

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
        int id=getIntent().getIntExtra("id",-1);
        String name=getIntent().getStringExtra("name");
        String phone=getIntent().getStringExtra("phone");
        int peoples=getIntent().getIntExtra("peoples",-1);
        String timeName=getIntent().getStringExtra("timeName");
        int duration=getIntent().getIntExtra("duration",-1);
        TextView custName=findViewById(R.id.custName);
        TextView totalPeople=findViewById(R.id.totalPeople);
        TextView durationText=findViewById(R.id.duration);
        String placeHolder="Customer Name: "+name;
        custName.setText(placeHolder);
        placeHolder="Total People: "+peoples;
        totalPeople.setText(placeHolder);
        placeHolder="Total Duration: "+timeName;
        durationText.setText(placeHolder);
        Button startTrip=findViewById(R.id.startTrip);
        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripStart(id,duration);
            }
        });





    }

    private void tripStart(int id,int minute) {
        String a=id+"";
        ApiInterface apiInterface= ApiClient.getClient(TicketDetailActivity.this).create(ApiInterface.class);
        TripStartBody tripStartBody=new TripStartBody(a);
        Call<BasicResponse> call=apiInterface.startTrip(tripStartBody);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Toast.makeText(TicketDetailActivity.this, "Trip Started!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean(getString(R.string.trip_exist),true);
                editor.putInt(getString(R.string.trip_id),id);
                editor.putInt(getString(R.string.trip_minute),minute);
                editor.apply();
                Intent intent=new Intent(TicketDetailActivity.this,TripDetailsActivity.class);
                intent.putExtra("minute",minute);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(TicketDetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}