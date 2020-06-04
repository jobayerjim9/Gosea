package com.gosea.captain.controller.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gosea.captain.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://157.245.181.14:8080/";
    private static Retrofit retrofit = null;


//    public static String getAuthCredential() {
//        String base = USER
//        return "basic "+
//    }

    public static Retrofit getClient(Context context) {

        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getString(R.string.user_file),Context.MODE_PRIVATE);
        final String token=sharedPreferences.getString(context.getString(R.string.token_file),"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJ1c2VybmFtZSI6ImJpbGFsam1hbCIsImV4cCI6MTU5MTgyNjM0MiwiZW1haWwiOiJiaWxhbGptYWxAZ21haWwuY29tIiwib3JpZ19pYXQiOjE1OTEyMjE1NDJ9.WwjpWoElziHpmW3ylPN5uKm5n_dtS2UnqqicOCy69CU");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(160, TimeUnit.SECONDS)
                .writeTimeout(160, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                                    "Bearer " + token);
                            Request newRequest = builder.build();
                            return chain.proceed(newRequest);


                    }
                }).addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
