package com.pyrotemplar.kickballreferee;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.content.SharedPreferences.*;


/**
 * Created by Pyrotemplar on 6/27/2015.
 * settings Activity
 */
public class Settings extends Activity {

    private static final String AUTOMODE = "AutoMode";
    private static final String THREE_FOULS_OPTION = "ThreefoulOption";
    private static final String EMAIL = "pyrotemplardev@gmail.com";
    private static final String LEFTYMODE = "LeftyMode";


    CheckBox autoModeBox;
    CheckBox leftyModeBox;
    TextView resetButton;
    TextView autoModeText;
    TextView leftyModeText;

    RadioButton threeFouldRadioButton;
    RadioButton fourFouldRadioButton;
    TextView feedbackButton;
    AdView mAdView;

    SharedPreferences sp;
    Editor edit;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.settings);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();


        autoModeText = (TextView) findViewById(R.id.autoModeText);
        leftyModeText = (TextView) findViewById(R.id.leftyModeText);
        autoModeBox = (CheckBox) findViewById(R.id.autoModeCheckbox);
        resetButton = (TextView) findViewById(R.id.resetCountText);
        leftyModeBox = (CheckBox) findViewById(R.id.leftyModeCheckBox);
        threeFouldRadioButton = (RadioButton) findViewById(R.id.fouls3Radio);
        fourFouldRadioButton = (RadioButton) findViewById(R.id.fouls4Radio);
        feedbackButton = (TextView) findViewById(R.id.feedbackButton);
        mAdView = (AdView) findViewById(R.id.adView);


        autoModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoModeBox.isChecked())
                    autoModeBox.setChecked(false);
                else
                    autoModeBox.setChecked(true);
            }
        });
        leftyModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftyModeBox.isChecked())
                    leftyModeBox.setChecked(false);
                else
                    leftyModeBox.setChecked(true);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.initializeCountFields();
                Toast.makeText(v.getContext(), "Reset", Toast.LENGTH_SHORT).show();
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                feedbackIntent.setType("text/email");
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Kickball Referee");
                feedbackIntent.putExtra(Intent.EXTRA_TEXT, "Dear Pyrotemplar," + "\n\n");
                startActivity(Intent.createChooser(feedbackIntent, "Send Feedback:"));
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadSettings();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    public void onPause() {
        saveSettings();
        super.onPause();
    }

    private void saveSettings() {


        edit.putBoolean(AUTOMODE, autoModeBox.isChecked());
        edit.putBoolean(LEFTYMODE, leftyModeBox.isChecked());
        if (threeFouldRadioButton.isChecked())
            edit.putBoolean(THREE_FOULS_OPTION, true);
        else
            edit.putBoolean(THREE_FOULS_OPTION, false);

        edit.commit();


    }

    private void loadSettings() {

        autoModeBox.setChecked(sp.getBoolean(AUTOMODE, false));
        leftyModeBox.setChecked(sp.getBoolean(LEFTYMODE, false));

        if (sp.getBoolean(THREE_FOULS_OPTION, false))
            threeFouldRadioButton.setChecked(true);

        else
            fourFouldRadioButton.setChecked(true);


    }


}