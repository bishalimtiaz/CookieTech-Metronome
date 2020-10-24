package com.blz.cookietech.cookietechmetronomelibrary;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blz.cookietech.Listener.BPMListener;
import com.blz.cookietech.Services.MetronomeService;
import com.blz.cookietech.cookietechmetronomelibrary.Model.Constants;
import com.blz.cookietech.cookietechmetronomelibrary.View.ChordEraRoundWheeler;

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
public class MetronomeFragment extends Fragment implements BPMListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private CardView play_pause_btn;
    private ImageView play_pause_icon;
    private boolean isPlaying = false;

    private static final String TAG = "MetronomeFragment";


    private ChordEraRoundWheeler bpmWheel;

    private ServiceConnection serviceConnection;

    boolean mBound = false;
    private MetronomeService mService;
    Intent metronomeServiceIntent;
    PendingIntent pendingIntent;


    public MetronomeFragment(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readTickTock();


    }

    private void readTickTock() {
        //Read Tick samples
        InputStream tick_inputStream = getResources().openRawResource(R.raw.test_sample_tick);
        BufferedReader tick_bufferedReader= new BufferedReader(new InputStreamReader(tick_inputStream));
        String tick_eachline;

        int i =0;
        try {
            tick_eachline = tick_bufferedReader.readLine();
            while (tick_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                /*String[] words = tick_eachline.split(" ");*/

                Constants.setTickValue(i,Double.parseDouble(tick_eachline));
                tick_eachline = tick_bufferedReader.readLine();
                i++;
            }
            tick_bufferedReader.close();
            tick_inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Read tock samples

        InputStream tock_inputStream = getResources().openRawResource(R.raw.test_sample_tock);
        BufferedReader tock_bufferedReader= new BufferedReader(new InputStreamReader(tock_inputStream));
        String tock_eachline = null;

        int j =0;
        try {
            tock_eachline = tock_bufferedReader.readLine();
            while (tock_eachline != null) {
                // `the words in the file are separated by space`, so to get each words
                /*String[] words = tock_eachline.split(" ");*/
                Constants.setTockValue(j,Double.parseDouble(tock_eachline));
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



                    Intent playPauseIntent = new Intent(MetronomeService.PlayPauseBroadcastReceiver.ACTION_PLAY_PAUSE);
                    playPauseIntent.putExtra(MetronomeService.PlayPauseBroadcastReceiver.PLAY_PAUSE_EXTRA,true);
                    requireActivity().sendBroadcast(playPauseIntent);
                    isPlaying = true;
                    play_pause_icon.setImageResource(R.drawable.ic_pause);



                }
                else{

                    Intent playPauseIntent = new Intent(MetronomeService.PlayPauseBroadcastReceiver.ACTION_PLAY_PAUSE);
                    playPauseIntent.putExtra(MetronomeService.PlayPauseBroadcastReceiver.PLAY_PAUSE_EXTRA,false);
                    requireActivity().sendBroadcast(playPauseIntent);
                    isPlaying = false;
                    play_pause_icon.setImageResource(R.drawable.ic_play);


                }

            }
        });



    }


    @Override
    public void onStart() {
        super.onStart();
        Intent service = new Intent(requireContext(),MetronomeService.class);
        service.putExtra("something",pendingIntent);
        requireActivity().startService(service);

    }

    @Override
    public void onBPMChange(int bpm) {

        Constants.setBpm(bpm);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent service = new Intent(requireContext(),MetronomeService.class);
        requireActivity().stopService(service);
    }


}