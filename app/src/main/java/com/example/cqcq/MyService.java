package com.example.cqcq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {

    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // write code for creating foreground service

        createNotificationChannel();

        Intent intent1 = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle("My App Tutorial")
                .setContentText("Our app is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();


        startForeground(1, notification);


        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                playSound();
            }
        };

        timer.scheduleAtFixedRate(task,0,60000);

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
