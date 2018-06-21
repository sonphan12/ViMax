package com.sonphan12.vimax.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.PermissionHelper;

public class SplashActivity extends AppCompatActivity {
    PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        permissionHelper = new PermissionHelper(this);
        permissionHelper.checkAndRequestPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PERMISSION_REQUEST_ALL: {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }
                finish();
            }
        }
    }
}
