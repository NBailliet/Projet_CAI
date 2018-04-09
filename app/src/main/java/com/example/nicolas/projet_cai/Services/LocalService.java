package com.example.nicolas.projet_cai.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.nicolas.projet_cai.RunYourData;
import com.example.nicolas.projet_cai.Fragments.HomeFragment;
import com.example.nicolas.projet_cai.R;
import com.example.nicolas.projet_cai.SettingsManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by Nicolas on 05/04/2018.
 */

public class LocalService extends Service
{
    Timer timer;
    private Context ctx;
    private Date date;
    Chronometer chronometer;
    static long seconds;
    static long lastSeconds;

    SettingsManager settings;
    private static final int LOC_API_CALL_INTERVAL = 1000;
    NotificationManager mNotificationManager;
    String PLAY_ACTION;
    String PAUSE_ACTION;
    String STOP_ACTION;
    Boolean testPause;


    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        Context context = this;
        super.onCreate();
        ctx = this;
        settings= RunYourData.getSettingsManager();
        System.out.println("Service créé");
        startService();
        int mId = 0;
        testPause = false;

        Intent intent = new Intent(this, RunYourData.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.runyourdatapetit2)
                        .setContentTitle("RunYourData Run")
                        .setContentText("Click to go back to RunYourData app")
                        .setColor(2)
                        .setUsesChronometer(true)
                        .setContentIntent(pIntent);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

        //onReceive(context,intentPlay);

    }

    public void startService()
    {
        System.out.println("Service lancé");
        chronometer = new Chronometer(LocalService.this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startTimer();
    }

    public void onDestroy()
    {
        super.onDestroy();
        stopTimer();
        Toast.makeText(this, "Service arrêté ...", Toast.LENGTH_SHORT).show();
        mNotificationManager.cancel(0);
    }

    private final Handler toastHandler = new Handler()
    {
        public void handleMessage(Notification.MessagingStyle.Message msg)
        {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
    };

    //Timer related functions

    private void startTimer(){
        if(timer!=null ){
            return;
        }
        final long[] newseconds = new long[1];
        newseconds[0]=0;
        timer=new Timer();
        final long[] counter = new long[1];
        Log.e("START TIMER", "1");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (settings.getStopPref()) {
                    newseconds[0] = lastSeconds;
                    System.out.println("NEW SECONDS VALUE = "+String.valueOf(newseconds[0]));
                    counter[0] = counter[0] + 1;
                    settings.setStopPref(false);
                }
                if (settings.getStartPref()) {
                    lastSeconds = LocalService.getSeconds();
                    System.out.println("LAST SECONDS VALUE = "+String.valueOf(lastSeconds));
                    stopTimer();
                    settings.setStartPref(false);
                }
                long millis= SystemClock.elapsedRealtime() - chronometer.getBase() + newseconds[0]*999;
                seconds = millis / 1000;
                Log.e("TIME SERVICE :", "" + seconds);
                if (seconds>600) {
                    stopSelf();
                }
            }

        }, 0, LOC_API_CALL_INTERVAL);
        Log.e("START TIMER", "2");
    }

    private void stopTimer(){

        if(null!=timer){

            timer.cancel();
            timer=null;
        }
    }

    public static int getSeconds(){
        return (int) seconds;
    }


}