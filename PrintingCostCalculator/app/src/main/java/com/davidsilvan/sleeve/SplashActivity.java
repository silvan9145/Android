package com.davidsilvan.sleeve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000; //duration of splash screen
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/trench.ttf");
        TextView splashText = (TextView) findViewById(R.id.splashTextView);
        splashText.setTypeface(font);

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int newUserValue = sharedPref.getInt(getString(R.string.new_user_key), 5);

        //if the user is not new, take them to the individual order costs screen
        if (newUserValue != 5) {
            intent = new Intent(SplashActivity.this, OrderActivity.class);
        }
        else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }

        //After a delay, load the appropriate activity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
