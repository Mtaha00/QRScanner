package com.example.qrscanner59;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button openCameraBtn;
    private TextView instruction;
    private TextView details;

    private String[] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.INTERNET};
    private static final int PERMISSION_REQ_CODE =100;
    private static final int ACTIVITY_REQ_CODE=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCameraBtn = findViewById(R.id.button);
        instruction = findViewById(R.id.instruction_txt);
        details = findViewById(R.id.details_link_txt);

        openCameraBtn.setOnClickListener(this);
        details.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if(ActivityCompat.checkSelfPermission(this,permissions[0])== PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else {
                    ActivityCompat.requestPermissions(this,permissions, PERMISSION_REQ_CODE);
                }
                break;

            case R.id.details_link_txt:
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(details.getText().toString()));
                startActivity(intent);
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== PERMISSION_REQ_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                ActivityCompat.requestPermissions(this,permissions, PERMISSION_REQ_CODE);
            }
        }

    }
    private void openCamera(){
        Intent intent=new Intent(MainActivity.this,ScannerActivity.class);
        startActivityForResult(intent,ACTIVITY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ACTIVITY_REQ_CODE&&resultCode==RESULT_OK){
            instruction.setVisibility(View.VISIBLE);
            details.setText(data.getStringExtra(ScannerActivity.RESULT_KEY));
        }

    }
}