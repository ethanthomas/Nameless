package com.nameless;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.parse.ParseInstallation;

import at.markushi.ui.RevealColorView;

public class MainActivity extends Activity implements View.OnClickListener {

    Button connectButton;
    RevealColorView revealColorView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       sharedPreferences = getSharedPreferences("CurrentUser", 0);
        initializeViews();

        if (sharedPreferences.getBoolean("hasBeenInitialized", false) == false)
            setupParseForInitialUse();
    }

    public void initializeViews() {
        connectButton = (Button) findViewById(R.id.connectButton);
        revealColorView = (RevealColorView) findViewById(R.id.reveal);

        setListeners();
    }


    public void setupParseForInitialUse() {
        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        parseInstallation.put("username", parseInstallation.getObjectId());
        parseInstallation.saveInBackground();
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putBoolean("hasBeenInitialized", false);
        edit.commit();

        Log.d("IN", String.valueOf(sharedPreferences.getBoolean("hasBeenInitialized", false)));
    }

    public void setListeners() {
        connectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectButton:
                openChat();
                break;
        }
    }

    public void openChat() {

        connectButton.animate().translationY(-450).setInterpolator(new DecelerateInterpolator()).alpha(0.0f);

        revealColorView.setVisibility(View.VISIBLE);
        revealColorView.reveal((revealColorView.getLeft() + revealColorView.getRight()) / 2,
                (revealColorView.getTop() + revealColorView.getBottom()) / 2, getColor(R.color.green_800), 0, 450, null);

    }

    public int getColor(int id) {
        return getResources().getColor(id);
    }


}
