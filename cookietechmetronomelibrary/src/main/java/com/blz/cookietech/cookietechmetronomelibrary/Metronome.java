package com.blz.cookietech.cookietechmetronomelibrary;

public class Metronome {
    private double bpm = 300;
    private int beat=4;
    private int noteValue;
    private int silence;

    private double beatSound = 900;
    private double sound = 1200;
    private final int tick = 1000; // samples of tick

    private boolean play = true;
    private AudioGenerator audioGenerator = new AudioGenerator(8000);
    private double[] ticks;

    public Metronome(double[] ticks) {
        audioGenerator.createPlayer();
        this.ticks = ticks;
    }

    public void calcSilence() {
        silence = (int) (((60/bpm)*8000)-tick);
    }

    public void play() {
        calcSilence();
       /* double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);*/
        double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);
        double silence = 0;
        double[] sound = new double[8000];
        int t = 0,s = 0,b = 0;
        do {
            for(int i=0;i<sound.length&&play;i++) {
                if(t<this.tick) {
                    if(b == 0)
                        sound[i] = tock[t];
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
        } while(play);
    }

    public void stop() {
        play = false;
        audioGenerator.destroyAudioTrack();
    }

    public void playPublic(/*final int bpm */) {
        new Thread(new Runnable() {
            public void run() {
// setBpm(bpm); is possible to set bpm here
                play();
            }
        }).start();
    }
}
