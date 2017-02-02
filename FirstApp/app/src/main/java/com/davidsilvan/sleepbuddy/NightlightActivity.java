package com.davidsilvan.sleepbuddy;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.provider.Settings.System;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 2/6/2016.
 */
public class NightlightActivity extends Activity {

    private String toast = "Tap screen to change color";
    private ColorPicker cp;
    private int colorRed;
    private int colorGreen;
    private int colorBlue;
    private int brightness;
    private int check;

    private SeekBar brightnessBar;
    private ContentResolver contentResolver;
    private Window window;

    @Bind(R.id.nightlight)
    RelativeLayout light;
    @Bind(R.id.brightnessText)
    TextView brightnessText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nightlight_screen);
        ButterKnife.bind(this);

        brightnessBar = (SeekBar) findViewById(R.id.brightnessBar);
        check = 0; //used to count how many times the nightlight screen has been opened
        colorRed = 255;
        colorGreen = 255;
        colorBlue = 255;
        window = getWindow();
        contentResolver = getContentResolver();
        brightnessBar.setMax(255);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNightlightColor();
        checkSystemWritePermission();

        try {
            brightness = System.getInt(contentResolver, System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
        }

        brightnessText.setText("Brightness: " + Integer.toString((int) (brightness / 2.55)));
        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessText.setText("Brightness: " + Integer.toString((int) (progress / 2.55)));
                System.putInt(contentResolver, System.SCREEN_BRIGHTNESS, progress);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.screenBrightness = progress;
                window.setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Only show the toast message twice in total
        if (check < 2) {
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            check++;
        }
    }

    @OnClick(R.id.nightlight)
    void onClickScreen() {
        final ColorPicker cp = new ColorPicker(NightlightActivity.this, colorRed, colorGreen, colorBlue);
        cp.show();
        Button okColor = (Button)cp.findViewById(R.id.okColorButton);

        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorRed = cp.getRed();
                colorGreen = cp.getGreen();
                colorBlue = cp.getBlue();

                cp.dismiss();
                setNightlightColor();
            }
        });
    }

    public void setNightlightColor() {
        light.setBackgroundColor(Color.rgb(colorRed, colorGreen, colorBlue));
    }

    private void checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean retVal = Settings.System.canWrite(NightlightActivity.this);
            if (!retVal) {
                showPermissionDialog();
            }
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(NightlightActivity.this)
                .setTitle("Please allow audio recording")
                .setMessage("You must allow Sleep Buddy to modify system settings in order to change the brightness")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + NightlightActivity.this.getPackageName()));
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
