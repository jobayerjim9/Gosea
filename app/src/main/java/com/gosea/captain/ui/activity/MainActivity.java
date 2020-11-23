package com.gosea.captain.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.ViewPagerAdapter;
import com.gosea.captain.controller.helper.SoundService;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.CheckOutStatus;
import com.gosea.captain.models.DistanceModel;
import com.gosea.captain.models.FirebaseTokenUpdateBody;
import com.gosea.captain.ui.fragment.AccountFragment;
import com.gosea.captain.ui.fragment.HomeFragment;
import com.gosea.captain.ui.fragment.QueueFragment;
import com.gosea.captain.ui.fragment.TripsFragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationProvider provider =
                    locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(false);
            String providerName = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30, 5, this);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token: ", "token " + refreshedToken);
        FirebaseTokenUpdateBody firebaseTokenUpdateBody = new FirebaseTokenUpdateBody(refreshedToken);
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiInterface.updateToken(firebaseTokenUpdateBody);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
            boolean existTrip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
            if (existTrip) {
                int id = sharedPreferences.getInt(getString(R.string.trip_id), 0);
                Intent intent = new Intent(this, TripDetailsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new AccountFragment());
        viewPagerAdapter.addFragment(new QueueFragment());
        viewPagerAdapter.addFragment(new TripsFragment());
        viewPager.setAdapter(viewPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.home) {
                    viewPager.setCurrentItem(0);
                } else if (item.getItemId()==R.id.account) {
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId()==R.id.queue) {
                    viewPager.setCurrentItem(2);
                } else if (item.getItemId() == R.id.trips) {
                    viewPager.setCurrentItem(3);
                } else {
                    return false;
                }
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("App Destroyed", "App Destroyed");
        editor.putBoolean(getString(R.string.trip_time), false);
        editor.commit();
        stopService(new Intent(this, SoundService.class));

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("updateLocation", location.getLatitude() + " " + location.getLongitude());
        SharedPreferences checkOutPref = getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
        if (checkOutPref.getBoolean("check_in", false)) {
            checkDistance(location.getLatitude(), location.getLongitude());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                LocationProvider provider =
                        locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setCostAllowed(false);
                String providerName = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30, 50, this);
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 50, locationListener);
            } else {
                Toast.makeText(MainActivity.this, "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void checkDistance(double lat, double lon) {
        ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
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
                        //   checkIn();
                    } else {
                        Toast.makeText(MainActivity.this, "You Are Not In Area", Toast.LENGTH_SHORT).show();
                        SharedPreferences checkOutPref = getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
                        if (checkOutPref.getBoolean("check_in", false)) {
                            checkOut();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DistanceModel>> call, Throwable t) {

            }
        });

    }

    boolean done = false;
    private void checkOut() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.trip_file), Context.MODE_PRIVATE);
        boolean trip = sharedPreferences.getBoolean(getString(R.string.trip_exist), false);
        if (trip) {
            Toast.makeText(this, R.string.cannot_check_out, Toast.LENGTH_SHORT).show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
            Call<CheckOutStatus> call = apiInterface.checkOut();
            call.enqueue(new Callback<CheckOutStatus>() {
                @Override
                public void onResponse(Call<CheckOutStatus> call, Response<CheckOutStatus> response) {
                    SharedPreferences checkOutPref = getSharedPreferences(getString(R.string.check_info), Context.MODE_PRIVATE);
                    checkOutPref.edit().clear().apply();
                    CheckOutStatus checkOutStatus = response.body();
                    if (checkOutStatus != null) {
                        try {
                            if (!checkOutStatus.isStatus() && !done) {
                                done = true;
                                Toast.makeText(MainActivity.this, R.string.checked_out, Toast.LENGTH_SHORT).show();
                                recreate();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CheckOutStatus> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

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
}