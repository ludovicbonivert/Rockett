package be.ludovicbonivert.rockett.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.ludovicbonivert.rockett.R;


/*
*
*  Rockett is an app created by Ludovic Bonivert
*  Some of the assets used in the app comes from the Android Asset Studio made by Roman Nurik
*
* */

public class MainActivity extends ActionBarActivity {

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

        if(id == R.id.action_addTimer){
            createNewTimer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewTimer(){
        Intent intentForTimerSettings = new Intent(this, TimerSettingsActivity.class);
        startActivity(intentForTimerSettings);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TimerInfoFragment extends Fragment {

        public TimerInfoFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            createListenerOnViewPerformancesButton(rootView);
            return rootView;
        }

        protected void createListenerOnViewPerformancesButton(View rootview){

            Button viewPerformances = (Button) rootview.findViewById(R.id.button_viewPerfs);
            viewPerformances.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent viewPerformances = new Intent(getActivity(), PerformancesActivity.class);
                    startActivity(viewPerformances);
                }
            });


        }


    }
}
