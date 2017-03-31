package alvi17.onesearch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import alvi17.onesearch.R;
import alvi17.onesearch.base.BaseGooglePlayServicesActivity;
import alvi17.onesearch.framework.WordSearchManager;
import alvi17.onesearch.models.GameDifficulty;
import alvi17.onesearch.models.GameMode;
import alvi17.onesearch.models.GameType;
import alvi17.onesearch.ui.gameplay.WordSearchActivity;

public class MenuActivity extends BaseGooglePlayServicesActivity implements View.OnClickListener {

    private final static String MENU_PREF_NAME = "menu_prefs";
    private final static String FIRST_TIME = "first_time";

    private final static long ROUND_TIME_IN_MS = 120000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.GET_ACCOUNTS
    };
    private static final int REQUEST_APP_PERMISSIONS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_menu_screen;
        // Check if first time opening app, show splash screen
        SharedPreferences prefs = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = getSharedPreferences(MENU_PREF_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();

            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_menu);

        findViewById(R.id.bMenuEasy).setOnClickListener(this);
        findViewById(R.id.bMenuMedium).setOnClickListener(this);
        findViewById(R.id.bMenuHard).setOnClickListener(this);
        //TODO: Reimplement advanced after more efficient way of drawing out the grid
//        findViewById(R.id.bMenuAdvanced).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bMenuSignIn) {
            if(!checkPermission(Manifest.permission.GET_ACCOUNTS))
            {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_APP_PERMISSIONS);
            }
            else {
                mInSignInFlow = true;
                mSignInClicked = true;
                mGoogleApiClient.connect();
                return;
            }
        }
        String gd = null;
        int ga_button_id = -1;
        switch (view.getId()) {
            case R.id.bMenuEasy:
                gd = GameDifficulty.Easy;
                ga_button_id = R.string.ga_click_easy;
                break;
            case R.id.bMenuMedium:
                gd = GameDifficulty.Medium;
                ga_button_id = R.string.ga_click_medium;
                break;
            case R.id.bMenuHard:
                gd = GameDifficulty.Hard;
                ga_button_id = R.string.ga_click_hard;
                break;
//            case R.id.bMenuAdvanced:
//                gd = GameDifficulty.Advanced;
//                break;
        }
        //analyticsTrackEvent(ga_button_id);
        WordSearchManager wsm = WordSearchManager.getInstance();
        wsm.Initialize(new GameMode(GameType.Timed, gd, ROUND_TIME_IN_MS), getApplicationContext());
        wsm.buildWordSearches();
        Intent i = new Intent(getApplicationContext(), WordSearchActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // analyticsTrackScreen(getString(categoryId));
        WordSearchManager.nullify();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        findViewById(R.id.bMenuSignIn).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.bMenuSignIn).setVisibility(View.VISIBLE);
        findViewById(R.id.bMenuSignIn).setOnClickListener(this);
    }


    private boolean checkPermission(String perm)
    {
        return(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, perm));
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
                mInSignInFlow = true;
                mSignInClicked = true;
                mGoogleApiClient.connect();
                return;
            }
            else {
                //showToast("Permissions denied. Exiting.", Color.RED);
                Toast.makeText(getApplicationContext(),"Permission required for Google play games services to work. Kindly Grant permission.",Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
