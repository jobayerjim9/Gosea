package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.profile.PasswordChangeBody;
import com.gosea.captain.models.profile.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextInputLayout passwordInput, confirmPassword, currentPass;
    TextView name, boatType, parkingPoint, zone, email, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUi();
    }

    private void initUi() {
        Button updateButton = findViewById(R.id.updateButton);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        passwordInput = findViewById(R.id.passwordInput);
        currentPass = findViewById(R.id.currentPass);
        confirmPassword = findViewById(R.id.confirmPassword);
        name = findViewById(R.id.name);
        boatType = findViewById(R.id.boatType);
        parkingPoint = findViewById(R.id.parkingPoint);
        zone = findViewById(R.id.zone);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);

        getProfile();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePassword();
            }
        });

    }

    private void validatePassword() {
        String password = passwordInput.getEditText().getText().toString().trim();
        String confirmP = confirmPassword.getEditText().getText().toString().trim();
        String current = currentPass.getEditText().getText().toString().trim();

        if (current.isEmpty()) {
            currentPass.setErrorEnabled(true);
            currentPass.setError(getString(R.string.enter_current));
        } else if (password.isEmpty()) {
            passwordInput.setErrorEnabled(true);
            passwordInput.setError(getString(R.string.enter_new));
        } else if (confirmP.isEmpty()) {
            confirmPassword.setErrorEnabled(true);
            confirmPassword.setError(getString(R.string.enter_again));
        } else if (!password.equals(confirmP)) {
            passwordInput.setErrorEnabled(false);
            confirmPassword.setErrorEnabled(true);
            confirmPassword.setError(getString(R.string.not_matched));
        } else {
            passwordInput.setErrorEnabled(false);
            confirmPassword.setErrorEnabled(false);
            updatePassword(password, current);
        }

    }

    private void updatePassword(String password, String current) {
        PasswordChangeBody passwordChangeBody = new PasswordChangeBody(current, password);
        ApiInterface apiInterface = ApiClient.getClient(ProfileActivity.this).create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.changePassword(passwordChangeBody);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                BasicResponse basicResponse = response.body();
                if (basicResponse != null) {
                    Log.d("passwordStatus", basicResponse.getStatus() + "");
                    if (basicResponse.getStatus() == 200) {
                        Toast.makeText(ProfileActivity.this, R.string.pass_change, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });


    }


    private void getProfile() {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ProfileResponse> call = apiInterface.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                if (profileResponse != null) {
                    try {
                        email.setText(profileResponse.getEmail());
                        username.setText(profileResponse.getUsername());
                        Log.d("name", profileResponse.getUsername());
                        String placeHolder = getString(R.string.name) + " " + profileResponse.getProfile().getBoat().getName();
                        name.setText(placeHolder);
                        placeHolder = getString(R.string.boat_type) + " " + profileResponse.getProfile().getBoat().getBoat_typ().getB_type();
                        boatType.setText(placeHolder);
                        placeHolder = getString(R.string.parking_point) + " " + profileResponse.getProfile().getBoat().getParking_spot().getPoint();
                        parkingPoint.setText(placeHolder);
                        placeHolder = getString(R.string.parking_zone) + " " + profileResponse.getProfile().getBoat().getParking_spot().getZone();
                        zone.setText(placeHolder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });
    }
}