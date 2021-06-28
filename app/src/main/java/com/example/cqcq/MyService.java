package com.example.cqcq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
    private Timer timer;
    String value;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // write code for creating foreground service

        if (intent != null && intent.getExtras()!=null) {
            value = intent.getStringExtra("minutes");

            createNotificationChannel();

            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

            Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                    .setContentTitle("CqCq")
                    .setContentText("body")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).build();

            startForeground(1, notification);

            timer = new Timer();
            TimerTask task = new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    updateNotification();
                    playSound();
                }
            };

            timer.scheduleAtFixedRate(task, 0,Integer.parseInt(value)*60000);
        } else {
            System.out.println("idk something happened");
        }
        return START_STICKY;
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
        timer.cancel();
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }

    public void playSound(){
        MediaPlayer mediaPlayer=MediaPlayer.create(MyService.this,R.raw.glass_sound);
        mediaPlayer.start();
    }
}
