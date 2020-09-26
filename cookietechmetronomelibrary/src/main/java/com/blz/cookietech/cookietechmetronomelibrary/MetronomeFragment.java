package com.blz.cookietech.cookietechmetronomelibrary;

import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.blz.cookietech.Listener.BPMListener;
import com.blz.cookietech.cookietechmetronomelibrary.View.ChordEraRoundWheeler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetronomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 * sample -> 6000
 * sample_01 -> 4500
 * sample_02 -> 3000
 * sample_03 -> 1500
 * sample_04 -> 1050
 */
public class MetronomeFragment extends Fragment implements BPMListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private CardView play_pause_btn;
    private ImageView play_pause_icon;
    private boolean isPlaying = false;

    private static final String TAG = "MetronomeFragment";

    private double [] tick = new double[3000];
    private double [] tock = new double[3000];

    private Metronome metronome;
    private AudioGenerator audio;

    private ChordEraRoundWheeler bpmWheel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetronomeFragment() {
        // Required empty public constructor
        Log.d(TAG, "MetronomeFragment: ");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetronomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetronomeFragment newInstance(String param1, String param2) {
        MetronomeFragment fragment = new MetronomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        audio = new AudioGenerator(8000);
        metronome = new Metronome(tick,tock);
        readTickTock();
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
        play_pause_btn = view.findViewById(R.id.play_pause_btn);
        play_pause_icon = view.findViewById(R.id.play_pause_icon);
        bpmWheel = view.findViewById(R.id.bpmWheel);
        bpmWheel.setBPMListener(this);

        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPlaying){

                    metronome.playPublic();
                    isPlaying = true;
                    play_pause_icon.setImageResource(R.drawable.ic_pause);

                }
                else{

                    metronome.stop();
                    isPlaying = false;
                    play_pause_icon.setImageResource(R.drawable.ic_play);
                }

            }
        });

    }

    @Override
    public void onBPMChange(float bpm) {

        metronome.setBpm((double)bpm);
        Log.d("bpm count", String.valueOf(bpm));

    }

    static class MetronomeRunnable implements Runnable{

        @Override
        public void run() {

        }
    }


}