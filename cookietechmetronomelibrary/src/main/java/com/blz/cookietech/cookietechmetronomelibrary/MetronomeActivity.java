package com.blz.cookietech.cookietechmetronomelibrary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MetronomeActivity extends AppCompatActivity {

    private Button play_btn, stop_btn;
    Metronome metronome;
    AudioGenerator audio;
    private double [] tick = new double[1050];
    private double [] tock = new double[1050];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        play_btn = findViewById(R.id.play_btn);
        stop_btn = findViewById(R.id.stop_btn);

        audio = new AudioGenerator(8000);

        initializeFields();

        play_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //metronome = new Metronome(tick,tock);
                //metronome.playPublic();
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metronome.stop();

            }
        });
    }

    private void initializeFields() {
        //Read Tick samples
        InputStream tick_inputStream = getResources().openRawResource(R.raw.tick_sample_04);
        BufferedReader tick_bufferedReader= new BufferedReader(new InputStreamReader(tick_inputStream));
        String tick_eachline;

        int i =0;
        try {
            tick_eachline = tick_bufferedReader.readLine();
            while (tick_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                /*String[] words = tick_eachline.split(" ");*/

                tick[i] = Double.parseDouble(tick_eachline);
                tick_eachline = tick_bufferedReader.readLine();
                i++;
            }
            tick_bufferedReader.close();
            tick_inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Read tock samples

        InputStream tock_inputStream = getResources().openRawResource(R.raw.tock_sample_04);
        BufferedReader tock_bufferedReader= new BufferedReader(new InputStreamReader(tock_inputStream));
        String tock_eachline = null;

        int j =0;
        try {
            tock_eachline = tock_bufferedReader.readLine();
            while (tock_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                /*String[] words = tock_eachline.split(" ");*/

                tock[j] = Double.parseDouble(tock_eachline);
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
