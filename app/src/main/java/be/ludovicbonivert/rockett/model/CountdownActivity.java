package be.ludovicbonivert.rockett.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.controller.CountdownService;
import be.ludovicbonivert.rockett.data.Chronos;

public class CountdownActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CountdownFragment())
                    .commit();
        }
        setTitle("Rocketting");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_countdown, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CountdownFragment extends Fragment {

        private Chronometer chronoCountdown;
        private Intent intent;
        private boolean countdownPaused = false;
        private long timeWhenPaused = 0;
        private String task;
        // the receiver will receive the broadcast from the service
        public IntentFilter filter;
        public BroadcastReceiver receiver;
        public Countdown countdown;

        public CountdownFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_countdown, container, false);
            chronoCountdown = (Chronometer) rootView.findViewById(R.id.chronometerCountdown);

            // 25 minutes = Pomodoro technique = 1500000 Milliseconds
            // 1 minute = 60000 mms
            countdown = new Countdown(1501000, 1000);
            intent = getActivity().getIntent();
            filter = new IntentFilter();
            filter.addAction("Starting countdown");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // UI update here
                    // Start automatically the countdown timer
                    // Not working for the moment

                }
            };

            countdown.start();
            getActivity().registerReceiver(receiver, filter);

            createListenerOnStopButton(rootView);

            populateTextViewWithTaskFromIntent(rootView);

            // Start the countdown service
            getActivity().startService(new Intent(getActivity().getBaseContext(), CountdownService.class));

            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // When the activity is destroyed, unregister the receiver
            getActivity().unregisterReceiver(receiver);
        }

        public void createListenerOnStopButton(View rootView){
            Button stopbtn = (Button) rootView.findViewById(R.id.btn_stopCountdown);
            stopbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    // Stop the countdown service
                    getActivity().stopService(new Intent(getActivity().getBaseContext(), CountdownService.class));
                    chronoCountdown.stop();
                    countdown.cancel();

                    Chronos chrono = new Chronos();
                    long elapsedMillis = SystemClock.elapsedRealtime() - chronoCountdown.getBase();
                    double seconds = elapsedMillis / 1000.0;
                    double minutes = Math.round(seconds/60);

                    chrono.setTimeInSeconds(seconds);
                    chrono.setTimeInMinutes(minutes);
                    chrono.setTask(task);

                    chrono.save();
                    Intent backToMain = new Intent(getActivity(), MainActivity.class);
                    startActivity(backToMain);


                }
            });

        }
        public void populateTextViewWithTaskFromIntent(View rootView){
            task = intent.getExtras().getString(getString(R.string.taskParamForIntent));
            TextView taskTextView = (TextView) rootView.findViewById(R.id.taskCountdown);
            taskTextView.setText(task);


        }

        public class Countdown extends CountDownTimer{
            public Countdown(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }


            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
                chronoCountdown.setText(hms);
            }

            @Override
            public void onFinish() {
                // ALARM
                Chronos chrono = new Chronos();
                chrono.setTimeInSeconds(1500);
                chrono.setTimeInMinutes(25);
                chrono.setTask(task);
                chrono.save();

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);

                r.play();

                Intent backToMain = new Intent (getActivity(), MainActivity.class);
                startActivity(backToMain);

                // Same as clicking on stop btn
            }
        }

    }


}
