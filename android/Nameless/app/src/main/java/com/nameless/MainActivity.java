package com.nameless;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

//    public void checkLocation() {
//        LocationManager lm = null;
//        boolean gps_enabled = false,network_enabled = false;
//        if(lm==null)
//            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        try{
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        }catch(Exception ex){}
//        try{
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        }catch(Exception ex){}
//
//        if(!gps_enabled && ){
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage("Location not enabled");
//            dialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(myIntent);
//                }
//            });
//            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//
//                }
//            });
//            dialog.show();
//
//        }
//    }

}
