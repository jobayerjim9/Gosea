package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.LoginBody;
import com.gosea.captain.models.LoginResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.lang_file), Context.MODE_PRIVATE);
        String lang = preferences.getString(getString(R.string.language), null);
        if (lang != null) {
            setApplicationLanguage(lang);
        }
        signIn();


    }

    private void signIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(getString(R.string.username_file), null);
        final String password = sharedPreferences.getString(getString(R.string.password_file), null);

        if (username != null && password != null) {
            LoginBody loginBody = new LoginBody(username, password);
            ApiInterface apiInterface = ApiClient.getClient(SplashActivity.this).create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.getLogin(loginBody);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
//                        Log.d("tokenFrom",loginResponse.getToken());
                        if (loginResponse.getStatus().equals("200")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.token_file), loginResponse.getToken());
                            editor.putString(getString(R.string.username_file), username);
                            editor.putString(getString(R.string.password_file), password);
                            editor.apply();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            Toast.makeText(SplashActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            Toast.makeText(SplashActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }
            });
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }


    }

    public void setApplicationLanguage(String language) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }
}