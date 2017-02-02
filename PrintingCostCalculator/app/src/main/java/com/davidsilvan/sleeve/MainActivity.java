package com.davidsilvan.sleeve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Intent myIntent; //used to switch to the individual order costs screen
    private SharedPreferences sharedPref; //used to keep track of a new user
    private EditText rentEditText, utilitiesEditText, phoneEditText, insuranceEditText, miscEditText;
    private EditText tmoEditText, hlrEditText, hoursEditText, mlrEditText, thoEditText;
    private DecimalFormat df = new DecimalFormat("0.00"); //used to format the displayed costs
    private DecimalFormat dfHours = new DecimalFormat("0.0"); //used to format the hours values
    private LinearLayout overheadEditingContainer;
    private boolean isKeyboardOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        myIntent = new Intent(this, OrderActivity.class);

        //Component declarations
        rentEditText = (EditText) findViewById(R.id.rentEditText);
        utilitiesEditText = (EditText) findViewById(R.id.utilitiesEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        insuranceEditText = (EditText) findViewById(R.id.insuranceEditText);
        miscEditText = (EditText) findViewById(R.id.miscEditText);
        tmoEditText = (EditText) findViewById(R.id.tmoEditText);
        hlrEditText = (EditText) findViewById(R.id.hlrEditText);
        hoursEditText = (EditText) findViewById(R.id.hoursEditText);
        mlrEditText = (EditText) findViewById(R.id.mlrEditText);
        thoEditText = (EditText) findViewById(R.id.thoEditText);
        Button continueButton = (Button) findViewById(R.id.nextScreenButton);

        initKeyboardDetector();
        overheadEditingContainer = (LinearLayout) findViewById(R.id.overheadEditingContainer);
        overheadEditingContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (isKeyboardOpened) {
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    isKeyboardOpened = false;
                }
                return false;
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(myToolbar);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/trench.ttf");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //Set font of the action bar TextView and give it a shadow
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitleMain);
        toolbarTitle.setTypeface(titleFont);
        toolbarTitle.setShadowLayer(10, 0, 0, Color.BLACK);

        //Set the icon of the Total Hourly Overhead EditText
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.money);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        BitmapDrawable icon = new BitmapDrawable(getResources(), resizedBitmap);
        thoEditText.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        setEditTextValues(); //Update the EditTexts with the previously entered values

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if Total Hourly Overhead = 0, dont allow the user to continue
                float thoValue = returnFloatValue(thoEditText);
                if (thoValue != 0) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.new_user_key), 0);
                    editor.putFloat(getString(R.string.tho_key), returnFloatValue(thoEditText));
                    editor.putFloat(getString(R.string.rent_key), returnFloatValue(rentEditText));
                    editor.putFloat(getString(R.string.utilities_key), returnFloatValue(utilitiesEditText));
                    editor.putFloat(getString(R.string.phone_key), returnFloatValue(phoneEditText));
                    editor.putFloat(getString(R.string.insurance_key), returnFloatValue(insuranceEditText));
                    editor.putFloat(getString(R.string.misc_key), returnFloatValue(miscEditText));
                    editor.putFloat(getString(R.string.hlr_key), returnFloatValue(hlrEditText));
                    editor.putFloat(getString(R.string.num_hours_key), returnFloatValue(hoursEditText));
                    editor.commit();
                    startActivity(myIntent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter all values first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //For auto updating the values in other EditText boxes when input is entered in 1 box
        rentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTmoEditText();
                updateThoEditText();
            }
        });
        utilitiesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTmoEditText();
                updateThoEditText();
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTmoEditText();
                updateThoEditText();
            }
        });
        insuranceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTmoEditText();
                updateThoEditText();
            }
        });
        miscEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTmoEditText();
                updateThoEditText();
            }
        });
        hlrEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateMlrEditText();
                updateThoEditText();
            }
        });
        hoursEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateMlrEditText();
                updateThoEditText();
            }
        });
    }

    //For returning a float value of the string being displayed in an EditText box
    private float returnFloatValue(EditText e) {
        String str = e.getText().toString();
        if (str.equals("") || str.equals(".") || str.equals("N/A"))
            return 0;
        else
            return Float.parseFloat(str);
    }

    //Updates the Total Monthly Overhead EditText value
    private void updateTmoEditText() {
        float value = returnFloatValue(rentEditText);
        value += returnFloatValue(utilitiesEditText);
        value += returnFloatValue(phoneEditText);
        value += returnFloatValue(insuranceEditText);
        value += returnFloatValue(miscEditText);
        tmoEditText.setText(df.format(value));
    }

    //Updates the Monthly Labor Rate EditText value
    private void updateMlrEditText() {
        float value = returnFloatValue(hlrEditText);
        value *= returnFloatValue(hoursEditText);
        mlrEditText.setText(df.format(value));
    }

    //Updates the Total Hourly Overhead EditText value
    private void updateThoEditText() {
        float tmo = returnFloatValue(tmoEditText);
        float hlr = returnFloatValue(hlrEditText);
        float hours = returnFloatValue(hoursEditText);
        if (hours == 0)
            thoEditText.setText("N/A");
        else {
            float value = tmo + hlr;
            value /= hours;
            thoEditText.setText(df.format(value));
        }
    }

    //Displays the values in all of the EditTexts
    private void setEditTextValues() {
        float rent = sharedPref.getFloat(getString(R.string.rent_key), 0);
        float utilities = sharedPref.getFloat(getString(R.string.utilities_key), 0);
        float phone = sharedPref.getFloat(getString(R.string.phone_key), 0);
        float insurance = sharedPref.getFloat(getString(R.string.insurance_key), 0);
        float misc = sharedPref.getFloat(getString(R.string.misc_key), 0);
        float hlr = sharedPref.getFloat(getString(R.string.hlr_key), 0);
        float numHours = sharedPref.getFloat(getString(R.string.num_hours_key), 0);

        setTextOrHint(rentEditText, rent);
        setTextOrHint(utilitiesEditText, utilities);
        setTextOrHint(phoneEditText, phone);
        setTextOrHint(insuranceEditText, insurance);
        setTextOrHint(miscEditText, misc);
        setTextOrHint(hlrEditText, hlr);
        if (numHours == 0) {
            hoursEditText.setHint(dfHours.format(numHours));
        }
        else {
            hoursEditText.setText(dfHours.format(numHours));
        }

        updateTmoEditText();
        updateMlrEditText();
        updateThoEditText();
    }

    //Sets the texts or hints of the EditTexts depending on if the value is 0 or not
    private void setTextOrHint(EditText e, float value) {
        if (value == 0) {
            e.setHint(df.format(value));
        }
        else {
            e.setText(df.format(value));
        }
    }

    private void initKeyboardDetector() {
        final View activityRootView = findViewById(R.id.activity_main);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(MainActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    isKeyboardOpened = true;
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
