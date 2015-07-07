package com.pyrotemplar.kickballreferee;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity implements OnClickListener {
    private static final String LOG_TAG = "MainActivity";
    private static final String THREE_FOULS_OPTION = "ThreefoulOption";
    private static final String AUTOMODE = "AutoMode";

    private SharedPreferences prefs = null;

    ImageButton team1ScoreMinusButton;
    Button team1ScorePlusButton;
    Button team2ScoreMinusButton;
    Button team2ScorePlusButton;
    Button strikeButton;
    Button strikeMinusButton;
    Button ballButton;
    Button ballMinusButton;
    Button foulButton;
    Button foulMinusButton;
    Button outButton;
    Button outMinusButton;
    Button inningMinusButton;
    Button inningPlugButton;


    static TextView team1NameTextView;
    static TextView team2NameTextView;
    TextView team1ScoreTextView;
    TextView team2ScoreTextView;
    TextView ballCountTextView;
    TextView strikeCountTextView;
    TextView foulCountTextView;
    TextView outCountTextView;
    TextView inningTextView;

    AdView mAdView;

    static String teamNameString = null;

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


    ImageButton settingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        team1NameTextView = (TextView) findViewById(R.id.team1Name);
        team2NameTextView = (TextView) findViewById(R.id.team2Name);
        team1ScoreTextView = (TextView) findViewById(R.id.team1Score);
        team2ScoreTextView = (TextView) findViewById(R.id.team2Score);
        ballCountTextView = (TextView) findViewById(R.id.ballText);
        strikeCountTextView = (TextView) findViewById(R.id.strikeText);
        foulCountTextView = (TextView) findViewById(R.id.foulText);
        outCountTextView = (TextView) findViewById(R.id.outText);
        inningTextView = (TextView) findViewById(R.id.inningText);

        mAdView = (AdView) findViewById(R.id.adView);

        team1ScoreMinusButton = (ImageButton) findViewById(R.id.team1ScoreMinus);
        team1ScorePlusButton = (Button) findViewById(R.id.team1ScorePlus);
        team2ScoreMinusButton = (Button) findViewById(R.id.team2ScoreMinus);
        team2ScorePlusButton = (Button) findViewById(R.id.team2ScorePlus);
        strikeButton = (Button) findViewById(R.id.strikeButton);
        strikeMinusButton = (Button) findViewById(R.id.strikeButtonMinus);
        ballButton = (Button) findViewById(R.id.ballButton);
        ballMinusButton = (Button) findViewById(R.id.ballButtonMinus);
        foulButton = (Button) findViewById(R.id.foulButton);
        foulMinusButton = (Button) findViewById(R.id.foulButtonMinus);
        outButton = (Button) findViewById(R.id.outButton);
        outMinusButton = (Button) findViewById(R.id.outButtonMinus);
        inningMinusButton = (Button) findViewById(R.id.inningMinusButton);
        inningPlugButton = (Button) findViewById(R.id.inningPlusButton);

        initializeCountFields();

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

        team1ScoreMinusButton.setOnClickListener(this);
        team1ScorePlusButton.setOnClickListener(this);
        team2ScoreMinusButton.setOnClickListener(this);
        team2ScorePlusButton.setOnClickListener(this);
        strikeButton.setOnClickListener(this);
        strikeMinusButton.setOnClickListener(this);
        ballButton.setOnClickListener(this);
        ballMinusButton.setOnClickListener(this);
        foulButton.setOnClickListener(this);
        foulMinusButton.setOnClickListener(this);
        outButton.setOnClickListener(this);
        outMinusButton.setOnClickListener(this);
        inningMinusButton.setOnClickListener(this);
        inningPlugButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
        Log.d(LOG_TAG, "OnRsume is called");
    }

    protected static void initializeCountFields() {
        team1Score = 0;
        team2Score = 0;
        strikeCount = 0;
        ballCount = 0;
        foulCount = 0;
        outCount = 0;
        inning = 1;
        topOrBot = 1;
        team1NameTextView.setText("Team A");
        team2NameTextView.setText("Team B");


    }

    private void updateFields() {
        threeFoulOption = prefs.getBoolean(THREE_FOULS_OPTION, false);
        autoMode = prefs.getBoolean(AUTOMODE, false);
        team1ScoreTextView.setText(String.valueOf(team1Score));
        team2ScoreTextView.setText(String.valueOf(team2Score));
        ballCountTextView.setText(String.valueOf(ballCount));
        strikeCountTextView.setText(String.valueOf(strikeCount));
        foulCountTextView.setText(String.valueOf(foulCount));
        outCountTextView.setText(String.valueOf(outCount));
        if (topOrBot == 1)
            inningTextView.setText("TOP " + String.valueOf(inning));
        else if (topOrBot == 2)
            inningTextView.setText("BOTTOM " + String.valueOf(inning));
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
                inning++;
                topOrBot = 1;
            }
            outCount = 0;
            ballCount = 0;
            foulCount = 0;
            strikeCount = 0;
        }

    }

    public SharedPreferences getPrefs() {
        return (prefs);
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

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                if (v.getId() == R.id.team1Name)
                                    team1NameTextView.setText(userInput.getText().toString());
                                else if (v.getId() == R.id.team2Name)
                                    team2NameTextView.setText(userInput.getText().toString());
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.team1ScoreMinus: {
                if (team1Score > 0)
                    team1Score--;
                break;
            }
            case R.id.team1ScorePlus: {
                team1Score++;
                break;
            }
            case R.id.team2ScoreMinus: {
                if (team2Score > 0)
                    team2Score--;
                break;
            }
            case R.id.team2ScorePlus: {
                team2Score++;
                break;
            }
            case R.id.ballButtonMinus: {
                if (ballCount > 0)
                    ballCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.ballButton: {
                if (ballCount < 4)
                    ballCount++;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.strikeButtonMinus: {
                if (strikeCount > 0)
                    strikeCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.strikeButton: {
                if (strikeCount < 3)
                    strikeCount++;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.foulButtonMinus: {
                if (foulCount > 0)
                    foulCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.foulButton: {
                if (threeFoulOption) {
                    if (foulCount < 3)
                        foulCount++;
                } else {
                    if (foulCount < 4)
                        foulCount++;
                }
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.outButtonMinus: {
                if (outCount > 0)
                    outCount--;
                if (autoMode)
                    autoMode();
                break;
            }
            case R.id.outButton: {
                if (outCount < 3)
                    outCount++;
                if (autoMode)
                    autoMode();
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
            case R.id.inningPlusButton: {


                if (topOrBot == 1) {
                    topOrBot = 2;
                } else if (topOrBot == 2) {
                    inning++;
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
            default:
        }

        updateFields();
    }
}
