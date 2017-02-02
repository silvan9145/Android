package com.davidsilvan.sleepbuddy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    private static final String INTENT_KEY = "Intent Key";
    private static final int AUDIO_PERMISSION = 12;
    private static final int WRITE_PERMISSION = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }

        TabHost host = getTabHost();

        int num = getIntent().getIntExtra(INTENT_KEY, 0);
        Intent journalActivityIntent = new Intent(this, JournalActivity.class);
        if (num == 5) {
            journalActivityIntent.putExtra(INTENT_KEY, 5);
        }
        else {
            journalActivityIntent.putExtra(INTENT_KEY, 0);
        }

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(new Intent(this, TipsActivity.class));
        spec.setIndicator("Tips");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab2");
        spec.setContent(journalActivityIntent);
        spec.setIndicator("Journal");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab3");
        spec.setContent(new Intent(this, NightlightActivity.class));
        spec.setIndicator("Nightlight");
        host.addTab(spec);

        host.setCurrentTab(1);
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialogAudio();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialogWrite();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        showPermissionDialogAudio();
                    }
                }
                return;
            }
            case WRITE_PERMISSION: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showPermissionDialogWrite();
                    }
                }
                return;
            }
        }
    }

    private void showPermissionDialogAudio() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Please allow audio recording")
                .setMessage("You must allow Sleep Buddy to record audio in order for the app to run correctly. " +
                        "Please press \"Allow\" on the next dialog to ensure correct behavior!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showPermissionDialogWrite() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Please allow filesystem access")
                .setMessage("You must allow Sleep Buddy to access the filesystem on your device in order for the app " +
                        "to run correctly. Please press \"Allow\" on the next dialog to ensure correct behavior!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}