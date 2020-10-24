package com.blz.cookietech.cookietechmetronomelibrary;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blz.cookietech.cookietechmetronomelibrary.Model.Constants;

public class Metronome extends HandlerThread {


    private static final String NAME = "Metronome Thread";
    private int timeSignature = 4;
    private int[] combination = {1,1,1,1};

    public static final int MESSAGE_PLAY_PAUSE = 1;


    private volatile boolean play = true;
    private AudioGenerator audioGenerator = new AudioGenerator(44100);


    public Looper looper;
    public Handler handler;
    // handler is associated with the message queue of this thread

    BeatGenerator beatGenerator = new BeatGenerator();

    public Metronome() {
        super(NAME, Process.THREAD_PRIORITY_FOREGROUND);
    }

    @Override
    protected void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("akash_debug", "message comes:");
                switch (msg.what){
                    case Metronome.MESSAGE_PLAY_PAUSE:
                        if(msg.arg1== 1){
                            beatGenerator.setPlaying(true);
                        }else if(msg.arg1 == 0){
                            beatGenerator.setPlaying(false);
                        }

                        break;

                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }


    /*    private int calcSilence(int subdivision) {
        return  (int) (((60/bpm)*22000)/(subdivision*2));
    }*/
/*

    private int calcSilence(int subdivision) {
        return  (int) (((60/bpm)*44100)/(subdivision*2));
    }
    private long sleepTime(int subdivision){
        return (long) ((60000/bpm)/(subdivision*2));
    }
*/


    @Override
    public void run() {

        Log.d("akash_debug", "start of run():");

        audioGenerator.createPlayer();
        //handler will only work if there is a looper
        Looper.prepare(); // it creates both looper and msg queue

        looper = Looper.myLooper();//initialize looper

        // handler is associated with the message queue of this thread
        // start the loopers loop
        Looper.loop(); // this is an infinite loop until get a exit msg

        audioGenerator.destroyAudioTrack();



        Log.d("akash_debug", "End of run():");
    }

    public void play() {

       /* double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);*/
        /*double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);*/
        double silence = 0;


        int t = 0,s = 0,b = 0;
        int testFlag = 0;
 /*       do {
            beat = 4*subdivision;
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

        } while(play);*/

        /*int cPointer = 0;
        boolean beatSlice = false;
        int beatCount = 0;
        long startTime = 0;
        do {
            double[] finalSample;
            int subdivision = combination.length;
            int beatLoop = timeSignature*subdivision;
            int silenceLength = calcSilence(subdivision);
            Log.d("akash_metronome", "play: "+ silenceLength);
            if(silenceLength < 0)
                silenceLength = 0;
            double[] silenceSample = new double[1];
            if(!beatSlice){
                finalSample = silenceSample;
                beatSlice = true;
            }else{
                if(combination[cPointer] != 0 || beatCount < subdivision){
                    if(beatCount == 0){
                        finalSample = Constants.getTick();
                        //audioGenerator.writeSound(ticks);
                    }else{
                        finalSample = Constants.getTock();
                        //audioGenerator.writeSound(tocks);
                    }
                }
                else{
                    finalSample = new double[tick];
                    //audioGenerator.writeSound(new double[tick]);
                }

                beatCount++;
                if(beatCount >= beatLoop){
                    beatCount = 0;
                }
                beatSlice = false;

                cPointer++;
                if(cPointer >= subdivision){
                    cPointer = 0;
                }
            }
            if(startTime != 0){
                try {
                    long sleepTime = sleepTime(subdivision) - (System.currentTimeMillis()-startTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            audioGenerator.writeSound(finalSample);
            startTime = System.currentTimeMillis();



        } while(play);*/

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

    public void stopMetronome() {
        play = false;
        audioGenerator.pauseAudioTrack();
        Message messagePause = new Message();
        messagePause.what = MESSAGE_PLAY_PAUSE;
        messagePause.arg1 = 0;
        handler.sendMessageAtFrontOfQueue(messagePause);
    }

    public void playMetronome() {
        if(audioGenerator.isPlayerCreated()){
            audioGenerator.playAudioTrack();
        }
        beatGenerator.setPlaying(true);
        Message messagePlay = new Message();
        messagePlay.what = MESSAGE_PLAY_PAUSE;
        messagePlay.arg1 = 1;
        handler.sendMessageAtFrontOfQueue(messagePlay);
        handler.post(beatGenerator);
    }



    private class BeatGenerator implements Runnable{
        boolean playing = false;

        public void setPlaying(boolean playing) {
            this.playing = playing;
        }

        private int calcSilence(int subdivision) {
            return  (int) (((60/Constants.getBpm())*44100)/(subdivision*2));
        }
        private long sleepTime(int subdivision){
            return (long) ((60000/Constants.getBpm())/(subdivision*2));
        }

        @Override
        public void run() {

            Log.d("akash_debug", "run: " + System.currentTimeMillis());
            if(playing){
                handler.postDelayed(beatGenerator,100);
            }

           /* double[] finalSample;
            int subdivision = combination.length;
            int beatLoop = timeSignature*subdivision;
            int silenceLength = calcSilence(subdivision);
            Log.d("akash_metronome", "play: "+ silenceLength);
            if(silenceLength < 0)
                silenceLength = 0;
            double[] silenceSample = new double[1];
            if(!beatSlice){
                finalSample = silenceSample;
                beatSlice = true;
            }else{
                if(combination[cPointer] != 0 || beatCount < subdivision){
                    if(beatCount == 0){
                        finalSample = Constants.getTick();
                        //audioGenerator.writeSound(ticks);
                    }else{
                        finalSample = Constants.getTock();
                        //audioGenerator.writeSound(tocks);
                    }
                }
                else{
                    finalSample = new double[tick];
                    //audioGenerator.writeSound(new double[tick]);
                }

                beatCount++;
                if(beatCount >= beatLoop){
                    beatCount = 0;
                }
                beatSlice = false;

                cPointer++;
                if(cPointer >= subdivision){
                    cPointer = 0;
                }
            }
            if(startTime != 0){
                try {
                    long sleepTime = sleepTime(subdivision) - (System.currentTimeMillis()-startTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            audioGenerator.writeSound(finalSample);
            startTime = System.currentTimeMillis();
            if(playing){
                handler.postDelayed(beatGenerator,100);
            }*/

        }
    }
}
