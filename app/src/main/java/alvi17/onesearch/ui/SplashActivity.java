package alvi17.onesearch.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import alvi17.onesearch.R;
import alvi17.onesearch.base.BaseActivity;

public class SplashActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.GET_ACCOUNTS
    };
    private static final int REQUEST_APP_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        findViewById(R.id.bReadySplash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_APP_PERMISSIONS);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_APP_PERMISSIONS) {
            boolean permissionGranted = true;
            int index = 0;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = false;
                    Log.e(this.getClass().getSimpleName(), "Permission failed :" + permissions[index]);
                    break;
                }
                index++;
            }
            if (permissionGranted) {
                // initialize the agent

            }
            else {
                //showToast("Permissions denied. Exiting.", Color.RED);
                Toast.makeText(getApplicationContext(),"Permission denied. Google play games services won't work.",Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
