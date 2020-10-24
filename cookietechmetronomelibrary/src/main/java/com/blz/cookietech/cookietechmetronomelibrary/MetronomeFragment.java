package com.blz.cookietech.cookietechmetronomelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.NumberPicker;
;
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager;
import com.blz.cookietech.Adapter.SubdivisionAdapter;
import com.blz.cookietech.Dialogs.TimerDialog;
import com.blz.cookietech.Helpers.Constants;
import com.blz.cookietech.Listener.BPMListener;
import com.blz.cookietech.Listener.StopTimerListener;
import com.blz.cookietech.Services.MetronomeService;
import com.blz.cookietech.cookietechmetronomelibrary.View.ChordEraRoundWheeler;
import com.blz.cookietech.cookietechmetronomelibrary.View.LightsView;
import com.blz.cookietech.cookietechmetronomelibrary.View.TimerWheeler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 * sample -> 6000
 * sample_01 -> 4500
 * sample_02 -> 3000
 * sample_03 -> 1500
 * sample_04 -> 1050
 */
public class MetronomeFragment extends Fragment implements BPMListener, StopTimerListener, NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, TimerDialog.TimerDialogListener {




    private static final String TAG = "MetronomeFragment";
    private final double [] tick = new double[3000];
    private final double [] tock = new double[3000];

    private CardView play_pause_btn;
    private ImageView play_pause_icon;

    private Metronome metronome;
    private AudioGenerator audio;

    private ChordEraRoundWheeler bpmWheel;

    private TimerWheeler timerWheel;
    private NumberPicker leftTimeSignaturePicker, rightTimeSignaturePicker;

    /** This Value may be handled with sharedPreference Or ViewModel on savedInstanceState**/
    private int leftTimeSignature = 4;
    private int rightTimeSignature = 4;
    private boolean isPlaying = false;
    private int subdivisionPosition = 0;
    private int BPM = 80;
    private boolean isTimerEnabled = false;
    private int minutes =10;


    /** Lights View **/
    LightsView lightsView;

    /** Subdivision RecyclerView **/
    private RecyclerView subdivisionRecyclerView;

    /** Subdivision Container **/
    private ConstraintLayout subdivisionContainer;
    private int subdivisionContainerWidth;
    private int subdivisionContainerHeight;

    /** Wheeler Container **/
    private ConstraintLayout wheelerContainer;
    private int wheelerContainerHeight;
    private int wheelerContainerWidth;




    public MetronomeFragment() {
        // Required empty public constructor
        Log.d(TAG, "MetronomeFragment: ");
        // test
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readTickTock();
        audio = new AudioGenerator(8000);
        metronome = Metronome.getInstance();
        metronome.setTickTock(tick,tock);

        Log.d(TAG, "onCreate: tick count: " + metronome.getTickCount());

    }

