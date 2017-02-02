package com.davidsilvan.spammer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText body, recipients, numMessages;
    private int messageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        recipients = (EditText) findViewById(R.id.recipients);
        body = (EditText) findViewById(R.id.body);
        numMessages = (EditText) findViewById(R.id.numMessages);
        Button minusButton = (Button) findViewById(R.id.minusButton);
        Button plusButton = (Button) findViewById(R.id.plusButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = recipients.getText().toString();
                String msg = body.getText().toString();
                messageCount = Integer.parseInt(numMessages.getText().toString());

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (int i = 0; i < messageCount; i++) {
                        smsManager.sendTextMessage(number, null, msg, null, null);
                    }
                    Toast.makeText(getApplicationContext(), "Messages Sent!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Message delivery failed.\nPlease try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageCount = Integer.parseInt(numMessages.getText().toString()) - 1;
                numMessages.setText("" + messageCount);
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageCount = Integer.parseInt(numMessages.getText().toString()) + 1;
                numMessages.setText("" + messageCount);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }
}
