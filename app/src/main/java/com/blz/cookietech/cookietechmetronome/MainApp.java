package com.blz.cookietech.cookietechmetronome;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.blz.cookietech.cookietechmetronomelibrary.Model.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.blz.cookietech.Services.MetronomeService.CHANNEL_ID;

public class MainApp extends MultiDexApplication {




    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        readTickTock();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = getString(com.blz.cookietech.cookietechmetronomelibrary.R.string.metronome_channel_name);
            String description = getString(com.blz.cookietech.cookietechmetronomelibrary.R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_UNKNOWN)
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .build();
            channel.setSound(null,att);
            channel.setVibrationPattern(new long[]{0});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }
    }


      private void readTickTock() {
        //Read Tick samples
        InputStream tick_inputStream = getResources().openRawResource(R.raw.tick_sample);
        BufferedReader tick_bufferedReader= new BufferedReader(new InputStreamReader(tick_inputStream));
        String tick_eachline;

        int i =0;
        try {
            tick_eachline = tick_bufferedReader.readLine();
            while (tick_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                //*String[] words = tick_eachline.split(" ");*//*

                AppSharedComponents.setTick(i,Double.parseDouble(tick_eachline));
                tick_eachline = tick_bufferedReader.readLine();
                i++;
            }
            tick_bufferedReader.close();
            tick_inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Read tock samples

        InputStream tock_inputStream = getResources().openRawResource(R.raw.tock_sample);
        BufferedReader tock_bufferedReader= new BufferedReader(new InputStreamReader(tock_inputStream));
        String tock_eachline = null;

        int j =0;
        try {
            tock_eachline = tock_bufferedReader.readLine();
            while (tock_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                //*String[] words = tock_eachline.split(" ");*//*
                AppSharedComponents.setTock(j,Double.parseDouble(tock_eachline));
                tock_eachline = tock_bufferedReader.readLine();
                j++;
            }
            tock_bufferedReader.close();
            tock_inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