    private void readTickTock() {
        //Read Tick samples
        InputStream tick_inputStream = getResources().openRawResource(R.raw.tick_sample_02);
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

        InputStream tock_inputStream = getResources().openRawResource(R.raw.tock_sample_02);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metronome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Initialize play pause button **/
        play_pause_btn = view.findViewById(R.id.play_pause_btn);
        play_pause_icon = view.findViewById(R.id.play_pause_icon);

        /** Initialize Time Signature Picker **/
        leftTimeSignaturePicker  = view.findViewById(R.id.leftTimeSignaturePicker);
        rightTimeSignaturePicker = view.findViewById(R.id.rightTimeSignaturePicker);

        /** Initialize BPM Wheel Section**/
        bpmWheel = view.findViewById(R.id.bpmWheel);
        bpmWheel.setBPM(BPM);
        bpmWheel.setBPMListener(this);

        /** Initialize Lights View **/
        lightsView = view.findViewById(R.id.lightsView);

        /**Initialize  Subdivision RecyclerView **/
        subdivisionRecyclerView = view.findViewById(R.id.subdivisionRecyclerView);

        /**Initialize  Subdivision Container **/
        subdivisionContainer = view.findViewById(R.id.subdivisionContainer);

        /**Initialize Timer Wheel **/
        timerWheel = view.findViewById(R.id.timerWheel);

        /** Initialize wheelerContainer **/
        wheelerContainer = view.findViewById(R.id.wheelerContainer);



        /** Timer Wheel Section **/
        SetUpTimerWheel();

        timerWheel.setOnStopTimerListener(this);
        timerWheel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TimerDialog timerDialog = new TimerDialog(isTimerEnabled, minutes);
                timerDialog.show(getChildFragmentManager(),"Timer Dialog");
                return true;
            }
        });



        /** Time Signature Picker Section**/
        rightTimeSignaturePicker.setMinValue(1);
        rightTimeSignaturePicker.setMaxValue(8);
        leftTimeSignaturePicker.setMinValue(1);
        leftTimeSignaturePicker.setMaxValue(16);
        leftTimeSignaturePicker.setOnValueChangedListener(this);
        rightTimeSignaturePicker.setOnValueChangedListener(this);
        leftTimeSignaturePicker.setOnScrollListener(this);
        rightTimeSignaturePicker.setOnScrollListener(this);
        leftTimeSignaturePicker.setValue(leftTimeSignature);
        rightTimeSignaturePicker.setValue(rightTimeSignature);


        /** Lights View Section**/
        lightsView.setLightNumber(leftTimeSignature);

        /** Set BPM Wheel listener**/
        bpmWheel.setBPMListener(this);




        /** Dynamically change the bpm wheeler and timer wheeler size **/
        wheelerContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.d(TAG, "onGlobalLayout: called");
                wheelerContainerHeight = wheelerContainer.getHeight();
                wheelerContainerWidth = wheelerContainer.getWidth();
                int topBottomPadding = wheelerContainerHeight/10;
                int leftRightPadding = wheelerContainerWidth/10;
                int marginFromOtherWheeler = wheelerContainerWidth/20;

                Log.d(TAG, "onGlobalLayout: wheelerContainerHeight : " + wheelerContainerHeight);
                Log.d(TAG, "onGlobalLayout: wheelerContainerWidth : " + wheelerContainerWidth);
                Log.d(TAG, "onGlobalLayout: topBottomPadding : " + topBottomPadding);
                Log.d(TAG, "onGlobalLayout: marginFromOtherWheeler : " + marginFromOtherWheeler);



                //int wheelerContainerMiddle = wheelerContainerWidth/2;
                int sixtyPercentOfWidth = (wheelerContainerWidth * 3)/5;
                int fortyPercentOfWidth = wheelerContainerWidth - sixtyPercentOfWidth;

                Log.d(TAG, "onGlobalLayout: sixtyPercentOfWidth : " + sixtyPercentOfWidth);
                Log.d(TAG, "onGlobalLayout: fortyPercentOfWidth : " + fortyPercentOfWidth);


                if (wheelerContainerHeight > 0 && wheelerContainerWidth > 0){

                    wheelerContainer.setPadding(leftRightPadding/2,topBottomPadding/2,leftRightPadding/2,topBottomPadding/2);

                    int bpmWheelWidth = sixtyPercentOfWidth -marginFromOtherWheeler;
                    int bpmWheelHeight = wheelerContainerHeight - topBottomPadding;
                    int bpmWheelSize = Math.min(bpmWheelWidth, bpmWheelHeight);
                    Log.d(TAG, "onGlobalLayout: bpmWheelWidth : " + bpmWheelWidth);
                    Log.d(TAG, "onGlobalLayout: bpmWheelHeight : " + bpmWheelHeight);
                    Log.d(TAG, "onGlobalLayout: bpmWheelSize : " + bpmWheelSize);

                    ViewGroup.LayoutParams params = bpmWheel.getLayoutParams();
                    params.height = bpmWheelSize;
                    params.width = bpmWheelSize;
                    bpmWheel.setLayoutParams(params);

                    int timerWheelWidth = fortyPercentOfWidth - marginFromOtherWheeler;
                    int timerWheelSize = Math.min(timerWheelWidth,bpmWheelHeight);
                    Log.d(TAG, "onGlobalLayout: timerWheelWidth : " + timerWheelWidth);
                    Log.d(TAG, "onGlobalLayout: timerWheelSize : " + timerWheelSize);


                    params = timerWheel.getLayoutParams();
                    params.height = timerWheelSize;
                    params.width = timerWheelSize;
                    timerWheel.setLayoutParams(params);
                    Log.d(TAG, "onGlobalLayout: called");



                    int mainMiddle = leftRightPadding/2 + bpmWheelSize + marginFromOtherWheeler;
                    int buttonMiddle = play_pause_btn.getLeft() + (play_pause_btn.getWidth()/2);

                    play_pause_btn.setTranslationX(mainMiddle-buttonMiddle);




                    wheelerContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }





            }
        });



        /** Setup Adapter and recyclerView Function **/
        RecyclerView.LayoutManager layoutManager = new LoopingLayoutManager(
                getContext(),                           // Pass the context.
                LoopingLayoutManager.VERTICAL,  // Pass the orientation. Vertical by default.
                false                           // Pass whether the views are laid out in reverse.
                // False by default.
        );

        subdivisionRecyclerView.setLayoutManager(layoutManager);
        subdivisionRecyclerView.setHasFixedSize(true);
        SubdivisionAdapter adapter = new SubdivisionAdapter(subdivisionRecyclerView);
        subdivisionRecyclerView.setAdapter(adapter);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(subdivisionRecyclerView);

        subdivisionRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);



                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //Log.d(TAG, "onScrollStateChanged: newState : " + newState);
                    View centerView = snapHelper.findSnapView(layoutManager);
                    int pos = layoutManager.getPosition(centerView);
                    //Log.d(TAG, "onScrollStateChanged: pos : " + pos);
                    subdivisionPosition = pos;
                }
            }
        });

       



        /** Play Pause Functionality **/
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent metronomeServiceIntent = new Intent(getActivity(), MetronomeService.class);

                if (!isPlaying){

                    //play
                    //metronome.playPublic();

                    /**This will start service when app is open**/
                    //startService(serviceIntent);
                    //metronomeServiceIntent.putExtra("metronome", (Parcelable) metronome);
                    //ContextCompat.startForegroundService(Objects.requireNonNull(getActivity()),metronomeServiceIntent);
                    isPlaying = true;
                    play_pause_icon.setImageResource(R.drawable.ic_pause);
                    timerWheel.startTimer();

                }
                else{

                   //stop
                    // metronome.stop();

                    //Objects.requireNonNull(getActivity()).stopService(metronomeServiceIntent);

                    resetPlayPauseBtn();
                    timerWheel.stopTimer();
                }

            }
        });

    }

    private void resetPlayPauseBtn() {
        isPlaying = false;
        play_pause_icon.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onBPMChange(int bpm) {

        metronome.setBpm(bpm);
        BPM = bpm;
        Log.d("bpm count", String.valueOf(bpm));

    }


    @Override
    public void onStopTimer() {
        resetPlayPauseBtn();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        if (picker.getId() == R.id.leftTimeSignaturePicker){
            leftTimeSignature = newVal;

            Log.d(TAG, "onValueChange: called");
            Log.d(TAG, "onValueChange: leftTimeSignature : " + leftTimeSignature);

        }
        else if (picker.getId() == R.id.rightTimeSignaturePicker){
            rightTimeSignature = newVal;
        }

    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE){
            if (view.getId() == R.id.leftTimeSignaturePicker){
                //leftTimeSignature = newVal;
                lightsView.setLightNumber(leftTimeSignature);

            }
            else if (view.getId() == R.id.rightTimeSignaturePicker){
                //rightTimeSignature = newVal;
            }

        }

    }


    @Override
    public void getTimePickerStatus(int minute, boolean isEnabled) {
        minutes = minute;
        isTimerEnabled = isEnabled;
        SetUpTimerWheel();
        Log.d(TAG, "getTimePickerStatus: minutes : " + minutes + " isTimerEnabled : " + isTimerEnabled);
    }

    private void SetUpTimerWheel(){
        if (isTimerEnabled){
            timerWheel.setUpTimer(minutes);
        }
        else {
            timerWheel.setUpTimer(Constants.ZERO);
        }
    }
}