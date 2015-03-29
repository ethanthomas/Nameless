package com.nameless;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseReceiver extends BroadcastReceiver {
    private final String TAG = "Parse Notification";
    private String msg = "";
    String alert;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i(TAG, "PUSH RECEIVED!!!");

//Get JSON data and put them into variables
        try {

            JSONObject json = new JSONObject(intent.getExtras().getString(
                    "com.parse.Data"));

            alert = json.getString("mes").toString();

        } catch (JSONException e) {

        }

        Intent launchActivity = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, launchActivity, 0);

        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (MainActivity.paused) {
            Intent notificationIntent = new Intent(ctx, MainActivity.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent intent1 = PendingIntent.getActivity(ctx, 0,
                    notificationIntent, 0);

            final Notification.Builder notif = new Notification.Builder(ctx)
                    .setContentTitle("Nameless")
                    .setContentText(alert)
                            //      .setTicker(getString(R.string.tick)) removed, seems to not show at all
                            //      .setWhen(System.currentTimeMillis()) removed, match default
                    .setContentIntent(intent1)
                    .setColor(ctx.getResources().getColor(R.color.green_800)) //ok
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                            //      .setCategory(Notification.CATEGORY_CALL) does not seem to make a difference
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true); //does not seem to make a difference
            //      .setVisibility(Notification.VISIBILITY_PRIVATE); //does not seem to make a difference

            nm.notify(0, notif.build());
        } else {

            MainActivity.messages.add(MainActivity.getCurrentChatId() + "//" + alert);
            MainActivity.update();

        }
    }
}