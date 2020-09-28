package com.blz.cookietech.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blz.cookietech.cookietechmetronomelibrary.Metronome;
import com.blz.cookietech.cookietechmetronomelibrary.MetronomeFragment;
import com.blz.cookietech.cookietechmetronomelibrary.R;

import static com.blz.cookietech.Services.App.CHANNEL_ID;

public class MetronomeService extends Service {

    private static final String TAG = "MetronomeService";


    private Handler metronomeServiceHandler;
    private HandlerThread metronomeHandlerThread;
    private static Metronome metronome;


    /** onCreate will be called at the first time we create our service**/

    @Override
    public void onCreate() {
        super.onCreate();
        metronomeHandlerThread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        metronomeHandlerThread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        Looper metronomeServiceLooper = metronomeHandlerThread.getLooper();
        metronomeServiceHandler = new Handler(metronomeServiceLooper);
    }


    /**This method will triggered when we start a service**/
    /** This method will be triggered every time we call startService*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        metronome = Metronome.getInstance();
        Log.d(TAG, "onStartCommand: tick count: " + metronome.getTickCount());



        Intent notificationIntent = new Intent(this, MetronomeFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_wave)
                .setContentTitle("Example Service")
                .setContentText("This is a Demo Metronome")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();
        //notificationManagerCompat.notify(1,notification);

        /** Id is the identifier for notification for later update **/

        startForeground(1,notification);

        /** we can also stop service here **/

        //do heavy work in the background thread
        metronomeServiceHandler.post(new MetronomeRunnable());
        //stopSelf();

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        metronome.stop();
        metronomeHandlerThread.quit();




    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static class MetronomeRunnable implements Runnable{


        @Override
        public void run() {

            metronome.playPublic();


        }
    }
}
