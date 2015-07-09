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
 */
public class Settings extends Activity {

    private static final String AUTOMODE = "AutoMode";
    private static final String THREE_FOULS_OPTION = "ThreefoulOption";
    private static final String EMAIL = "pyrotemplardev@gmail.com";



    CheckBox autoModeBox;
    TextView resetButton;
    RadioButton threeFouldRadioButton;
    RadioButton fourFouldRadioButton;
    TextView feedbackButton;
    AdView mAdView;

    SharedPreferences sp;
    Editor edit;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.settings);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();



        autoModeBox = (CheckBox) findViewById(R.id.autoModecheckbox);
        resetButton = (TextView) findViewById(R.id.resetCountText);
        threeFouldRadioButton = (RadioButton) findViewById(R.id.fouls3Radio);
        fourFouldRadioButton = (RadioButton) findViewById(R.id.fouls4Radio);
        feedbackButton = (TextView) findViewById(R.id.feedbackButton);
        mAdView = (AdView) findViewById(R.id.adView);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.initializeCountFields();
                Toast.makeText(v.getContext(),"Reset", Toast.LENGTH_SHORT).show();
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                feedbackIntent.setType("text/email");
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT,  "Feedback for Kickball Referee");
                feedbackIntent.putExtra(Intent.EXTRA_TEXT,"Dear Pyrotemplar," + "\n\n");
                startActivity( Intent.createChooser(feedbackIntent, "Send Feedback:"));
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadSettings();

    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onRestart(){
        super.onRestart();

    }

    @Override
    public void onPause(){
        saveSettings();
        super.onPause();
    }
    private void saveSettings(){


        sp.edit().putBoolean(AUTOMODE, autoModeBox.isChecked()).commit();
        if(threeFouldRadioButton.isChecked())
            edit.putBoolean(THREE_FOULS_OPTION, true);
        else
            edit.putBoolean(THREE_FOULS_OPTION, false);

        edit.commit();



    }
    private void loadSettings(){

        autoModeBox.setChecked(sp.getBoolean(AUTOMODE, false));

        if(sp.getBoolean(THREE_FOULS_OPTION, false))
            threeFouldRadioButton.setChecked(true);

        else
            fourFouldRadioButton.setChecked(true);


    }


}