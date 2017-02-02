package com.davidsilvan.fileconcealer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by David on 3/30/2016.
 */
public class ViewFileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_file);
        final TextView text = (TextView) findViewById(R.id.fileContents);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);
        try {
            File f = new File(message);
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            text.setText(sb);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: couldn't read file", Toast.LENGTH_LONG).show();
        }

    }
}
