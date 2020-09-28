package com.blz.cookietech.cookietechmetronomelibrary;

import android.util.Log;

public class Metronome {

    private static final String TAG = "Metronome";
    private volatile double bpm = 20;
    private int beat=4;
    private int silence;

    /*private double beatSound = 900;
    private double sound = 1200;*/
    private final int tick = 2000; // samples of tick

    private volatile boolean play = true;
    private AudioGenerator audioGenerator = new AudioGenerator(16000);
    private double[] ticks;
    private double[] tocks;

    private static Metronome metronome =new Metronome();

    private Metronome() {
        audioGenerator.createPlayer();

        Log.d(TAG, "Metronome: constructor");
    }

    public static Metronome getInstance(){
       

        return metronome;

    }

    public void setTickTock(double[] ticks, double[] tocks){
        this.ticks = ticks;
        this.tocks = tocks;

    }

    private void calcSilence() {
        silence = (int) (((60/bpm)*16000)-tick);
    }

    public void play() {

       /* double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);*/
        /*double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);*/
        double silence = 0;


        int t = 0,s = 0,b = 0;
        int testFlag = 0;
        do {
            calcSilence();
            int sampleSize = this.tick + this.silence;
            double[] sound = new double[sampleSize];
            Log.d(TAG, "play: do");
            for(int i=0;i<sampleSize&&play;i++) {
                Log.d(TAG, "play: for: " + i);
                if(t<this.tick) {
                    testFlag+=1;
                    if(b == 0)
                        sound[i] = tocks[t];
                    else
                        sound[i] = ticks[t];
                    t++;
                } else {
                    sound[i] = silence;
                    s++;
                    if(s >= this.silence) {
                        t = 0;
                        s = 0;
                        b++;
                        if(b > (this.beat-1))
                            b = 0;
                    }
                }
            }
            Log.d("akash_debug", "play: write "+ testFlag);
            audioGenerator.writeSound(sound);

        } while(play);

        Log.d("akash_debug", "play: "+ testFlag);

        /*int sampleSize = this.tick + this.silence;
        double[] sound = new double[sampleSize];
        for (int j =0;j<1;j++){
            for(int i=0;i<sound.length&&play;i++) {
                if(t<this.tick) {
                    if(b == 0)
                        sound[i] = tocks[t];
                    else
                        sound[i] = ticks[t];
                    t++;
                } else {
                    sound[i] = silence;
                    s++;
                    if(s >= this.silence) {
                        t = 0;
                        s = 0;
                        b++;
                        if(b > (this.beat-1))
                            b = 0;
                    }
                }
            }
            audioGenerator.writeSound(sound);
        }*/
    }

    public void stop() {
        play = false;
        audioGenerator.destroyAudioTrack();
    }

    public void playPublic(/*final int bpm */) {
        if(audioGenerator.isPlayerCreated()){
            audioGenerator.initAudioTrack();
            play = true;
        }
        play();
    }


    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public int getTickCount(){
        return ticks.length;

    }
}
