package com.example.user.savethebill;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by rohanpc on 5/30/2016.
 */
public class
CancelAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        PendingIntent pendingIntent = bundle.getParcelable("key");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d("cancelAlarmReceiver","REACHED");
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}