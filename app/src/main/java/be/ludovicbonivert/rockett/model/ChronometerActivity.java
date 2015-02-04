package be.ludovicbonivert.rockett.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.controller.ChronometerService;
import be.ludovicbonivert.rockett.data.Chronos;

public class ChronometerActivity extends ActionBarActivity {

    public static final String TAG = ChronometerActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        setTitle("Rocketting");
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.main_blue));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chronometer, menu);
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
    public static class PlaceholderFragment extends Fragment {

        private Chronometer chronometer;
        private Intent intent;
        // the assigned task
        String task;
        long timeWhenPaused = 0;

        // the receiver will receive the broadcast from the service
        IntentFilter filter;
        BroadcastReceiver receiver;


        boolean chronoPaused = false;

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_chronometer, container, false);

            // receive intent from previous activity, normally settings with the task :))
            intent = getActivity().getIntent();

            filter = new IntentFilter();
            filter.addAction("Starting chrono");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // UI update here
                    // Start automatically the chronometer
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
            };

            getActivity().registerReceiver(receiver, filter);
            chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
            createListenerOnPauseButton(rootView);
            createListenerOnStopButton(rootView);
            createListenerOnRestartButton(rootView);
            populateTextViewWithTaskFromIntent(rootView);

            // Start the chronometer service
            getActivity().startService(new Intent(getActivity().getBaseContext(), ChronometerService.class));

            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // When the activity is destroyed, unregister the receiver
            getActivity().unregisterReceiver(receiver);
        }

        private void createListenerOnPauseButton(View rootview){
            Button pauseButton = (Button) rootview.findViewById(R.id.btn_pauseChrono);
            pauseButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                   /* If the chrono is paused we need to resume it */
                    if(chronoPaused){
                        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
                        chronometer.start();
                        chronoPaused = !chronoPaused;

                    } else if (!chronoPaused){
                        timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
                        chronometer.stop();
                        chronoPaused = !chronoPaused;

                    }
                }
            });
        }

        private void createListenerOnRestartButton(View rootview){

            Button restartButton = (Button) rootview.findViewById(R.id.btn_restartChrono);
            restartButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    timeWhenPaused = 0;
                    chronoPaused = false;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
            });
        }
        private void createListenerOnStopButton(View rootview){

            Button stopButton = (Button) rootview.findViewById(R.id.btn_stopChrono);
            stopButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    // Stop the chronometer service
                    getActivity().stopService(new Intent(getActivity().getBaseContext(), ChronometerService.class));

                    chronometer.stop();
                    // When the chrono stops ; create ParseObject and return to mainScreen

                    Chronos chrono = new Chronos();
                    // We first need to receive the value from the chronometer
                    long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                    double seconds = elapsedMillis / 1000.0;
                    double minutes = Math.round(seconds / 60);
                    chrono.setTimeInSeconds(seconds);
                    chrono.setTimeInMinutes(minutes);
                    chrono.setTask(task);

                    chrono.save();

                    Intent backToMain = new Intent(getActivity(), MainActivity.class);
                    startActivity(backToMain);

                }
            });
        }

        private void populateTextViewWithTaskFromIntent(View rootview){

            task = intent.getExtras().getString(getString(R.string.taskParamForIntent));
            TextView taskTextView = (TextView) rootview.findViewById(R.id.task);
            taskTextView.setText(task);

        }


    }
}
