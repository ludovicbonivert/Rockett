package be.ludovicbonivert.rockett.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.controller.SpinnerOnItemSelectedListener;

public class TimerSettingsActivity extends ActionBarActivity {

    protected final String LOG_TAG = TimerSettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_settings);

        // Providing up navigation, back to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TimerSettingsFragment())
                    .commit();

            // No idea but I need to executePendingTransactions before launching the next activity. Else the app crash ONLY in debug mode
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer_settings, menu);
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

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TimerSettingsFragment extends Fragment {

        public TimerSettingsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_timer_settings, container, false);
            populateSpinner(rootView);
            createListenerOnStartTimerButton(rootView);
            return rootView;
        }

        private void populateSpinner(View rootview){
            // We need rootview for accessing findViewById
            Spinner spinner = (Spinner) rootview.findViewById(R.id.type_of_timer_spinner);
            // Create an arrayAdapter, and populate it with the previously created string array
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.timersettings_options, android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            // Initialize the SpinnerOnItemSelectedListener-class. We could use an anonym class but we need it to get the selected
            // item in the spinner
            SpinnerOnItemSelectedListener spinnerOnItemSelectedListener = new SpinnerOnItemSelectedListener();
            spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);

        }

        private void createListenerOnStartTimerButton(final View rootview){

            final String taskParamforIntent = "taskNote";
            Button btn_startTimer = (Button) rootview.findViewById(R.id.button_startTimer);
            btn_startTimer.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            // Check first what type of timer we will launch
                            String selectedTimer = SpinnerOnItemSelectedListener.itemSelected;

                            if(selectedTimer.equals("Chronometer")){

                                Intent intentForStartingChronometer = new Intent(getActivity(), ChronometerActivity.class);
                                intentForStartingChronometer.putExtra(getString(R.string.taskParamForIntent), getTaskOfSettingsField(rootview));
                                startActivity(intentForStartingChronometer);


                            } else if(selectedTimer.equals("Pomodoro")){

                                Intent intentForStartingCountdown = new Intent(getActivity(), CountdownActivity.class);
                                intentForStartingCountdown.putExtra("taskNote", getTaskOfSettingsField(rootview));
                                startActivity(intentForStartingCountdown);
                            }

                        }
                    }
            );

        }

        private String getTaskOfSettingsField(View rootview){
            EditText taskFromSettingsActivity = (EditText) rootview.findViewById(R.id.editText_task);
            if(taskFromSettingsActivity.getText().toString().isEmpty()){
                return getString(R.string.task_for_empty_intent);
            }
            return taskFromSettingsActivity.getText().toString();
        }

    }


}
