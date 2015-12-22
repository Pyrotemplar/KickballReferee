package com.pyrotemplar.kickballreferee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pyrotemplar.inappbilling.util.IabHelper;
import com.pyrotemplar.inappbilling.util.IabResult;
import com.pyrotemplar.inappbilling.util.Purchase;

import static android.content.SharedPreferences.*;


/**
 * Created by Pyrotemplar on 6/27/2015.
 * settings Activity
 */
public class Settings extends Activity {

    private static final String LOG_TAG = "settingsActivity";
    private static final String AUTOMODE = "AutoMode";
    private static final String THREE_FOULS_OPTION = "ThreefoulOption";
    private static final String EMAIL = "pyrotemplardev@gmail.com";
    private static final String VIBRATIONMODE = "VibrationMode";
    private static final String ADS_FREE_MODE = "adsFreeMode";
    private static final String ITEM_SKU = "com.pyrotemplar.kickballreferee.adfreemode";

    boolean isAdsFreeModeEnabled;

    CheckBox autoModeBox;
    CheckBox vibrationModeBox;
    Button resetButton;
    Button feedbackButton;
    Button removeAdsButton;
    Button yesButton;
    Button noButton;

    LinearLayout autoModeLayout;
    LinearLayout vibrationModeLayout;
    RelativeLayout adsLayout;

    RadioButton threeFouldRadioButton;
    RadioButton fourFouldRadioButton;
    IabHelper mHelper;
    AdView mAdView;

    SharedPreferences sp;
    Editor edit;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.settings);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        edit = sp.edit();
        setUpBilling();


        autoModeLayout = (LinearLayout) findViewById(R.id.autoMode_layout);
        vibrationModeLayout = (LinearLayout) findViewById(R.id.vibrationMode_layout);
        adsLayout = (RelativeLayout) findViewById(R.id.ads_layout);
        autoModeBox = (CheckBox) findViewById(R.id.autoModeCheckbox);
        resetButton = (Button) findViewById(R.id.resetCountButton);
        vibrationModeBox = (CheckBox) findViewById(R.id.vibrationModeCheckBox);
        threeFouldRadioButton = (RadioButton) findViewById(R.id.fouls3Radio);
        fourFouldRadioButton = (RadioButton) findViewById(R.id.fouls4Radio);
        feedbackButton = (Button) findViewById(R.id.feedbackButton);
        removeAdsButton = (Button) findViewById(R.id.removeAdsButton);
        mAdView = (AdView) findViewById(R.id.adView);


        autoModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoModeBox.isChecked())
                    autoModeBox.setChecked(false);
                else
                    autoModeBox.setChecked(true);
            }
        });
        vibrationModeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrationModeBox.isChecked())
                    vibrationModeBox.setChecked(false);
                else
                    vibrationModeBox.setChecked(true);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
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

        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAds();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadSettings();

    }

    @Override
    public void onResume() {
        if (isAdsFreeModeEnabled) {
            mAdView.destroy();
            adsLayout.removeAllViews();
            adsLayout.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause() {
        saveSettings();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private void saveSettings() {


        edit.putBoolean(AUTOMODE, autoModeBox.isChecked());
        edit.putBoolean(VIBRATIONMODE, vibrationModeBox.isChecked());
        edit.putBoolean(ADS_FREE_MODE, isAdsFreeModeEnabled);
        if (threeFouldRadioButton.isChecked())
            edit.putBoolean(THREE_FOULS_OPTION, true);
        else
            edit.putBoolean(THREE_FOULS_OPTION, false);

        edit.commit();


    }

    private void loadSettings() {

        autoModeBox.setChecked(sp.getBoolean(AUTOMODE, true));
        vibrationModeBox.setChecked(sp.getBoolean(VIBRATIONMODE, false));
        isAdsFreeModeEnabled = sp.getBoolean(ADS_FREE_MODE, false);

        if (sp.getBoolean(THREE_FOULS_OPTION, false))
            threeFouldRadioButton.setChecked(true);
        else
            fourFouldRadioButton.setChecked(true);


    }

    private void resetData() {


        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.reset_promp, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        yesButton = (Button) promptsView.findViewById(R.id.yesButton);
        noButton = (Button) promptsView.findViewById(R.id.noButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.initializeCountFields();
                Toast.makeText(v.getContext(), "Data Reset", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().

                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // show it
        alertDialog.show();
    }

    private void setUpBilling() {

        mHelper = new IabHelper(this, getResources().getString(R.string.base64EncodedPublicKey));

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d(LOG_TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(LOG_TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });
    }

    private void removeAds() {

        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");

    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if(isAdsFreeModeEnabled){
                adsAlreadyRemovedPromp();
            }
            if (result.getResponse() == 7) {
                isAdsFreeModeEnabled = true;
            }
            if (result.isFailure()) {
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                isAdsFreeModeEnabled = true;
            }

        }
    };

    private void adsAlreadyRemovedPromp() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.message_promp, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button okButton = (Button) promptsView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View okView) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // show it
        alertDialog.show();
    }
}