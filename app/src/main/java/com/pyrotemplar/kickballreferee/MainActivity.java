package com.pyrotemplar.kickballreferee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

/**
 * Created by Pyrotemplar on 6/27/2015.
 * Main Activity,
 */
public class MainActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = "MainActivity";
    private static final String THREE_FOULS_OPTION = "ThreefoulOption";
    private static final String AUTOMODE = "AutoMode";
    private static final String ADS_FREE_MODE = "adsFreeMode";
    private static final String VIBRATION_MODE = "VibrationMode";
    private static final String HOME_COLOR = "homeColor";
    private static final String AWAY_COLOR = "awayColor";
    private static final String BALL_COLOR = "ballColor";
    private static final String STRIKE_COLOR = "strikeColor";
    private static final String FOUL_COLOR = "foulColor";
    private static final String OUT_COLOR = "outColor";
    private static final String DEFAULT_HOME_NAME = "HOME";
    private static final String DEFAULT_AWAY_NAME = "AWAY";

    private GameTimer timer;
    private boolean isGameClockRunning;
    private boolean newTime;
    private boolean isVibrationModeOn;
    private static boolean vibrate;
    private boolean isAdsFreeModeEnabled;
    private boolean undo;
    private long back_pressed;
    private int milliSecondsToFinish;
    private SharedPreferences prefs = null;
    private static int gameClockTime;
    private Vibrator vibrator;

    private String date;

    private int homeColor;
    private int awayColor;
    private int ballColor;
    private int strikeColor;
    private int foulColor;
    private int outColor;

    ImageButton awayTeamScoreMinusButton;
    ImageButton awayTeamScorePlusButton;
    ImageButton homeTeamScoreMinusButton;
    ImageButton homeTeamScorePlusButton;
    ImageButton strikeButtonPlus;
    ImageButton strikeButtonMinus;
    ImageButton ballButtonPlus;
    ImageButton ballButtonMinus;
    ImageButton foulButtonPlus;
    ImageButton foulButtonMinus;
    ImageButton outButtonPlus;
    ImageButton outButtonMinus;
    ImageButton inningMinusButton;
    ImageButton inningPlusButton;
    ImageButton gameClockEditButton;
    ImageButton gameClockPlayButton;

    Button hitButton;
    Button okButton;
    Button shareScoreButton;
    Button undoButton;

    View ballCircleOne;
    View ballCircleTwo;
    View ballCircleThree;

    View strikeCircleOne;
    View strikeCircleTwo;

    View foulCircleOne;
    View foulCircleTwo;
    View foulCircleThree;

    View outCircleOne;
    View outCircleTwo;
    View outCircleThree;

    ImageView topInningImage;
    ImageView bottomInningImage;


    static TextView awayTeamNameTextView;
    static TextView homeTeamNameTextView;
    TextView awayTeamScoreView;
    TextView homeTeamScoreView;
    TextView inningTextView;
    TextView timerView;

    AdView mAdView;

    static int awayTeamScore;
    static int homeTeamScore;
    static int strikeCount;
    static int ballCount;
    static int foulCount;
    static int outCount;
    static int inning;
    static int topOrBot;

    private static boolean threeFoulOption;
    private static boolean autoMode;

    private static String homeTeamName;
    private static String awayTeamName;

    private static Stack<CurrentState> undoStack;
    private static CurrentState currentState;


    private LinearLayout inningLayout;
    private LinearLayout ballCountLayout;
    private LinearLayout strikeCountLayout;
    private LinearLayout foulCountLayout;
    private LinearLayout outCountLayout;
    private LinearLayout adsLayout;
    private LayoutInflater layoutInflater;
    ImageButton settingButton;
    private static boolean validClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = getLayoutInflater();
        //mainLayout = (LinearLayout) findViewById(R.id.countLayout);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
