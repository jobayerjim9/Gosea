package com.gosea.captain.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.CheckOutStatus;
import com.gosea.captain.models.CheckedStatusResponse;
import com.gosea.captain.models.profile.ProfileResponse;
import com.gosea.captain.ui.activity.LoginActivity;
import com.gosea.captain.ui.activity.MainActivity;
import com.gosea.captain.ui.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private TextView username, email;
    CardView languageCard, logoutCard, profileCard;
    private Context context;
    Button checkInButton;
    private CharSequence[] items = new CharSequence[]{"English", "Arabic"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        username = v.findViewById(R.id.username);
        email = v.findViewById(R.id.email);
        checkInButton = v.findViewById(R.id.checkInButton);
        languageCard = v.findViewById(R.id.languageCard);
        profileCard = v.findViewById(R.id.profileCard);
        logoutCard = v.findViewById(R.id.logoutCard);
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });
        languageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.choose_language)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    updateEnglish();
                                } else if (which == 1) {
                                    updateArabic();
                                }
                            }
                        }).show();
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(context, LoginActivity.class));
                getActivity().finish();
            }
        });
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
        checkInButton.setText(R.string.check_out);
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
        checkInButton.setText(R.string.check_in);
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
                        Toast.makeText(getContext(), R.string.check_in_success, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckedStatusResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void updateArabic() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.lang_file), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.language), "ar").apply();
        setApplicationLanguage("ar");
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateEnglish() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.lang_file), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.language), "en").apply();
        setApplicationLanguage("en");
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setApplicationLanguage(String language) {


        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }
}