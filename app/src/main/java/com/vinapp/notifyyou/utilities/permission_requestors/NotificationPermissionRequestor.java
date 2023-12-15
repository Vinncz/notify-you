package com.vinapp.notifyyou.utilities.permission_requestors;

import android.Manifest;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.vinapp.notifyyou.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationPermissionRequestor implements PermissionRequestor {

    private AppCompatActivity activityInstance;
    public NotificationPermissionRequestor (AppCompatActivity _activityInstance) {
        this.activityInstance = _activityInstance;
    }

    @Override
    public boolean requestPermission () {
        if ( activityInstance == null ) return false;

        AtomicBoolean result = new AtomicBoolean(false);
        ActivityResultLauncher<String> permissionLauncherSingle = activityInstance.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),

                userGrantsNotificationPermission -> {
                    if ( userGrantsNotificationPermission ) {
                        result.set(true);
                    } else {
                        result.set(false);
                    }
                }
        );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            permissionLauncherSingle.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        return result.get();
    }
}
