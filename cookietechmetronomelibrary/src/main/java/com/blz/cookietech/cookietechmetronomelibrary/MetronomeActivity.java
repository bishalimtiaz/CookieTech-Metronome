package com.blz.cookietech.cookietechmetronomelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        metronome = new Metronome();
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
            @Override
            public void onClick(View v) {
                metronome.playPublic();
                //audio.createPlayer();
                /*audio.writeSound(doNote);
                audio.writeSound(silence);
                audio.writeSound(reNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(laNote);
                audio.writeSound(silence);
                audio.writeSound(laNote2);
                audio.writeSound(silence);
                audio.writeSound(siNote);
                audio.writeSound(silence);
                audio.writeSound(laNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(doNote2);
                audio.writeSound(silence);
                audio.writeSound(miNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(miNote2);
                audio.writeSound(silence);
                audio.writeSound(doNote3);
                audio.writeSound(silence);
                audio.writeSound(miNote3);
                audio.writeSound(silence);
                audio.writeSound(doNote);
                audio.writeSound(silence);
                audio.writeSound(reNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(laNote);
                audio.writeSound(silence);
                audio.writeSound(laNote2);
                audio.writeSound(silence);
                audio.writeSound(siNote);
                audio.writeSound(silence);
                audio.writeSound(laNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(doNote2);
                audio.writeSound(silence);
                audio.writeSound(miNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(faNote);
                audio.writeSound(silence);
                audio.writeSound(miNote2);
                audio.writeSound(silence);
                audio.writeSound(miNote2);
                audio.writeSound(silence);
                audio.writeSound(reNote2);*/

                /*audio.destroyAudioTrack();*/
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
