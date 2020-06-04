package com.gosea.captain.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.gosea.captain.R;
import com.gosea.captain.controller.retrofit.ApiClient;
import com.gosea.captain.controller.retrofit.ApiInterface;
import com.gosea.captain.models.ticket.TicketResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarCodeActivity extends AppCompatActivity {
    CodeScanner mCodeScanner;
    final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        if (ContextCompat.checkSelfPermission(BarCodeActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(BarCodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(BarCodeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(BarCodeActivity.this, permissions, 1);
        }
        else {
            startScanner();
        }

    }

    private void startScanner() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        ArrayList<BarcodeFormat> barcodeFormats=new ArrayList<>();
        barcodeFormats.add(BarcodeFormat.QR_CODE);
        barcodeFormats.add(BarcodeFormat.CODABAR);
        mCodeScanner.setFormats(barcodeFormats);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                String code=result.getText();
                if (code.isEmpty()) {
                    Toast.makeText(BarCodeActivity.this, "Empty Ticket Or Fake Ticket", Toast.LENGTH_SHORT).show();
                }
                else {
                    validateCode(code);
                }

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void validateCode(String code) {
        ProgressDialog progressDialoe=new ProgressDialog(BarCodeActivity.this);
        progressDialoe.setCancelable(false);
        progressDialoe.setMessage("Verifying Ticket");
        progressDialoe.show();
        ApiInterface apiInterface= ApiClient.getClient(BarCodeActivity.this).create(ApiInterface.class);
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
                        Intent intent=new Intent(BarCodeActivity.this,TicketDetailActivity.class);
                        intent.putExtra("id",ticketResponse.getTicketData().get(0).getId());
                        intent.putExtra("name",ticketResponse.getTicketData().get(0).getOrder().getName());
                        intent.putExtra("phone",ticketResponse.getTicketData().get(0).getOrder().getPhone());
                        intent.putExtra("peoples",ticketResponse.getTicketData().get(0).getOrder().getPeoples());
                        intent.putExtra("timeName",ticketResponse.getTicketData().get(0).getOrder().getTimeslot().getName());
                        intent.putExtra("duration",ticketResponse.getTicketData().get(0).getOrder().getTimeslot().getDuration());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(BarCodeActivity.this, "Invalid Ticket", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                progressDialoe.dismiss();
                Toast.makeText(BarCodeActivity.this, "Invalid Ticket", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner!=null)
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (mCodeScanner!=null)
        mCodeScanner.releaseResources();
        super.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");
                startScanner();

            } else {
                Toast.makeText(this,"Allow Permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(BarCodeActivity.this, this.permissions, 1);
            }
        }

    }
}