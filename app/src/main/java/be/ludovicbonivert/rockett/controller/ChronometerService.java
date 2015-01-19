package be.ludovicbonivert.rockett.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by LudovicBonivert on 18/01/15.
 */
public class ChronometerService extends Service{

    private final static String TAG = ChronometerService.class.getSimpleName();



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent local = new Intent();

        local.setAction("Starting chrono");
        this.sendBroadcast(local);

        // The service will continuely run, until we stop the chrono
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}
