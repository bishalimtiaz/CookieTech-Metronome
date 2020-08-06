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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        play_btn = findViewById(R.id.play_btn);
        stop_btn = findViewById(R.id.stop_btn);

        audio = new AudioGenerator(8000);
        final double[] silence = audio.getSineWave(200, 8000, 0);

        int noteDuration = 2400;

        final double[] doNote = audio.getSineWave(noteDuration/2, 8000, 523.25);
        final double[] reNote = audio.getSineWave(noteDuration/2, 8000, 587.33);
        final double[] faNote = audio.getSineWave(noteDuration, 8000, 698.46);
        final double[] laNote = audio.getSineWave(noteDuration, 8000, 880.00);
        final double[] laNote2 = audio.getSineWave((int) (noteDuration*1.25), 8000, 880.00);
        final double[] siNote = audio.getSineWave(noteDuration/2, 8000, 987.77);
        final double[] doNote2 = audio.getSineWave((int) (noteDuration*1.25), 8000, 523.25);
        final double[] miNote = audio.getSineWave(noteDuration/2, 8000, 659.26);
        final double[] miNote2 = audio.getSineWave(noteDuration, 8000, 659.26);
        final double[] doNote3 = audio.getSineWave(noteDuration, 8000, 523.25);
        final double[] miNote3 = audio.getSineWave(noteDuration*3, 8000, 659.26);
        final double[] reNote2 = audio.getSineWave(noteDuration*4, 8000, 587.33);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                /** Yesterday's Testing*/
               /* //metronome.playPublic();
                MediaExtractor mex = new MediaExtractor();
                //final AssetFileDescriptor afd=getResources().openRawResourceFd(R.raw.tick);
                final String uriPath="android.resource://"+getPackageName()+"/" + R.raw.tick;
                final Uri uri= Uri.parse(uriPath);
                try {
                    mex.setDataSource(getApplicationContext(),uri,null);// the adresss location of the sound on sdcard.
                    Log.d("sample Size",String.valueOf(mex.getSampleSize()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                MediaFormat mf = mex.getTrackFormat(0);

                //int bitRate = mf.getInteger(MediaFormat.KEY_BIT_RATE);
                int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                String mime = mf.getString(MediaFormat.KEY_MIME);
                //Log.d("bitRate",String.valueOf(bitRate));
                Log.d("sampleRate",String.valueOf(sampleRate));*//*
                final String uriPath="android.resource://"+getPackageName()+"/" + R.raw.tick;
                //final Uri uri= Uri.parse(uriPath);
                File file =  new File(String.valueOf(getResources().openRawResource(R.raw.tick)));
                Log.d("Can Read", String.valueOf(file.canRead()));*/

                InputStream inputStream = getResources().openRawResource(R.raw.filename);
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                String eachline = null;
                double [] tick = new double[1896];
                int i =0;
                try {
                    eachline = bufferedReader.readLine();
                    while (eachline != null) {
                        // `the words in the file are separated by space`, so to get each words
                        String[] words = eachline.split(" ");
                        Log.d("value: ", eachline);
                        tick[i] = Double.parseDouble(eachline);
                        eachline = bufferedReader.readLine();
                        i++;
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                metronome = new Metronome(tick);
                metronome.playPublic();

            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metronome.stop();

            }
        });
    }
}
