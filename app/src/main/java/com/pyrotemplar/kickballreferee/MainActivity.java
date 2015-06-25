package com.pyrotemplar.kickballreferee;

import android.app.Activity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

    Button team1ScoreMinusButton;
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


    EditText team1NameTextView;
    EditText team2NameTextView;
    TextView team1ScoreTextView;
    TextView team2ScoreTextView;
    TextView ballCountTextView;
    TextView strikeCountTextView;
    TextView foulCountTextView;
    TextView outCountTextView;
    TextView inningTextView;

    int team1Score;
    int team2Score;
    int strikeCount;
    int ballCount;
    int foulCount;
    int outCount;
    int inning;
    int topOrBot;


    ImageButton settingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        team1NameTextView = (EditText) findViewById(R.id.team1Name);
        team2NameTextView = (EditText) findViewById(R.id.team2Name);
        team1ScoreTextView = (TextView) findViewById(R.id.team1Score);
        team2ScoreTextView = (TextView) findViewById(R.id.team2Score);
        ballCountTextView = (TextView) findViewById(R.id.ballText);
        strikeCountTextView = (TextView) findViewById(R.id.strikeText);
        foulCountTextView = (TextView) findViewById(R.id.foulText);
        outCountTextView = (TextView) findViewById(R.id.outText);
        inningTextView = (TextView) findViewById(R.id.inningText);

        team1ScoreMinusButton = (Button) findViewById(R.id.team1ScoreMinus);
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


        team1NameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(LOG_TAG, "Long Click for Team1NameView");
                return false;
            }
        });

        team2NameTextView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(LOG_TAG, "Long Click for Team2NameView");
                return false;
            }
        });

        team1ScoreMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team1Score > 0)
                    team1Score--;
                team1ScoreTextView.setText(String.valueOf(team1Score));
            }
        });
        team1ScorePlusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                team1Score++;
                team1ScoreTextView.setText(String.valueOf(team1Score));
            }
        });
        team2ScoreMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team2Score > 0)
                    team2Score--;
                team2ScoreTextView.setText(String.valueOf(team2Score));

            }
        });
        team2ScorePlusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                team2Score++;
                team2ScoreTextView.setText(String.valueOf(team2Score));

            }
        });

        strikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strikeCount < 3)
                    strikeCount++;

                strikeCountTextView.setText(String.valueOf(strikeCount));
            }
        });
        strikeMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strikeCount > 0)
                    strikeCount--;

                strikeCountTextView.setText(String.valueOf(strikeCount));
            }
        });
        ballButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount < 4)
                    ballCount++;

                ballCountTextView.setText(String.valueOf(ballCount));

            }
        });
        ballMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballCount > 0)
                    ballCount--;

                ballCountTextView.setText(String.valueOf(ballCount));

            }
        });
        foulButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foulCount < 4)
                    foulCount++;

                foulCountTextView.setText(String.valueOf(foulCount));


            }
        });
        foulMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foulCount > 0)
                    foulCount--;

                foulCountTextView.setText(String.valueOf(foulCount));
            }
        });
        outButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outCount < 3)
                    outCount++;

                outCountTextView.setText(String.valueOf(outCount));
            }
        });
        outMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outCount > 0)
                    outCount--;

                outCountTextView.setText(String.valueOf(outCount));
            }
        });
        inningMinusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inning >= 1 ) {
                    if (topOrBot == 1 && inning != 1) {
                        inning--;
                        topOrBot = 2;
                    } else if (topOrBot == 2)
                        topOrBot = 1;

                    if (topOrBot == 1)
                        inningTextView.setText("TOP " + String.valueOf(inning));
                    else if (topOrBot == 2)
                        inningTextView.setText("BOTTOM " + String.valueOf(inning));
                }
            }
        });
        inningPlugButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topOrBot == 1) {
                    topOrBot = 2;
                } else if (topOrBot == 2) {
                    inning++;
                    topOrBot = 1;
                }
                if (topOrBot == 1)
                    inningTextView.setText("TOP " + String.valueOf(inning));
                else if (topOrBot == 2)
                    inningTextView.setText("BOTTOM " + String.valueOf(inning));
            }
        });


        settingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Settings Button Pressed");
            }
        });

    }


    private void initializeCountFields() {
        team1Score = 0;
        team2Score = 0;
        strikeCount = 0;
        ballCount = 0;
        foulCount = 0;
        outCount = 0;
        inning = 1;
        topOrBot = 1;

    }


}
