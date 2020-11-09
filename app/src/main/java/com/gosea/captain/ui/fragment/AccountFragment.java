package com.gosea.captain.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.CheckOutStatus;
import com.gosea.captain.models.CheckedStatusResponse;
import com.gosea.captain.models.DistanceModel;
import com.gosea.captain.models.profile.ProfileResponse;
import com.gosea.captain.ui.activity.LoginActivity;
import com.gosea.captain.ui.activity.MainActivity;
import com.gosea.captain.ui.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private TextView username, email;
    CardView languageCard, logoutCard, profileCard;
    private Context context;
    Button checkInButton;
    private double lon = 0, lat = 0;
    private CharSequence[] items = new CharSequence[]{"English", "Arabic"};
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        // requestAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
        }
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
                deleteQueue();

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
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        boolean trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
        if (trip) {
            Toast.makeText(context, R.string.cannot_check_out, Toast.LENGTH_SHORT).show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
            Call<CheckOutStatus> call = apiInterface.checkOut();
            call.enqueue(new Callback<CheckOutStatus>() {
                @Override
                public void onResponse(Call<CheckOutStatus> call, Response<CheckOutStatus> response) {
                    CheckOutStatus checkOutStatus = response.body();
                    if (checkOutStatus != null) {
                        if (!checkOutStatus.isStatus()) {
                            setCheckIn();
                            SharedPreferences checkOutPref = context.getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
                            checkOutPref.edit().putBoolean("check_in", false).apply();
                            Toast.makeText(getContext(), R.string.checked_out, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckOutStatus> call, Throwable t) {
                    Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void setCheckIn() {
        checkInButton.setText(R.string.check_in);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != 0 && lon != 0) {
                    checkDistance();
                } else {
                    Toast.makeText(context, "Location Not Updated!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void checkDistance() {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<ArrayList<DistanceModel>> call = apiInterface.getDistance();
        call.enqueue(new Callback<ArrayList<DistanceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DistanceModel>> call, Response<ArrayList<DistanceModel>> response) {
                ArrayList<DistanceModel> distanceModels = response.body();
                if (distanceModels != null) {
                    double distance = distance(lat, lon, distanceModels.get(0).getLat(), distanceModels.get(0).getLon());
                    distance = distance * 1000;
                    int distInt = (int) distance;
                    Log.d("distance", distInt + " " + lat + " " + lon + " " + distanceModels.get(0).getRadius());
                    if (distanceModels.get(0).getRadius() >= distInt) {
                        checkIn();
                    } else {
                        Toast.makeText(context, "You Are Not In Area", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DistanceModel>> call, Throwable t) {

            }
        });

    }

    private void checkIn() {
        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        Call<CheckedStatusResponse> call = apiInterface.checkIn();
        call.enqueue(new Callback<CheckedStatusResponse>() {
            @Override
            public void onResponse(Call<CheckedStatusResponse> call, Response<CheckedStatusResponse> response) {
                CheckedStatusResponse checkedStatusResponse=response.body();
                if (checkedStatusResponse!=null) {
                    if (checkedStatusResponse.isStatus()) {
                        SharedPreferences checkOutPref = context.getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
                        checkOutPref.edit().putBoolean("check_in", true).apply();
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        try {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            // requestAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
            } else {
                Toast.makeText(getContext(), "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void deleteQueue() {
        ProgressDialog progressDialoe = new ProgressDialog(context);
        progressDialoe.setCancelable(false);
        progressDialoe.setMessage(getString(R.string.removing_from_queue));
        progressDialoe.show();
        ApiInterface apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        Call<BasicResponse> call = apiInterface.delQueue();
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                progressDialoe.dismiss();
                Toast.makeText(context, R.string.get_out_from_queue, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.user_file), Context.MODE_PRIVATE);
                SharedPreferences sharedPreferencesTrip = context.getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
                SharedPreferences sharedPreferencesCheck = context.getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                sharedPreferencesTrip.edit().clear().apply();
                sharedPreferencesCheck.edit().clear().apply();
                startActivity(new Intent(context, LoginActivity.class));
                getActivity().finish();


            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                progressDialoe.dismiss();
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}