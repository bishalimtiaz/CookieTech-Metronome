package com.blz.cookietech.cookietechmetronome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        //go_btn = findViewById(R.id.go_btn);

        /*go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MetronomeActivity.class));
                finish();
            }
        });*/

        addMetronomeFragment();
    }

    private void addMetronomeFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MetronomeFragment metronomeFragment = new MetronomeFragment();
        fragmentTransaction.add(R.id.containerView,metronomeFragment);
        fragmentTransaction.commit();
    }
}
