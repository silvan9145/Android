package com.davidsilvan.simon;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer; //used for playing sound effects
    private SpannableString coloredTextSimon; //Used to color the text
    private SpannableString coloredTextYourTurn; //Used to color the text
    private String[] colorArray = {"GREEN", "RED", "YELLOW", "BLUE"}; //Array of color values
    private String currentColor; //The current color being displayed
    private int counter; //used to iterate through the color sequence
    private int score = 0; //used to hold the user's score
    private String firstValue; //used tp store the first value of the color sequence
    private Random random = new Random(); //For generating a random color value
    private LinkedQueue queue; //The queue used to store the actual color sequence
    private Button startButton, greenButton, redButton, yellowButton, blueButton; //The game buttons
    private TextView scoreLabel; //Used to display the score
    private TextView statusLabel; //Used to display the game status
    private Handler handler = new Handler(); //Used as a timer
    private Runnable sequenceRunnable = new Runnable() { //Displays the current color sequence
        @Override
        public void run() {
            resetColors(); //reset the colors so that they don't stay lit up
            if (counter <= 0) { //if the end of the sequence has been reached, stop the repetition
                handler.removeCallbacks(sequenceRunnable);
                statusLabel.setTextColor(Color.WHITE);
                input(); //allow the user to input the sequence
            }
            else {
                //Light up the button that is next in the color sequence
                currentColor = queue.moveToEnd();
                counter--; //decrement the counter
                if (currentColor.equals("GREEN")) {
                    statusLabel.setTextColor(Color.GREEN);
                    greenButton.setPressed(true);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.green);
                    mediaPlayer.start();
                }
                else if (currentColor.equals("RED")) {
                    statusLabel.setTextColor(Color.RED);
                    redButton.setPressed(true);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.red);
                    mediaPlayer.start();
                }
                else if (currentColor.equals("YELLOW")) {
                    statusLabel.setTextColor(Color.YELLOW);
                    yellowButton.setPressed(true);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yellow);
                    mediaPlayer.start();
                }
                else if (currentColor.equals("BLUE")) {
                    statusLabel.setTextColor(Color.rgb(30, 100, 255));
                    blueButton.setPressed(true);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.blue);
                    mediaPlayer.start();
                }

                handler.postDelayed(this, 1000); //loop the runnable
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Component instantiations
        startButton = (Button) findViewById(R.id.startButton);
        greenButton = (Button) findViewById(R.id.greenButton);
        redButton = (Button) findViewById(R.id.redButton);
        yellowButton = (Button) findViewById(R.id.yellowButton);
        blueButton = (Button) findViewById(R.id.blueButton);
        statusLabel = (TextView) findViewById(R.id.statusLabel);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);

        //Setting the buttons' OnClickListeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                startButton.setEnabled(false);
                startButton.setText(coloredTextSimon);
                startButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                startButton.setBackgroundResource(R.color.black);
            }
        });
        greenButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.green);
                    mediaPlayer.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInput("GREEN");
                }
                return false;
            }
        });
        redButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.red);
                    mediaPlayer.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInput("RED");
                }
                return false;
            }
        });
        yellowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yellow);
                    mediaPlayer.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInput("YELLOW");
                }
                return false;
            }
        });
        blueButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        stopMedia();
                    }
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.blue);
                    mediaPlayer.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    checkInput("BLUE");
                }
                return false;
            }
        });

        //Initialize fonts
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/squares.otf");
        startButton.setTypeface(face);
        scoreLabel.setTypeface(face);
        statusLabel.setTypeface(face);

        //Colored Text for "Simon"
        coloredTextSimon = new SpannableString("Simon");
        coloredTextSimon.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextSimon.setSpan(new ForegroundColorSpan(Color.RED), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextSimon.setSpan(new ForegroundColorSpan(Color.YELLOW), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextSimon.setSpan(new ForegroundColorSpan(Color.rgb(30, 100, 255)), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextSimon.setSpan(new ForegroundColorSpan(Color.GREEN), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        coloredTextYourTurn = new SpannableString("Your Turn");
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.RED), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.YELLOW), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.rgb(30, 100, 255)), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.GREEN), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.RED), 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.YELLOW), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coloredTextYourTurn.setSpan(new ForegroundColorSpan(Color.rgb(30, 100, 255)), 8, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        statusLabel.setText(coloredTextSimon);
        setButtonsDisabled();
    }

    //When the start button is pressed, the color sequence starts with 1 random color
    public void start() {
        score = 0;
        scoreLabel.setText("Score:\n0");
        int value = random.nextInt(colorArray.length); //generate a random index for colorArray
        firstValue = colorArray[value];
        queue = new LinkedQueue(firstValue); //initialize queue with first color value
        showSequence();
    }

    //Add one more value to the sequence of colors (the previous user-inputted color sequence was
    //correct)
    public void add() {
        int value = random.nextInt(colorArray.length); //generate a random index for colorArray
        String lastColor = queue.getLastColor();
        while (colorArray[value].equals(lastColor)) { //no repeats of colors
            value = random.nextInt(colorArray.length);
        }
        queue.add(colorArray[value]);
    }

    //This method will show the current sequence for the user to memorize
    public void showSequence() {
        statusLabel.setText("Showing sequence");
        counter = queue.getSize();
        handler.postDelayed(sequenceRunnable, 500);
    }

    //Makes buttons unpressable while adding 1 more value to the color sequence
    public void pause() {
        setButtonsDisabled();
        scoreLabel.setText("Score:\n" + score);
        add();
        showSequence();
    }

    //When the user loses
    public void end() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lose);
        mediaPlayer.start();
        statusLabel.setText("You lose!");
        queue = null;
        setButtonsDisabled();
        startButton.setEnabled(true);
        startButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        startButton.setBackgroundResource(R.drawable.start_button_selector);
        startButton.setText("PLAY");
    }

    //When the user is inputting the color sequence
    public void input() {
        //initialize counter for keeping track of when the queue has been fully traversed
        statusLabel.setText(coloredTextYourTurn);
        counter = queue.getSize();
        setButtonsEnabled();
    }

    //Method for setting the buttons back to original color
    public void resetColors() {
        greenButton.setPressed(false);
        redButton.setPressed(false);
        yellowButton.setPressed(false);
        blueButton.setPressed(false);
    }

    //Checks if the last color button that the user pressed was correct
    public void checkInput(String input) {
        if (counter > 0) {
            String color = queue.moveToEnd();
            counter--;
            if (!input.equals(color)) { //If the user entered the wrong color, end the game
                end();
            }
            else if (counter == 0) {
                score += 100;
                pause();
            }
        }
    }

    //Enables the color buttons
    public void setButtonsEnabled() {
        greenButton.setEnabled(true);
        redButton.setEnabled(true);
        yellowButton.setEnabled(true);
        blueButton.setEnabled(true);
    }

    //Disables the color buttons
    public void setButtonsDisabled() {
        greenButton.setEnabled(false);
        redButton.setEnabled(false);
        yellowButton.setEnabled(false);
        blueButton.setEnabled(false);
    }

    //Stops the currently playing sound file
    public void stopMedia() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
