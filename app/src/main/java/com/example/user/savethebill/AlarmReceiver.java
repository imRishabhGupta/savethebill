package com.example.user.savethebill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

/**
 * Created by rohanpc on 4/27/2016.
 */
public class
AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newintent = new Intent(context, AllBills.class);
        Bundle bundle=intent.getExtras();
        String billname;
        String type;
        Long endate1;
        int id;

        billname = bundle.getString("name");
        endate1=bundle.getLong("daysleft");
        type = bundle.getString("type");
        id=bundle.getInt("id");

        Date setDate= new Date();
        setDate.setTime(endate1);
        Date todayDate = new Date();
        Long diffMillis=endate1-todayDate.getTime();

        float dayCount = (float) diffMillis / (24 * 60 * 60 * 1000);
        int days=(int)dayCount+1;
        Log.d("alarmreceiver", String.valueOf(days));
        Log.d("alarmreceiver",String.valueOf(diffMillis));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AllBills.class);
        stackBuilder.addNextIntent(newintent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("You have about "+ days +" days left!")
                .setContentText(type)
                .setTicker(type + "Alert!")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
}