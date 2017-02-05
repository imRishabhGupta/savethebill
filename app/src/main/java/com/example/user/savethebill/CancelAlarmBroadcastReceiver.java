package com.example.user.savethebill;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rohanpc on 5/30/2016.
 */
public class
CancelAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        SharedPreferences.Editor editor=context.getSharedPreferences("bills",MODE_PRIVATE).edit();
        editor.putString(bundle.getString("name"), "yes");
        editor.commit();
    }
}