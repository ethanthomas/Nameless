package com.nameless;

import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class Application extends android.app.Application {

    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "pqej0xmoT7vCz9GxyKsaP6LkH5X9wQkyJ992PpBX",
                "fQ6l6ZyEV8hEwEa0AOZLyeddgtZGBI9g5pdTngXs");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseUser.enableAutomaticUser();
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, "Broadcast", MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    private Intent getIntent() {
        return null;
    }
}