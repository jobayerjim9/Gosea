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
import com.gosea.captain.models.FirebaseTokenUpdateBody;
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
                        SharedPreferences preferences = getSharedPreferences(getString(R.string.lang_file), Context.MODE_PRIVATE);
                        String lang = preferences.getString(getString(R.string.language), null);
                        if (lang != null) {
                            setApplicationLanguage(lang);
                        }
//                        Log.d("tokenFrom",loginResponse.getToken());
                        if (loginResponse.getStatus().equals("200")) {
                            Toast.makeText(SplashActivity.this, "Login Details Found", Toast.LENGTH_SHORT).show();
                            String tittle = getIntent().getStringExtra("tittle");
                            String body = getIntent().getStringExtra("body");
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.token_file), loginResponse.getToken());
                            editor.putString(getString(R.string.username_file), username);
                            editor.putString(getString(R.string.password_file), password);
                            editor.apply();
                            if (tittle != null) {
                                Intent intent = new Intent(SplashActivity.this, NotificationActivity.class);
                                intent.putExtra("tittle", tittle);
                                intent.putExtra("body", body);
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                Toast.makeText(SplashActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                        } else {


                            // refreshToken();

                        }
                        finish();

                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString(getString(R.string.token_file), "").apply();
                        signIn();
                        // refreshToken();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.lang_file), Context.MODE_PRIVATE);
                    String lang = preferences.getString(getString(R.string.language), null);
                    if (lang != null) {
                        setApplicationLanguage(lang);
                    }
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

    private void refreshToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.token_file), null);
        FirebaseTokenUpdateBody firebaseTokenUpdateBody = new FirebaseTokenUpdateBody(token);
        ApiInterface apiInterface = ApiClient.getClient(SplashActivity.this).create(ApiInterface.class);
        Call<FirebaseTokenUpdateBody> call = apiInterface.authRefresh(firebaseTokenUpdateBody);
        call.enqueue(new Callback<FirebaseTokenUpdateBody>() {
            @Override
            public void onResponse(Call<FirebaseTokenUpdateBody> call, Response<FirebaseTokenUpdateBody> response) {
                FirebaseTokenUpdateBody resp = response.body();
                if (resp != null) {
                    sharedPreferences.edit().putString(getString(R.string.token_file), resp.getToken()).apply();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    Toast.makeText(SplashActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FirebaseTokenUpdateBody> call, Throwable t) {

            }
        });
    }

    public void setApplicationLanguage(String language) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }
}