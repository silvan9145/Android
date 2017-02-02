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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class OrderActivity extends AppCompatActivity {

    private SharedPreferences sharedPref; //used to keep track of a new user
    private float thoValue; //The Total Hourly Overhead value from the first screen
    private DecimalFormat df = new DecimalFormat("0.00"); //used to format the displayed costs
    private DecimalFormat dfHours = new DecimalFormat("0.0"); //used to format the hours values
    private EditText individualGarmentCostEditText, inkMaterialsEditText, numGarmentsEditText, totalGarmentsEditText;
    private EditText totalPerGarmentEditText, designEditText, prePressEditText, printingPackagingEditText, totalHoursEditText;
    private EditText hourlyMinShopProfitEditText, tjcEditText, pgcEditText;
    private boolean isKeyboardOpened;
    private LinearLayout allEditingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initKeyboardDetector();

        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        thoValue = sharedPref.getFloat(getString(R.string.tho_key), 0);

        //Component declarations
        individualGarmentCostEditText = (EditText) findViewById(R.id.individualGarmentCostEditText);
        inkMaterialsEditText = (EditText) findViewById(R.id.inkMaterialsEditText);
        numGarmentsEditText = (EditText) findViewById(R.id.numGarmentsEditText);
        totalGarmentsEditText = (EditText) findViewById(R.id.totalGarmentsMaterialsEditText);
        totalPerGarmentEditText = (EditText) findViewById(R.id.totalPerGarmentMaterialsEditText);
        designEditText = (EditText) findViewById(R.id.designEditText);
        prePressEditText = (EditText) findViewById(R.id.prePressEditText);
        printingPackagingEditText = (EditText) findViewById(R.id.printingPackagingEditText);
        totalHoursEditText = (EditText) findViewById(R.id.totalHoursEditText);
        hourlyMinShopProfitEditText = (EditText) findViewById(R.id.hourlyMinShopProfitEditText);
        tjcEditText = (EditText) findViewById(R.id.totalJobCostEditText);
        pgcEditText = (EditText) findViewById(R.id.perGarmentCostEditText);

        allEditingContainer = (LinearLayout) findViewById(R.id.allEditingContainer);
        allEditingContainer.setOnTouchListener(new View.OnTouchListener() {
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarOrder);
        setSupportActionBar(myToolbar);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/trench.ttf");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitleOrder);
        toolbarTitle.setTypeface(titleFont);
        toolbarTitle.setShadowLayer(10, 0, 0, Color.BLACK);

        //Set the icons of the TJC and PGC EditTexts
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.money);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        BitmapDrawable icon = new BitmapDrawable(getResources(), resizedBitmap);
        tjcEditText.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        pgcEditText.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

        //For auto updating the values in other EditText boxes when input is entered in 1 box
        individualGarmentCostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(individualGarmentCostEditText);
                value += returnFloatValue(inkMaterialsEditText);
                totalPerGarmentEditText.setText(df.format(value));
                value *= returnFloatValue(numGarmentsEditText);
                totalGarmentsEditText.setText(df.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        inkMaterialsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(individualGarmentCostEditText);
                value += returnFloatValue(inkMaterialsEditText);
                totalPerGarmentEditText.setText(df.format(value));
                value *= returnFloatValue(numGarmentsEditText);
                totalGarmentsEditText.setText(df.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        numGarmentsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(totalPerGarmentEditText);
                value *= returnFloatValue(numGarmentsEditText);
                totalGarmentsEditText.setText(df.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        designEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(designEditText);
                value += returnFloatValue(prePressEditText);
                value += returnFloatValue(printingPackagingEditText);
                totalHoursEditText.setText(dfHours.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        prePressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(designEditText);
                value += returnFloatValue(prePressEditText);
                value += returnFloatValue(printingPackagingEditText);
                totalHoursEditText.setText(dfHours.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        printingPackagingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                float value = returnFloatValue(designEditText);
                value += returnFloatValue(prePressEditText);
                value += returnFloatValue(printingPackagingEditText);
                totalHoursEditText.setText(dfHours.format(value));
                updateTjcEditText();
                updatePgcEditText();
            }
        });
        hourlyMinShopProfitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTjcEditText();
                updatePgcEditText();
            }
        });
    }

    private void initKeyboardDetector() {
        final View activityRootView = findViewById(R.id.activity_order);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(OrderActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    isKeyboardOpened = true;
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent; //used to take actions
        switch (item.getItemId()) {
            case R.id.action_back:
                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.new_user_key), 5);
                editor.commit();
                intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); //prevent user from being able to press back
                return true;
            case R.id.action_share:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, "Number of Garments: " + (int)returnFloatValue(numGarmentsEditText) +
                ", \nTotal Job Cost: $" + df.format(returnFloatValue(tjcEditText)) +
                ", \nPer Garment Cost: $" + df.format(returnFloatValue(pgcEditText)));

                /*
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                if (activities.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Error: no available apps for sharing", Toast.LENGTH_SHORT).show();
                }
                else {
                    Collections.sort(activities, new ResolveInfo.DisplayNameComparator(packageManager));
                }*/
                startActivity(Intent.createChooser(intent, "Share"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //For returning a float value of the string being displayed in an EditText box
    private float returnFloatValue(EditText e) {
        String str = e.getText().toString();
        if (str.equals("") || str.equals(".") || str.equals("N/A"))
            return 0;
        else
            return Float.parseFloat(str);
    }

    //Updates the Total Job Cost EditText value
    private void updateTjcEditText() {
        float val1 = returnFloatValue(totalHoursEditText);
        float val2 = val1;
        val1 *= thoValue;

        val2 += returnFloatValue(hourlyMinShopProfitEditText);
        val2 += returnFloatValue(totalGarmentsEditText);
        tjcEditText.setText(df.format(val1 + val2));
    }

    //Updates the Per Garment Cost EditText value
    private void updatePgcEditText() {
        float value = returnFloatValue(tjcEditText);
        float numGarments = returnFloatValue(numGarmentsEditText);
        if (numGarments == 0)
            pgcEditText.setText("N/A");
        else {
            value /= numGarments;
            pgcEditText.setText(df.format(value));
        }
    }
}
