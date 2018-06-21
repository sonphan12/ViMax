package com.sonphan12.vimax.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sonphan12.vimax.ui.MainActivity;
import com.sonphan12.vimax.ui.SplashActivity;

import java.lang.ref.WeakReference;

public class PermissionHelper {
    WeakReference<Activity> activityWeakReference;

    public PermissionHelper(@NonNull Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void checkAndRequestPermission() {
        if (hasUngrantedPermission()) {
            ActivityCompat.requestPermissions(activityWeakReference.get(),
                    AppConstants.PERMISSIONS,
                    AppConstants.PERMISSION_REQUEST_ALL);
        } else {
            activityWeakReference.get().startActivity(new Intent(activityWeakReference.get(), MainActivity.class));
            activityWeakReference.get().finish();
        }
    }

    public boolean hasUngrantedPermission() {
        for (String permission : AppConstants.PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activityWeakReference.get(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

}
