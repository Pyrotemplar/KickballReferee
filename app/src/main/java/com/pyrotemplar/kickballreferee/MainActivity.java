package com.pyrotemplar.kickballreferee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    private GameTimer timer;
    private boolean isGameClockRunning;
    private boolean newTime;
    private boolean isVibrationModeOn;
    private boolean vibrate;
    private boolean isAdsFreeModeEnabled;
    private boolean undo;
    private long back_pressed;
    private int milliSecondsToFinish;
    private SharedPreferences prefs = null;
    private static int gameClockTime;
    private Vibrator vibrator;

    private String date;

    private int ballColor;
    private int strikeColor;
    private int foulColor;
    private int outColor;

    ImageButton team1ScoreMinusButton;
    ImageButton team1ScorePlusButton;
    ImageButton team2ScoreMinusButton;
    ImageButton team2ScorePlusButton;
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


    static TextView team1NameTextView;
    static TextView team2NameTextView;
    TextView team1ScoreView;
    TextView team2ScoreView;
    TextView inningTextView;
    TextView timerView;

    AdView mAdView;

    static int team1Score;
    static int team2Score;
    static int strikeCount;
    static int ballCount;
    static int foulCount;
    static int outCount;
    static int inning;
    static int topOrBot;

    private static boolean threeFoulOption;
    private static boolean autoMode;

    private static String team1Name;
    private static String team2Name;

    private static Stack<CurrentState> undoStack;
    private static CurrentState currentState;

    /*    private View countLayout;
        private LinearLayout mainLayout;
        private LinearLayout team1Layout;
        private LinearLayout team2Layout;*/
    private LinearLayout inningLayout;
    private LinearLayout ballCountLayout;
    private LinearLayout strikeCountLayout;
    private LinearLayout foulCountLayout;
    private LinearLayout outCountLayout;
    private LinearLayout adsLayout;
    private LayoutInflater layoutInflater;
    ImageButton settingButton;

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
        vibrate = true;
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
        team1Score = currentState.getTeam1Score();
        team2Score = currentState.getTeam2Score();
        strikeCount = currentState.getStrikeCount();
        ballCount = currentState.getBallCount();
        foulCount = currentState.getFoulCount();
        outCount = currentState.getOutCount();
        inning = currentState.getInning();
        topOrBot = currentState.getTopOrBot();
        updateFields();
    }

    protected static void initializeCountFields() {


        team1Score = 0;
        team2Score = 0;
        strikeCount = 0;
        ballCount = 0;
        foulCount = 0;
        outCount = 0;
        gameClockTime = 2700;
        inning = 1;
        topOrBot = 1;
        team1Name = "Team A";
        team2Name = "Team B";

        currentState = new CurrentState();
        currentState.setInning(1);
        currentState.setTopOrBot(1);
        undoStack.clear();

    }

    private void updateFields() {
        team1NameTextView.setText(team1Name);
        team2NameTextView.setText(team2Name);
        team1ScoreView.setText(String.valueOf(team1Score));
        team2ScoreView.setText(String.valueOf(team2Score));
        inningTextView.setText(String.valueOf(inning));

        if (topOrBot == 1) {
            topInningImage.setVisibility(View.VISIBLE);
            bottomInningImage.setVisibility(View.INVISIBLE);
            team1ScoreView.setTextColor(Color.RED);
            team2ScoreView.setTextColor(Color.WHITE);

        } else if (topOrBot == 2) {
            topInningImage.setVisibility(View.INVISIBLE);
            bottomInningImage.setVisibility(View.VISIBLE);
            team1ScoreView.setTextColor(Color.WHITE);
            team2ScoreView.setTextColor(Color.RED);
        }
        if (ballCount >= 1) {
            fillCountCircle(ballCircleOne, ballColor);
            fillCountCircle(ballCircleTwo, 0);
            fillCountCircle(ballCircleThree, 0);
            if (ballCount >= 2) {
                fillCountCircle(ballCircleTwo, ballColor);
                fillCountCircle(ballCircleThree, 0);
                if (ballCount >= 3)
                    fillCountCircle(ballCircleThree, ballColor);
            }
        } else {
            fillCountCircle(ballCircleOne, 0);
            fillCountCircle(ballCircleTwo, 0);
            fillCountCircle(ballCircleThree, 0);
        }

        if (foulCount >= 1) {
            fillCountCircle(foulCircleOne, foulColor);
            fillCountCircle(foulCircleTwo, 0);
            fillCountCircle(foulCircleThree, 0);
            if (foulCount >= 2) {
                fillCountCircle(foulCircleTwo, foulColor);
                fillCountCircle(foulCircleThree, 0);
                if (foulCount >= 3 && !threeFoulOption)
                    fillCountCircle(foulCircleThree, foulColor);
            }
        } else {
            fillCountCircle(foulCircleOne, 0);
            fillCountCircle(foulCircleTwo, 0);
            fillCountCircle(foulCircleThree, 0);
        }

        if (strikeCount >= 1) {
            fillCountCircle(strikeCircleOne, strikeColor);
            fillCountCircle(strikeCircleTwo, 0);
            if (strikeCount >= 2) {
                fillCountCircle(strikeCircleTwo, strikeColor);


            }
        } else {
            fillCountCircle(strikeCircleOne, 0);
            fillCountCircle(strikeCircleTwo, 0);

        }
        if (outCount >= 1) {
            fillCountCircle(outCircleOne, outColor);
            fillCountCircle(outCircleTwo, 0);
            fillCountCircle(outCircleThree, 0);
            if (outCount >= 2) {
                fillCountCircle(outCircleTwo, outColor);
                fillCountCircle(outCircleThree, 0);
                if (outCount == 3 && !autoMode) {
                    fillCountCircle(outCircleThree, outColor);
                }
            }
        } else {
            fillCountCircle(outCircleOne, 0);
            fillCountCircle(outCircleTwo, 0);
            fillCountCircle(outCircleThree, 0);
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

        team1NameTextView = (TextView) findViewById(R.id.team1Name);
        team2NameTextView = (TextView) findViewById(R.id.team2Name);
        team1ScoreView = (TextView) findViewById(R.id.team1ScoreView);
        team2ScoreView = (TextView) findViewById(R.id.team2ScoreView);
        inningTextView = (TextView) findViewById(R.id.inningCount);
        timerView = (TextView) findViewById(R.id.timerView);

        adsLayout = (LinearLayout) findViewById(R.id.ads_layout);
        mAdView = (AdView) findViewById(R.id.adView);

        team1ScoreMinusButton = (ImageButton) findViewById(R.id.team1ScoreMinus);
        team1ScorePlusButton = (ImageButton) findViewById(R.id.team1ScorePlus);
        team2ScoreMinusButton = (ImageButton) findViewById(R.id.team2ScoreMinus);
        team2ScorePlusButton = (ImageButton) findViewById(R.id.team2ScorePlus);
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

        ballColor = getResources().getColor(R.color.Yellow);
        strikeColor = getResources().getColor(R.color.Green);
        foulColor = getResources().getColor(R.color.LightBlue);
        outColor = getResources().getColor(R.color.Red);

        gameClockEditButton = (ImageButton) findViewById(R.id.gameClockEditButton);
        gameClockPlayButton = (ImageButton) findViewById(R.id.gameClockPlayButton);
        shareScoreButton = (Button) findViewById(R.id.shareScoreButton);
        undoButton = (Button) findViewById(R.id.undoButton);

        initializeGameClock(gameClockTime);

        date = new SimpleDateFormat("MM-dd-yy").format(new Date());


        settingButton = (ImageButton) findViewById(R.id.settingButton);
        threeFoulOption = prefs.getBoolean(THREE_FOULS_OPTION, false);
        autoMode = prefs.getBoolean(AUTOMODE, false);

        team1NameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTeamName(v);

                Log.d(LOG_TAG, "Long Click for Team1NameView");
                return false;
            }
        });

        team2NameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTeamName(v);

                Log.d(LOG_TAG, "Long Click for Team2NameView");
                return false;
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        team1ScoreMinusButton.setOnClickListener(this);
        team1ScorePlusButton.setOnClickListener(this);
        team2ScoreMinusButton.setOnClickListener(this);
        team2ScorePlusButton.setOnClickListener(this);
        inningLayout.setOnClickListener(this);
        team1ScoreView.setOnClickListener(this);
        team2ScoreView.setOnClickListener(this);
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

        if (v.getId() == R.id.team1Name)
            userInput.setText(team1NameTextView.getText());
        else
            userInput.setText(team2NameTextView.getText());
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
                    if (v.getId() == R.id.team1Name) {
                        team1Name = userInput.getText().toString();
                        team1NameTextView.setText(team1Name);
                        alertDialog.dismiss();
                    } else if (v.getId() == R.id.team2Name) {
                        team2Name = userInput.getText().toString();
                        team2NameTextView.setText(team2Name);
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

            case R.id.team1ScoreMinus: {
                if (topOrBot == 1) {
                    if (team1Score > 0) {
                        team1Score--;
                    }
                }else
                vibrate = false;
                break;
            }
            case R.id.team1ScoreView:
            case R.id.team1ScorePlus: {
                if (topOrBot == 1) {
                    if (team1Score < 99)
                        team1Score++;
                    else
                        team1Score = 0;

                }else
                vibrate = false;
                break;
            }
            case R.id.team2ScoreMinus: {
                if (topOrBot == 2) {
                    if (team2Score > 0) {
                        team2Score--;
                    }
                }else
                vibrate = false;
                break;
            }
            case R.id.team2ScoreView:
            case R.id.team2ScorePlus: {
                if (topOrBot == 2) {
                    if (team2Score < 99)
                        team2Score++;
                    else
                        team2Score = 0;

                } else
                vibrate = false;
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
                if (outCount < 3)
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

        if (!undo) {
            undoStack.push(currentState);
            currentState = new CurrentState();
            currentState.setTeam1Score(team1Score);
            currentState.setTeam2Score(team2Score);
            currentState.setStrikeCount(strikeCount);
            currentState.setBallCount(ballCount);
            currentState.setFoulCount(foulCount);
            currentState.setOutCount(outCount);
            currentState.setInning(inning);
            currentState.setTopOrBot(topOrBot);

            updateFields();
        } else
            undo = false;
    }

    private void fillCountCircle(View v, int color) {

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setShape(GradientDrawable.OVAL);
        shape.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shape.setStroke(9, getResources().getColor(R.color.PrimaryAccentColor));
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
                        logoTextView.setTextColor(getResources().getColor(R.color.Red));
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
        shareScoreIntent.putExtra(Intent.EXTRA_SUBJECT, "Final Score between " + team1Name + " vs. " + team2Name + " on " + date);
        shareScoreIntent.putExtra(Intent.EXTRA_TEXT, "Final Score between " + team1Name + " vs. " + team2Name + " on " + date + "\n" + team1Name + " - " + team1Score + "\n" + team2Name + " - " + team2Score);
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