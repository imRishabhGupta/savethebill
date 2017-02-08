package com.example.user.savethebill;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

/**
 * Created by rohanpc on 4/27/2016.
 */
public class
AlarmReceiver extends BroadcastReceiver {
    @TargetApi(16)
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

        SharedPreferences preferences=context.getSharedPreferences("bills",Context.MODE_PRIVATE);
        String cancel=preferences.getString(billname+id,null);
        Log.d("AlarmReceiver",cancel+" "+ billname+id);
        if(cancel.equals("no")){

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

            Intent cancellationIntent = new Intent(context, CancelAlarmBroadcastReceiver.class);
            //cancellationIntent.setAction("android.media.action.DISPLAY_NOTIFICATION");
            cancellationIntent.putExtra("name",billname+id);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle("You have about "+ days +" days left!")
                    .setContentText(type)
                    .setTicker(type + "Alert!")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .addAction(R.mipmap.ic_launcher,"Stop",PendingIntent.getBroadcast(context,id,cancellationIntent,PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
        }
        else {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Log.d("cancelAlarmReceiver","REACHED");
            am.cancel(PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }
}