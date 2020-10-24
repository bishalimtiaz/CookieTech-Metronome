package com.blz.cookietech.cookietechmetronome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blz.cookietech.cookietechmetronomelibrary.MetronomeActivity;
import com.blz.cookietech.cookietechmetronomelibrary.MetronomeFragment;

public class MainActivity extends AppCompatActivity {
   // private Button go_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addMetronomeFragment();


    }

    private void addMetronomeFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MetronomeFragment metronomeFragment = new MetronomeFragment(pendingIntent);
        fragmentTransaction.add(R.id.containerView,metronomeFragment);
        fragmentTransaction.addToBackStack("metronome");
        fragmentTransaction.commit();
    }
}
