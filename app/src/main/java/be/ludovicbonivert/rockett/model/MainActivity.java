package be.ludovicbonivert.rockett.model;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.data.Chronos;


/*
*
*  Rockett is an app created by Ludovic Bonivert
*  Some of the assets used in the app comes from the Android Asset Studio made by Roman Nurik
*
* */

public class MainActivity extends ActionBarActivity {

    public static boolean isConnectedToInternet = false;
    public static boolean isConnectedToWifi = false;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TimerInfoFragment())
                    .commit();
        }

        /*
        Locale locale = new Locale("nl");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_addTimer) {
            createNewTimer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewTimer() {
        Intent intentForTimerSettings = new Intent(this, TimerSettingsActivity.class);
        startActivity(intentForTimerSettings);
    }

    private void getInternetStatusOfDevice() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnectedToInternet = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnectedToInternet) {
            isConnectedToWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
    }

    /**
     * TimeInfoFragment containing the components of the main screen
     */
    public static class TimerInfoFragment extends Fragment {

        double totalProductivityMinutes = 0;
        // The amountOfParseObjects will help me calculating the average productivity
        int amountOfChronosObjects;

        public TimerInfoFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            createListenerOnViewPerformancesButton(rootView);
            getTotalProductivityMinutes(rootView);
            getDailyRocketts(rootView);
            return rootView;
        }

        protected void createListenerOnViewPerformancesButton(View rootview) {

            Button viewPerformances = (Button) rootview.findViewById(R.id.button_viewPerfs);
            viewPerformances.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent viewPerformances = new Intent(getActivity(), PerformancesActivity.class);
                    startActivity(viewPerformances);
                }
            });
        }

        protected void getTotalProductivityMinutes(final View rootview) {

            final TextView totalMinutesMain = (TextView) rootview.findViewById(R.id.main_timer);

            // Using a local datastore, we dont care now if connected or not
            List<Chronos> chronos = Chronos.listAll(Chronos.class);
            for (int i = 0, j = chronos.size(); i < j; i++) {
                if (i == 0) {
                    amountOfChronosObjects = chronos.size();
                }
                totalProductivityMinutes += chronos.get(i).getTimeInMinutes();
            }

            totalMinutesMain.setText(String.valueOf(Math.round(totalProductivityMinutes)));
            // We need to call the converter AFTER the parsing is done.
            //convertTotalProductivityMinutesToRocketts(rootview);
            calculateAverageProductivity(rootview);

        }

        private void getDailyRocketts(View rootview){
            // Get all the objects
            List<Chronos> chronos = Chronos.listAll(Chronos.class);
            int todaysRocketts = 0;
            for (int i = 0, j = chronos.size(); i < j; i++) {
                if (i == 0) {
                    amountOfChronosObjects = chronos.size();
                }
                // If todays date is equal to a rockett's date, add it up to the today's counter
                if(DateUtils.isToday(chronos.get(i).getCreatedAt().getTime())){
                   todaysRocketts += Math.round(chronos.get(i).getTimeInMinutes() / 25);
                }
            }

            TextView totalRocketts = (TextView) rootview.findViewById(R.id.total_rocketts);
            totalRocketts.setText(String.valueOf(Math.round(todaysRocketts)));

        }

        protected void convertTotalProductivityMinutesToRocketts(View rootview) {

            TextView totalRocketts = (TextView) rootview.findViewById(R.id.total_rocketts);
            totalRocketts.setText(String.valueOf(Math.round(totalProductivityMinutes) / 25));

        }

        protected void calculateAverageProductivity(View rootview) {

            TextView averageProductivity = (TextView) rootview.findViewById(R.id.average_productivity);
            averageProductivity.setText(String.valueOf(Math.round(totalProductivityMinutes / amountOfChronosObjects)));

        }

    }

}

