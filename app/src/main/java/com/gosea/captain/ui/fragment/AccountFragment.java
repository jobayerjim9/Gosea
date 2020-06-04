package com.gosea.captain.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.CheckOutStatus;
import com.gosea.captain.models.CheckedStatusResponse;
import com.gosea.captain.models.profile.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private TextView username,email;
    Button checkInButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_account, container, false);
        username=v.findViewById(R.id.username);
        email=v.findViewById(R.id.email);
        checkInButton=v.findViewById(R.id.checkInButton);
        
        getProfile();


        return v;
    }
    private void getProfile() {
        ApiInterface apiInterface= ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<ProfileResponse> call=apiInterface.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse=response.body();
                if (profileResponse!=null) {
                    Log.d("name",profileResponse.getUsername());
                    username.setText(profileResponse.getUsername());
                    email.setText(profileResponse.getEmail());
                    if (profileResponse.getProfile().isStatus()) {
                        setCheckOut();
                        
                    } else {
                        setCheckIn();
                        
                    }
                    
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });
    }

    private void setCheckOut() {
        checkInButton.setText("Check Out");
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              checkOut();
            }
        });
    }

    private void checkOut() {
        ApiInterface apiInterface=ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<CheckOutStatus> call=apiInterface.checkOut();
        call.enqueue(new Callback<CheckOutStatus>() {
            @Override
            public void onResponse(Call<CheckOutStatus> call, Response<CheckOutStatus> response) {
                CheckOutStatus checkOutStatus=response.body();
                if (checkOutStatus!=null) {
                    if (!checkOutStatus.isStatus()) {
                        setCheckIn();
                        Toast.makeText(getContext(), "Checked Out Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckOutStatus> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    private void setCheckIn() {
        checkInButton.setText("Check In");
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIn();
            }
        });
    }

    private void checkIn() {
        ApiInterface apiInterface=ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<CheckedStatusResponse> call=apiInterface.checkIn();
        call.enqueue(new Callback<CheckedStatusResponse>() {
            @Override
            public void onResponse(Call<CheckedStatusResponse> call, Response<CheckedStatusResponse> response) {
                CheckedStatusResponse checkedStatusResponse=response.body();
                if (checkedStatusResponse!=null) {
                    if (checkedStatusResponse.isStatus()) {
                        setCheckOut();
                        Toast.makeText(getContext(), "Checked In Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckedStatusResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}