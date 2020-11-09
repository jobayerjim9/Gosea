package com.gosea.captain.controller.retrofit;

import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.CheckOutStatus;
import com.gosea.captain.models.CheckedStatusResponse;
import com.gosea.captain.models.DistanceModel;
import com.gosea.captain.models.FirebaseTokenUpdateBody;
import com.gosea.captain.models.LoginBody;
import com.gosea.captain.models.LoginResponse;
import com.gosea.captain.models.QueueModel;
import com.gosea.captain.models.profile.PasswordChangeBody;
import com.gosea.captain.models.profile.ProfileResponse;
import com.gosea.captain.models.queue.FixedQueueReponse;
import com.gosea.captain.models.queue.QueueResponseModel;
import com.gosea.captain.models.ticket.TicketData;
import com.gosea.captain.models.ticket.TicketResponse;
import com.gosea.captain.models.trips.TripStartBody;
import com.gosea.captain.models.trips.TripStartResponse;
import com.gosea.captain.models.trips.TripTimeRemainingResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("auth/")
    Call<LoginResponse> getLogin(@Body LoginBody loginBody);

    @PATCH("atten_in/")
    Call<CheckedStatusResponse> checkIn();

    @PATCH("atten_out/")
    Call<CheckOutStatus> checkOut();

    @GET("pauth/")
    Call<ProfileResponse> getProfile();

    @POST("queue/")
    Call<QueueModel> getQueue();

    @DELETE("queue/")
    Call<BasicResponse> delQueue();

    @GET("dt_br_inv/")
    Call<TicketResponse> getTicketDetails(@Query("barcode") String barcode);

    @POST("trips/")
    Call<TripStartResponse> startTrip(@Body TripStartBody tripStartBody);

    @PATCH("trips_end/{id}")
    Call<BasicResponse> endTrip(@Path("id") String id);

    @GET("queue_detail/")
    Call<ArrayList<QueueResponseModel>> getQueueList();

    @GET("dt_status_inv/")
    Call<ArrayList<TicketData>> getTripHistory(@Query("status") String status);

    @POST("uauth/")
    Call<BasicResponse> changePassword(@Body PasswordChangeBody passwordChangeBody);

    @GET("trips_dtl/")
    Call<TripTimeRemainingResponse> getRemainingTime(@Query("id") String id);

    @POST("token/")
    Call<ResponseBody> updateToken(@Body FirebaseTokenUpdateBody firebaseTokenUpdateBody);

    @POST("auth-refresh/")
    Call<FirebaseTokenUpdateBody> authRefresh(@Body FirebaseTokenUpdateBody firebaseTokenUpdateBody);

    @PATCH("trips_cancel/{id}")
    Call<BasicResponse> cancelTrip(@Path("id") String id);

    @GET("queue-fixed/")
    Call<ArrayList<FixedQueueReponse>> getFixedQueue();

    @GET("distance/")
    Call<ArrayList<DistanceModel>> getDistance();

}
