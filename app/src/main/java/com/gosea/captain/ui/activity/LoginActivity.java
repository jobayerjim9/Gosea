package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.LoginBody;
import com.gosea.captain.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout usernameInput,passwordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
    }

    private void initUi() {
        usernameInput=findViewById(R.id.usernameInput);
        passwordInput=findViewById(R.id.passwordInput);
        Button loginButton=findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        final String username=usernameInput.getEditText().getText().toString().trim();
        final String password=passwordInput.getEditText().getText().toString().trim();
        if (username.isEmpty()) {
            usernameInput.setErrorEnabled(true);
            usernameInput.setError(getString(R.string.please_enter_username));
        }
        else if (password.isEmpty()) {
            passwordInput.setErrorEnabled(true);
            passwordInput.setError(getString(R.string.please_enter_password));
        }
        else {
            usernameInput.setErrorEnabled(false);
            passwordInput.setErrorEnabled(false);
            LoginBody loginBody=new LoginBody(username,password);
            ApiInterface apiInterface= ApiClient.getClient(LoginActivity.this).create(ApiInterface.class);
            Call<LoginResponse> call=apiInterface.getLogin(loginBody);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse=response.body();
                    if (loginResponse!=null) {
                        if (loginResponse.getStatus().equals("200")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.token_file), loginResponse.getToken());
                            editor.putString(getString(R.string.username_file), username);
                            editor.putString(getString(R.string.password_file), password);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, R.string.login_succesful, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, R.string.login_unsuccess, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}