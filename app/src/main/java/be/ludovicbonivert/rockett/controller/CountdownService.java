package be.ludovicbonivert.rockett.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by LudovicBonivert on 05/02/15.
 */
public class CountdownService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent local = new Intent();

        local.setAction("Starting countdown");
        this.sendBroadcast(local);

        // The service will continuely run, until we stop the chrono
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
