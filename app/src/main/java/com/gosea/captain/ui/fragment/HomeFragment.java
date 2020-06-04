package com.gosea.captain.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.BasicResponse;
import com.gosea.captain.models.QueueModel;
import com.gosea.captain.models.profile.ProfileResponse;
import com.gosea.captain.models.ticket.TicketResponse;
import com.gosea.captain.ui.activity.BarCodeActivity;
import com.gosea.captain.ui.activity.TicketDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    EditText searchText;
    ImageView searchButton;
    TextView queueStatus,queueText,checkStatus;
    ProfileResponse user;
    CardView queueButton,scanTicket;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        queueButton=v.findViewById(R.id.queueButton);
        checkStatus=v.findViewById(R.id.checkStatus);
        searchText=v.findViewById(R.id.searchText);
        searchButton=v.findViewById(R.id.searchButton);
        scanTicket=v.findViewById(R.id.scanTicket);
        queueStatus=v.findViewById(R.id.queueStatus);
        queueText=v.findViewById(R.id.queueText);
        scanTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BarCodeActivity.class));
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=searchText.getText().toString().trim();
                if (code.isEmpty()) {
                    Toast.makeText(context, "Cannot Be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    searchText.clearFocus();
                    searchText.setText("");
                    validateCode(code);
                }
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
                    if (profileResponse.getProfile().isQ_status()) {
                        setInQueue();
                    }
                    else {
                        setNotInQueue();
                    }
                    if (profileResponse.getProfile().isStatus()) {
                        setInCheck();
                    }
                    else {
                        setNotInCheck();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });
    }

    private void setInQueue() {

        queueText.setText("Remove Queue");
        queueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQueue();
            }
        });
        queueStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.yes_clipart),null,null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            queueStatus.setTextColor(getContext().getColor(R.color.green));
        } else {
            queueStatus.setTextColor(getContext().getResources().getColor(R.color.green));
        }
    }

    private void setNotInQueue() {

        queueText.setText("Queue");
        queueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerQueue();
            }
        });
        queueStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_baseline_close_24),null,null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            queueStatus.setTextColor(getContext().getColor(R.color.red));
        } else {
            queueStatus.setTextColor(getContext().getResources().getColor(R.color.red));
        }

    }

    private void setInCheck() {
        checkStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.yes_clipart),null,null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStatus.setTextColor(getContext().getColor(R.color.green));
        } else {
            checkStatus.setTextColor(getContext().getResources().getColor(R.color.green));
        }
    }

    private void setNotInCheck() {
        checkStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_baseline_close_24),null,null,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStatus.setTextColor(getContext().getColor(R.color.red));
        } else {
            checkStatus.setTextColor(getContext().getResources().getColor(R.color.red));
        }

    }

    private void deleteQueue() {
        ProgressDialog progressDialoe=new ProgressDialog(context);
        progressDialoe.setCancelable(false);
        progressDialoe.setMessage("Removing From Queue");
        progressDialoe.show();
        ApiInterface apiInterface=ApiClient.getClient(context).create(ApiInterface.class);
        Call<BasicResponse> call=apiInterface.delQueue();
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                progressDialoe.dismiss();
                BasicResponse basicResponse=response.body();
                if (basicResponse!=null) {
                    if (basicResponse.getStatus()==200) {
                        Toast.makeText(context, "You Get out From Queue!", Toast.LENGTH_SHORT).show();
                        setNotInQueue();
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                progressDialoe.dismiss();
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerQueue() {
        ProgressDialog progressDialoe=new ProgressDialog(context);
        progressDialoe.setCancelable(false);
        progressDialoe.setMessage("Registering On Queue");
        progressDialoe.show();
        ApiInterface apiInterface=ApiClient.getClient(context).create(ApiInterface.class);
        Call<QueueModel> call=apiInterface.getQueue();
        call.enqueue(new Callback<QueueModel>() {
            @Override
            public void onResponse(Call<QueueModel> call, Response<QueueModel> response) {
                QueueModel queueModel=response.body();
                progressDialoe.dismiss();
                if (queueModel!=null) {
                    Toast.makeText(context, "You are now in Queue!", Toast.LENGTH_SHORT).show();
                    setInQueue();
                }
            }

            @Override
            public void onFailure(Call<QueueModel> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialoe.dismiss();
            }
        });

    }
    private void validateCode(String code) {
        ProgressDialog progressDialoe=new ProgressDialog(context);
        progressDialoe.setCancelable(false);
        progressDialoe.setMessage("Verifying Ticket");
        progressDialoe.show();
        ApiInterface apiInterface= ApiClient.getClient(context).create(ApiInterface.class);
        Call<TicketResponse> call=apiInterface.getTicketDetails(code);
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                TicketResponse ticketResponse=response.body();
                progressDialoe.dismiss();
                if (ticketResponse!=null) {
                    Log.d("ticketStatus",ticketResponse.getStatus()+"");
                    if (ticketResponse.getStatus()==200)
                    {
                        Intent intent=new Intent(context, TicketDetailActivity.class);
                        intent.putExtra("id",ticketResponse.getTicketData().get(0).getId());
                        intent.putExtra("name",ticketResponse.getTicketData().get(0).getOrder().getName());
                        intent.putExtra("phone",ticketResponse.getTicketData().get(0).getOrder().getPhone());
                        intent.putExtra("peoples",ticketResponse.getTicketData().get(0).getOrder().getPeoples());
                        intent.putExtra("timeName",ticketResponse.getTicketData().get(0).getOrder().getTimeslot().getName());
                        intent.putExtra("duration",ticketResponse.getTicketData().get(0).getOrder().getTimeslot().getDuration());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "Invalid Ticket", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                progressDialoe.dismiss();
                Toast.makeText(context, "Invalid Ticket", Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }
}