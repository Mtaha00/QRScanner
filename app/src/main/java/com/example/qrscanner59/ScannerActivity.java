package com.example.qrscanner59;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    private CodeScannerView codeScannerView;
    private CodeScanner codeScanner;
    public static final String RESULT_KEY="resultKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        init();
    }

    private void init(){
        codeScannerView=findViewById(R.id.codScanner_view);
        codeScanner=new CodeScanner(this,codeScannerView);

        codeScanner.setDecodeCallback(decodeCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();

    }

    DecodeCallback decodeCallback=new DecodeCallback() {
        @Override
        public void onDecoded(@NonNull Result result) {
            runOnUiThread(new Runnable() {  //چون به صورت پیشفرض روی main thread انجام نمیشه و ما برای تغیرات بهش نیاز داریم
                @Override
                public void run() {
                    new AlertDialog.Builder(ScannerActivity.this)
                    .setTitle("توجه")
                    .setMessage("آیا میخواهید از اطلاعات این QR استفاده کنید؟")
                            .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent();
                                    intent.putExtra(RESULT_KEY,result.getText());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("خیر تلاش مجدد", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    codeScanner.startPreview();
                                }
                            })
                            .show();

                }
            });
        }
    };
}