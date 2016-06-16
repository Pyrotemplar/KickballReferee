package com.pyrotemplar.kickballreferee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.pyrotemplar.inappbilling.util.IabHelper;
import com.pyrotemplar.inappbilling.util.IabResult;
import com.pyrotemplar.inappbilling.util.Purchase;

import static android.content.SharedPreferences.Editor;


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
    private static final String HOME_COLOR = "homeColor";
    private static final String AWAY_COLOR = "awayColor";
    private static final String BALL_COLOR = "ballColor";
    private static final String STRIKE_COLOR = "strikeColor";
    private static final String FOUL_COLOR = "foulColor";
    private static final String OUT_COLOR = "outColor";

    boolean isAdsFreeModeEnabled;

    private ColorPicker colorPicker;

    private int ballColor;
    private int strikeColor;
    private int foulColor;
    private int outColor;
    private int homeColor;
    private int awayColor;


    CheckBox autoModeBox;
    CheckBox vibrationModeBox;
    View changeHomeColorButton;
    View changeAwayColorButton;
    View changeBallColorButton;
    View changeStrikeColorButton;
    View changeFoulColorButton;
    View changeOutColorButton;
    Button resetBallColorButton;
    Button resetStrikeColorButton;
    Button resetFoulColorButton;
    Button resetOutColorButton;

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

        homeColor = getResources().getColor(R.color.DefaultHomeColor);
        awayColor = getResources().getColor(R.color.DefaultAwayColor);

        ballColor = getResources().getColor(R.color.ballDefaultColor);
        strikeColor = getResources().getColor(R.color.strikeDefaultColor);
        foulColor = getResources().getColor(R.color.foulDefaultColor);
        outColor = getResources().getColor(R.color.outDefaultColor);


        autoModeLayout = (LinearLayout) findViewById(R.id.autoMode_layout);
        vibrationModeLayout = (LinearLayout) findViewById(R.id.vibrationMode_layout);
        adsLayout = (RelativeLayout) findViewById(R.id.ads_layout);
        autoModeBox = (CheckBox) findViewById(R.id.autoModeCheckbox);
        resetButton = (Button) findViewById(R.id.resetCountButton);
        vibrationModeBox = (CheckBox) findViewById(R.id.vibrationModeCheckBox);
        threeFouldRadioButton = (RadioButton) findViewById(R.id.fouls3Radio);
        fourFouldRadioButton = (RadioButton) findViewById(R.id.fouls4Radio);
        changeHomeColorButton = findViewById(R.id.changeHomeColorButton);
        changeAwayColorButton = findViewById(R.id.changeAwayColorButton);
        changeBallColorButton = findViewById(R.id.ballColorCircle);
        changeStrikeColorButton = findViewById(R.id.strikeColorCircle);
        changeFoulColorButton = findViewById(R.id.foulColorCircle);
        changeOutColorButton = findViewById(R.id.outColorCircle);
        resetBallColorButton = (Button) findViewById(R.id.resetBallColorButton);
        resetStrikeColorButton = (Button) findViewById(R.id.resetStrikeColorButton);
        resetFoulColorButton = (Button) findViewById(R.id.resetFoulColorButton);
        resetOutColorButton = (Button) findViewById(R.id.resetOutColorButton);
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

        changeHomeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);

            }
        });
        changeAwayColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);
            }
        });
        changeBallColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);
            }
        });
        changeStrikeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);
            }
        });
        changeFoulColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);
            }
        });
        changeOutColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerPromp(v);
            }
        });
        resetBallColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCircle(findViewById(R.id.ballColorCircle), getResources().getColor(R.color.ballDefaultColor));

            }
        });
        resetStrikeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCircle(findViewById(R.id.strikeColorCircle), getResources().getColor(R.color.strikeDefaultColor));
            }
        });
        resetFoulColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCircle(findViewById(R.id.foulColorCircle), getResources().getColor(R.color.foulDefaultColor));

            }
        });
        resetOutColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillCircle(findViewById(R.id.outColorCircle), getResources().getColor(R.color.outDefaultColor));

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
        edit.putInt(HOME_COLOR, homeColor);
        edit.putInt(AWAY_COLOR, awayColor);
        edit.putInt(BALL_COLOR, ballColor);
        edit.putInt(STRIKE_COLOR, strikeColor);
        edit.putInt(FOUL_COLOR, foulColor);
        edit.putInt(OUT_COLOR, outColor);


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

        fillRectangle(changeHomeColorButton, sp.getInt(HOME_COLOR, getResources().getColor(R.color.DefaultHomeColor)));
        fillRectangle(changeAwayColorButton, sp.getInt(AWAY_COLOR, getResources().getColor(R.color.DefaultAwayColor)));
        fillCircle(changeBallColorButton, sp.getInt(BALL_COLOR, getResources().getColor(R.color.ballDefaultColor)));
        fillCircle(changeStrikeColorButton, sp.getInt(STRIKE_COLOR, getResources().getColor(R.color.strikeDefaultColor)));
        fillCircle(changeFoulColorButton, sp.getInt(FOUL_COLOR, getResources().getColor(R.color.foulDefaultColor)));
        fillCircle(changeOutColorButton, sp.getInt(OUT_COLOR, getResources().getColor(R.color.outDefaultColor)));


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
            if (isAdsFreeModeEnabled) {
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

    private void updateColors(View v) {

        switch (v.getId()) {
            case R.id.changeHomeColorButton: {
                homeColor = colorPicker.getColor();
                fillRectangle(v, homeColor);
                break;

            }
            case R.id.changeAwayColorButton: {
                awayColor = colorPicker.getColor();
                fillRectangle(v, awayColor);
                break;
            }
            case R.id.ballColorCircle: {
                ballColor = colorPicker.getColor();
                fillCircle(v, ballColor);
                break;
            }
            case R.id.strikeColorCircle: {
                strikeColor = colorPicker.getColor();
                fillCircle(v, strikeColor);
                break;
            }
            case R.id.foulColorCircle: {
                foulColor = colorPicker.getColor();
                fillCircle(v, foulColor);
                break;
            }
            case R.id.outColorCircle: {
                outColor = colorPicker.getColor();
                fillCircle(v, outColor);
                break;
            }
            default:
        }
    }

    private void colorPickerPromp(final View v) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.color_picker_promp, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        colorPicker = (ColorPicker) promptsView.findViewById(R.id.colorPicker);


        Button okButton = (Button) promptsView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View okView) {
                alertDialog.dismiss();
                updateColors(v);
            }
        });

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // show it
        alertDialog.show();

    }

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


    private void fillCircle(View v, int color) {

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setShape(GradientDrawable.OVAL);
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
}