/*
        if (prefs.getBoolean(LEFTYMODE, false)) {
            //countLayout = layoutInflater.inflate(R.layout.lefty_count_layout, mainLayout, false);
        } else {
            //countLayout = layoutInflater.inflate(R.layout.righty_count_layout, mainLayout, false);
        }
        //  mainLayout.addView(countLayout);*/
        undoStack = new Stack<>();
        initializeCountFields();
        setupButtons();
    }


    @Override
    protected void onResume() {
        super.onResume();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        threeFoulOption = prefs.getBoolean(THREE_FOULS_OPTION, false);
        isAdsFreeModeEnabled = prefs.getBoolean(ADS_FREE_MODE, false);
        autoMode = prefs.getBoolean(AUTOMODE, true);
        isVibrationModeOn = prefs.getBoolean(VIBRATION_MODE, false);
        homeColor = prefs.getInt(HOME_COLOR, getResources().getColor(R.color.PrimaryBackgroundColor));
        awayColor = prefs.getInt(AWAY_COLOR, getResources().getColor(R.color.PrimaryBackgroundColor));
        ballColor = prefs.getInt(BALL_COLOR, getResources().getColor(R.color.countDefaultColor));
        strikeColor = prefs.getInt(STRIKE_COLOR, getResources().getColor(R.color.countDefaultColor));
        foulColor = prefs.getInt(FOUL_COLOR, getResources().getColor(R.color.countDefaultColor));
        outColor = prefs.getInt(OUT_COLOR, getResources().getColor(R.color.countDefaultColor));
        if (isAdsFreeModeEnabled) {
            mAdView.destroy();
            adsLayout.removeAllViews();
            adsLayout.setVisibility(View.GONE);
        }
        if (threeFoulOption)
            foulCircleThree.setVisibility(View.INVISIBLE);
        else
            foulCircleThree.setVisibility(View.VISIBLE);

        if (autoMode)
            outCircleThree.setVisibility(View.INVISIBLE);
        else
            outCircleThree.setVisibility(View.VISIBLE);

        fillRectangle(awayTeamNameTextView, awayColor);
        fillRectangle(homeTeamNameTextView, homeColor);


        // mainLayout = (LinearLayout) findViewById(R.id.countLayout);
     /*   if (prefs.getBoolean(LEFTYMODE, false)) {
            // countLayout = layoutInflater.inflate(R.layout.lefty_count_layout, mainLayout, false);
        } else {
            // countLayout = layoutInflater.inflate(R.layout.righty_count_layout, mainLayout, false);
        }
        //  mainLayout.addView(countLayout);*/
        updateFields();
        Log.d(LOG_TAG, "OnRsume is called");
    }

    protected void undoFunction() {
        currentState = undoStack.pop();
        awayTeamScore = currentState.getAwayTeamScore();
        homeTeamScore = currentState.getHomeTeamScore();
        strikeCount = currentState.getStrikeCount();
        ballCount = currentState.getBallCount();
        foulCount = currentState.getFoulCount();
        outCount = currentState.getOutCount();
        inning = currentState.getInning();
        topOrBot = currentState.getTopOrBot();
        updateFields();
    }

    protected static void initializeCountFields() {

        awayTeamScore = 0;
        homeTeamScore = 0;
        strikeCount = 0;
        ballCount = 0;
        foulCount = 0;
        outCount = 0;
        gameClockTime = 2700;
        inning = 1;
        topOrBot = 1;
        awayTeamName = DEFAULT_AWAY_NAME;
        homeTeamName = DEFAULT_HOME_NAME;
        vibrate = true;
        validClick = true;

        currentState = new CurrentState();
        currentState.setInning(1);
        currentState.setTopOrBot(1);
        undoStack.clear();

    }

    private void updateFields() {
        awayTeamNameTextView.setText(awayTeamName);
        homeTeamNameTextView.setText(homeTeamName);
        awayTeamScoreView.setText(String.valueOf(awayTeamScore));
        homeTeamScoreView.setText(String.valueOf(homeTeamScore));
        inningTextView.setText(String.valueOf(inning));

        if (topOrBot == 1) {
            topInningImage.setVisibility(View.VISIBLE);
            bottomInningImage.setVisibility(View.INVISIBLE);

        } else if (topOrBot == 2) {
            topInningImage.setVisibility(View.INVISIBLE);
            bottomInningImage.setVisibility(View.VISIBLE);


        }
        if (ballCount >= 1) {
            fillCircle(ballCircleOne, ballColor);
            fillCircle(ballCircleTwo, 0);
            fillCircle(ballCircleThree, 0);
            if (ballCount >= 2) {
                fillCircle(ballCircleTwo, ballColor);
                fillCircle(ballCircleThree, 0);
                if (ballCount >= 3)
                    fillCircle(ballCircleThree, ballColor);
            }
        } else {
            fillCircle(ballCircleOne, 0);
            fillCircle(ballCircleTwo, 0);
            fillCircle(ballCircleThree, 0);
        }

        if (foulCount >= 1) {
            fillCircle(foulCircleOne, foulColor);
            fillCircle(foulCircleTwo, 0);
            fillCircle(foulCircleThree, 0);
            if (foulCount >= 2) {
                fillCircle(foulCircleTwo, foulColor);
                fillCircle(foulCircleThree, 0);
                if (foulCount >= 3 && !threeFoulOption)
                    fillCircle(foulCircleThree, foulColor);
            }
        } else {
            fillCircle(foulCircleOne, 0);
            fillCircle(foulCircleTwo, 0);
            fillCircle(foulCircleThree, 0);
        }

        if (strikeCount >= 1) {
            fillCircle(strikeCircleOne, strikeColor);
            fillCircle(strikeCircleTwo, 0);
            if (strikeCount >= 2) {
                fillCircle(strikeCircleTwo, strikeColor);

            }
        } else {
            fillCircle(strikeCircleOne, 0);
            fillCircle(strikeCircleTwo, 0);

        }
        if (outCount >= 1) {
            fillCircle(outCircleOne, outColor);
            fillCircle(outCircleTwo, 0);
            fillCircle(outCircleThree, 0);
            if (outCount >= 2) {
                fillCircle(outCircleTwo, outColor);
                fillCircle(outCircleThree, 0);
                if (outCount == 3 && !autoMode) {
                    fillCircle(outCircleThree, outColor);
                }
            }
        } else {
            fillCircle(outCircleOne, 0);
            fillCircle(outCircleTwo, 0);
            fillCircle(outCircleThree, 0);
        }

    }

    private void autoMode() {
        if (ballCount == 4) {
            ballCount = 0;
            foulCount = 0;
            strikeCount = 0;
        }
        if (threeFoulOption) {
            if (foulCount == 3) {
                outCount++;
                ballCount = 0;
                foulCount = 0;
                strikeCount = 0;
            }
        } else {
            if (foulCount == 4) {
                outCount++;
                ballCount = 0;
                foulCount = 0;
                strikeCount = 0;
            }
        }
        if (strikeCount == 3) {
            outCount++;
            ballCount = 0;
            foulCount = 0;
            strikeCount = 0;
        }
        if (outCount == 3) {
            if (topOrBot == 1) {
                topOrBot = 2;
            } else if (topOrBot == 2) {
                if (inning < 9)
                    inning++;
                else
                    inning = 1;
                topOrBot = 1;
            }
            outCount = 0;
            ballCount = 0;
            foulCount = 0;
            strikeCount = 0;
        }

    }

    private void setupButtons() {

        awayTeamNameTextView = (TextView) findViewById(R.id.awayTeamNameTextView);
        homeTeamNameTextView = (TextView) findViewById(R.id.homeTeamNameTextView);
        awayTeamScoreView = (TextView) findViewById(R.id.awayTeamScoreView);
        homeTeamScoreView = (TextView) findViewById(R.id.homeTeamScoreView);
        inningTextView = (TextView) findViewById(R.id.inningCount);
        timerView = (TextView) findViewById(R.id.timerView);

        adsLayout = (LinearLayout) findViewById(R.id.ads_layout);
        mAdView = (AdView) findViewById(R.id.adView);

        awayTeamScoreMinusButton = (ImageButton) findViewById(R.id.awayTeamScoreMinusButton);
        awayTeamScorePlusButton = (ImageButton) findViewById(R.id.awayTeamScorePlusButton);
        homeTeamScoreMinusButton = (ImageButton) findViewById(R.id.homeTeamScoreMinusButton);
        homeTeamScorePlusButton = (ImageButton) findViewById(R.id.homeTeamScorePlusButton);
        strikeButtonPlus = (ImageButton) findViewById(R.id.strikeButtonPlus);
        strikeButtonMinus = (ImageButton) findViewById(R.id.strikeButtonMinus);
        ballButtonPlus = (ImageButton) findViewById(R.id.ballButtonPlus);
        ballButtonMinus = (ImageButton) findViewById(R.id.ballButtonMinus);
        foulButtonPlus = (ImageButton) findViewById(R.id.foulButtonPlus);
        foulButtonMinus = (ImageButton) findViewById(R.id.foulButtonMinus);
        outButtonPlus = (ImageButton) findViewById(R.id.outButtonPlus);
        outButtonMinus = (ImageButton) findViewById(R.id.outButtonMinus);
        topInningImage = (ImageView) findViewById(R.id.topInningImage);
        bottomInningImage = (ImageView) findViewById(R.id.bottomInningImage);
        inningMinusButton = (ImageButton) findViewById(R.id.inningMinusButton);
        inningPlusButton = (ImageButton) findViewById(R.id.inningPlusButton);
        hitButton = (Button) findViewById(R.id.hitButton);
        //  team1Layout = (LinearLayout) findViewById(R.id.team1Layout);
        //  team2Layout = (LinearLayout) findViewById(R.id.team2Layout);
        inningLayout = (LinearLayout) findViewById(R.id.inningLayout);
        ballCountLayout = (LinearLayout) findViewById(R.id.ballCountLayout);
        strikeCountLayout = (LinearLayout) findViewById(R.id.strikeCountLayout);
        foulCountLayout = (LinearLayout) findViewById(R.id.foulCountLayout);
        outCountLayout = (LinearLayout) findViewById(R.id.outCountLayout);

        ballCircleOne = findViewById(R.id.ballCircleOne);
        ballCircleTwo = findViewById(R.id.ballCircleTwo);
        ballCircleThree = findViewById(R.id.ballCircleThree);

        strikeCircleOne = findViewById(R.id.strikeCircleOne);
        strikeCircleTwo = findViewById(R.id.strikeCircleTwo);

        foulCircleOne = findViewById(R.id.foulCircleOne);
        foulCircleTwo = findViewById(R.id.foulCircleTwo);
        foulCircleThree = findViewById(R.id.foulCircleThree);

        outCircleOne = findViewById(R.id.outCircleOne);
        outCircleTwo = findViewById(R.id.outCircleTwo);
        outCircleThree = findViewById(R.id.outCircleThree);

        ballColor = getResources().getColor(R.color.countDefaultColor);
        strikeColor = getResources().getColor(R.color.countDefaultColor);
        foulColor = getResources().getColor(R.color.countDefaultColor);
        outColor = getResources().getColor(R.color.countDefaultColor);

        gameClockEditButton = (ImageButton) findViewById(R.id.gameClockEditButton);
        gameClockPlayButton = (ImageButton) findViewById(R.id.gameClockPlayButton);
        shareScoreButton = (Button) findViewById(R.id.shareScoreButton);
        undoButton = (Button) findViewById(R.id.undoButton);

        initializeGameClock(gameClockTime);

        date = new SimpleDateFormat("MM-dd-yy").format(new Date());


        settingButton = (ImageButton) findViewById(R.id.settingButton);
        threeFoulOption = prefs.getBoolean(THREE_FOULS_OPTION, false);
        autoMode = prefs.getBoolean(AUTOMODE, false);

        awayTeamNameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTeamName(v);

                Log.d(LOG_TAG, "Long Click for awayTeamNameTextView");
                return false;
            }
        });

        homeTeamNameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTeamName(v);

                Log.d(LOG_TAG, "Long Click for homeTeamNameTextView");
                return false;
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        awayTeamScoreMinusButton.setOnClickListener(this);
        awayTeamScorePlusButton.setOnClickListener(this);
        homeTeamScoreMinusButton.setOnClickListener(this);
        homeTeamScorePlusButton.setOnClickListener(this);
        inningLayout.setOnClickListener(this);
        awayTeamScoreView.setOnClickListener(this);
        homeTeamScoreView.setOnClickListener(this);
        strikeButtonPlus.setOnClickListener(this);
        strikeButtonMinus.setOnClickListener(this);
        ballButtonPlus.setOnClickListener(this);
        ballButtonMinus.setOnClickListener(this);
        foulButtonPlus.setOnClickListener(this);
        foulButtonMinus.setOnClickListener(this);
        outButtonPlus.setOnClickListener(this);
        outButtonMinus.setOnClickListener(this);
        inningMinusButton.setOnClickListener(this);
        inningPlusButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        ballCountLayout.setOnClickListener(this);
        strikeCountLayout.setOnClickListener(this);
        foulCountLayout.setOnClickListener(this);
        outCountLayout.setOnClickListener(this);
        hitButton.setOnClickListener(this);
        gameClockPlayButton.setOnClickListener(this);
        gameClockEditButton.setOnClickListener(this);
        undoButton.setOnClickListener(this);
        shareScoreButton.setOnClickListener(this);
    }

    private void editTeamName(final View v) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.team_name_promp, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        if (v.getId() == R.id.awayTeamNameTextView)
            userInput.setText(awayTeamNameTextView.getText());
        else
            userInput.setText(homeTeamNameTextView.getText());
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        userInput.setSelection(userInput.getText().length());
        okButton = (Button) promptsView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View okView) {
                if (isVibrationModeOn)
                    vibrator.vibrate(50);
                if (!userInput.getText().toString().equals("")) {
                    if (v.getId() == R.id.awayTeamNameTextView) {
                        awayTeamName = userInput.getText().toString();
                        awayTeamNameTextView.setText(awayTeamName);
                        alertDialog.dismiss();
                    } else if (v.getId() == R.id.homeTeamNameTextView) {
                        homeTeamName = userInput.getText().toString();
                        homeTeamNameTextView.setText(homeTeamName);
                        alertDialog.dismiss();
                    }
                }
            }
        });

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // show it
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.awayTeamScoreMinusButton: {
                if (topOrBot == 1) {
                    if (awayTeamScore > 0) {
                        awayTeamScore--;
                    }
                } else {
                    vibrate = false;
                    validClick = false;
                }
                break;
            }
            case R.id.awayTeamScorePlusButton:
            case R.id.awayTeamScoreView: {
                if (topOrBot == 1) {
                    if (awayTeamScore < 99)
                        awayTeamScore++;
                    else
                        awayTeamScore = 0;

                } else {
                    vibrate = false;
                    validClick = false;
                }
                break;
            }
            case R.id.homeTeamScoreMinusButton: {
                if (topOrBot == 2) {
                    if (homeTeamScore > 0) {
                        homeTeamScore--;
                    }
                } else {
                    vibrate = false;
                    validClick = false;
                }
                break;
            }
            case R.id.homeTeamScorePlusButton:
            case R.id.homeTeamScoreView: {
                if (topOrBot == 2) {
                    if (homeTeamScore < 99)
                        homeTeamScore++;
                    else
                        homeTeamScore = 0;

                } else {
                    vibrate = false;
                    validClick = false;
                }
                break;
            }
            case R.id.ballButtonMinus: {
                if (ballCount > 0)
                    ballCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.ballCountLayout:
            case R.id.ballButtonPlus: {
                if (ballCount < 4)
                    ballCount++;
                if (autoMode)
                    autoMode();
                else if (ballCount == 4)
                    ballCount = 0;
                break;
            }
            case R.id.strikeButtonMinus: {
                if (strikeCount > 0)
                    strikeCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.strikeCountLayout:
            case R.id.strikeButtonPlus: {
                if (strikeCount < 3)
                    strikeCount++;
                if (autoMode)
                    autoMode();
                else if (strikeCount == 3)
                    strikeCount = 0;
                break;
            }
            case R.id.foulButtonMinus: {
                if (foulCount > 0)
                    foulCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.foulCountLayout:
            case R.id.foulButtonPlus: {
                if (threeFoulOption) {
                    if (foulCount < 3)
                        foulCount++;
                } else {
                    if (foulCount < 4)
                        foulCount++;
                }
                if (autoMode)
                    autoMode();
                else if (foulCount == 3 && threeFoulOption)
                    foulCount = 0;
                else if (foulCount == 4)
                    foulCount = 0;
                break;
            }
            case R.id.outButtonMinus: {
                if (outCount > 0)
                    outCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.outCountLayout:
            case R.id.outButtonPlus: {
                if (outCount <= 3)
                    outCount++;
                if (autoMode) {
                    autoMode();
                    ballCount = 0;
                    foulCount = 0;
                    strikeCount = 0;
                } else if (outCount > 3)
                    outCount = 0;
                break;
            }
            case R.id.inningMinusButton: {
                if (inning >= 1) {
                    if (topOrBot == 1 && inning != 1) {
                        inning--;
                        topOrBot = 2;
                    } else if (topOrBot == 2)
                        topOrBot = 1;
                }
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.inningLayout:
            case R.id.inningPlusButton: {
                if (topOrBot == 1) {
                    topOrBot = 2;
                } else if (topOrBot == 2 && inning < 9) {
                    inning++;
                    topOrBot = 1;
                } else {
                    inning = 1;
                    topOrBot = 1;
                }

                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.settingButton: {
                startActivity(new Intent(v.getContext(), Settings.class));
                break;
            }
            case R.id.hitButton: {
                ballCount = 0;
                foulCount = 0;
                strikeCount = 0;
                break;
            }

            case R.id.gameClockEditButton: {
                editGameClock();
                break;
            }
            case R.id.gameClockPlayButton: {
                isGameClockRunning = !isGameClockRunning;
                startStopGameClick();
                break;
            }
            case R.id.undoButton: {
                if (!undoStack.isEmpty()) {
                    undoFunction();
                    undo = true;
                }
                break;
            }
            case R.id.shareScoreButton: {
                shareScore();
                break;
            }
            default:
        }
        if (isVibrationModeOn && vibrate)
            vibrator.vibrate(50);
        vibrate = true;

        if (!undo && validClick) {
            undoStack.push(currentState);
            currentState = new CurrentState();
            currentState.setAwayTeamScore(awayTeamScore);
            currentState.setHomeTeamScore(homeTeamScore);
            currentState.setStrikeCount(strikeCount);
            currentState.setBallCount(ballCount);
            currentState.setFoulCount(foulCount);
            currentState.setOutCount(outCount);
            currentState.setInning(inning);
            currentState.setTopOrBot(topOrBot);

            updateFields();
        } else {
            undo = false;
            validClick = true;
        }
    }

    private void fillCircle(View v, int color) {

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setShape(GradientDrawable.OVAL);
        shape.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shape.setStroke(9, getResources().getColor(R.color.PrimaryAccentColor));
        v.setBackground(shape);

    }

    private void fillRectangle(View v, int color) {

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setStroke(9, getResources().getColor(R.color.PrimaryAccentColor));
        shape.setCornerRadius(1);
        v.setBackground(shape);
    }

    private void gameClock(int length) {
        timer = new GameTimer(length * 1000, 1000, timerView);
    }

    private void startStopGameClick() {

        if (isGameClockRunning) {
            gameClockPlayButton.setImageResource(R.drawable.pause_button);
            timer.start();
        } else {
            gameClockPlayButton.setImageResource(R.drawable.play_button);
            timer.cancel();
            milliSecondsToFinish = (int) timer.UntilFinished / 1000;

            if (!newTime)
                gameClockTime = milliSecondsToFinish;
            else
                newTime = false;

            timer = new GameTimer(gameClockTime * 1000, 1000, timerView);
        }

    }

    private void initializeGameClock(int gameClockTime) {
        isGameClockRunning = false;
        newTime = false;
        gameClock(gameClockTime);
        timer.start();
        timer.cancel();
        milliSecondsToFinish = (int) timer.UntilFinished / 1000;
    }

    private void editGameClock() {

        isGameClockRunning = false;

        startStopGameClick();

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.edit_game_clock_promp, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.gameClockEditTextView);
        final TextView logoTextView = (TextView) promptsView
                .findViewById(R.id.logoTextView);


        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        userInput.setSelection(userInput.getText().length());
        okButton = (Button) promptsView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View okView) {
                if (isVibrationModeOn)
                    vibrator.vibrate(50);
                if (!userInput.getText().toString().equals("")) {
                    if (Integer.parseInt(userInput.getText().toString()) > 0 && Integer.parseInt(userInput.getText().toString()) <= 180 && userInput.getText().toString().matches("[0-9]+")) {
                        gameClockTime = Integer.parseInt(userInput.getText().toString()) * 60;
                        newTime = true;
                        startStopGameClick();
                        alertDialog.dismiss();
                    } else {
                        logoTextView.setText(R.string.errorWarningEditGameClock);
                        logoTextView.setTextColor(getResources().getColor(R.color.PrimaryHighlightColor));
                    }
                }
            }
        });

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // show it
        alertDialog.show();
    }

    private void shareScore() {
        Intent shareScoreIntent = new Intent(Intent.ACTION_SEND);
        shareScoreIntent.setType("text/plain");
        shareScoreIntent.putExtra(Intent.EXTRA_SUBJECT, "Final Score between " + awayTeamName + " vs. " + homeTeamName + " on " + date);
        shareScoreIntent.putExtra(Intent.EXTRA_TEXT, "Final Score between " + awayTeamName + " vs. " + homeTeamName + " on " + date + "\n" + awayTeamName +
                " - " + awayTeamScore + "\n" + homeTeamName + " - " + homeTeamScore);
        startActivity(Intent.createChooser(shareScoreIntent, "Share Game Score:"));
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

}