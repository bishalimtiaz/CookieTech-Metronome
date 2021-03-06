package com.blz.cookietech.cookietechmetronomelibrary;

class Metronome {
    private double bpm = 120;
    private int beat=4;
    private int silence;

    /*private double beatSound = 900;
    private double sound = 1200;*/
    private final int tick = 1000; // samples of tick

    private boolean play = true;
    private AudioGenerator audioGenerator = new AudioGenerator(8000);
    private double[] ticks;
    private double[] tocks;

    Metronome(double[] ticks, double[] tocks) {
        audioGenerator.createPlayer();
        this.ticks = ticks;
        this.tocks = tocks;
    }

    private void calcSilence() {
        silence = (int) (((60/bpm)*8000)-tick);
    }

    public void play() {
        calcSilence();
       /* double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);*/
        /*double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);*/
        double silence = 0;
        double[] sound = new double[8000];
        int t = 0,s = 0,b = 0;
        do {

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

        } while(play);

        /*for (int j =0;j<1;j++){
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
        new Thread(new Runnable() {
            public void run() {
                // setBpm(bpm); is possible to set bpm here
                play();
            }
        }).start();
    }


    public void setBpm(double bpm) {
        this.bpm = bpm;
    }
}
