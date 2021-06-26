package com.example.cqcq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String NEXT_ALARM = "nextAlarm";
    private Timer timer;
    Long period;
    String value;

    android.os.Handler customHandler = new android.os.Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // write code for creating foreground service

        if (intent != null && intent.getExtras()!=null) {
            value = intent.getStringExtra("minutes");
            period = Long.parseLong(value) * 60000;

            createNotificationChannel();

            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

            Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                    .setContentTitle("CqCq")
                    .setContentText(value.equals("1") ? "Repeating period : 1 minute" : "Repeating period : " + value + " minutes")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).build();

            startForeground(1, notification);

            customHandler.postDelayed(updateTimerThread, 0);

            /*timer = new Timer();
            TimerTask task = new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    saveData(value);
                    playSound();
//                    System.out.println(period);
                }
            };

            timer.scheduleAtFixedRate(task, 0,period);*/
        } else {
            System.out.println("idk something happened");
        }
        return START_STICKY;
    }

    private Runnable updateTimerThread = new Runnable()
    {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {
            //write here whatever you want to repeat
//            System.out.println("Running: " + new java.util.Date());
            updateNotification();
//            saveData(value);
            playSound();
            customHandler.postDelayed(this, period);
        }
    };

    void stopRepeatingTask() {
        customHandler.removeCallbacks(updateTimerThread);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateNotification() {
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a");
        LocalDateTime now = LocalDateTime.now().plusMinutes(Integer.parseInt(value));

        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle("CqCq")
                .setContentText("Next alarm at : " + dtf.format(now) )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
//        timer.cancel();
        stopRepeatingTask();
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }

    public void playSound(){
        MediaPlayer mediaPlayer=MediaPlayer.create(MyService.this,R.raw.glass_sound);
        mediaPlayer.start();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void saveData(String interval) {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
//        LocalDateTime now = LocalDateTime.now().plusMinutes(Integer.parseInt(interval));
////        System.out.println(dtf.format(now));
//
//        editor.putString(NEXT_ALARM, dtf.format(now));
//        editor.apply();
//    }
}
