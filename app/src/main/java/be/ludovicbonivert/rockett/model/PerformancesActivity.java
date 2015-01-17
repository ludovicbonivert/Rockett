package be.ludovicbonivert.rockett.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.controller.CustomPerformancesAdapter;

public class PerformancesActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performances);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        setTitle(getString(R.string.performancesActivity));

        // Providing up navigation, back to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_performances, menu);
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

        private ParseQueryAdapter<ParseObject> mainAdapter;
        private ListView listView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_performances, container, false);
            initPerformancesListView(rootView);

            return rootView;
        }

        private void initPerformancesListView(View rootview){

            // init main ParseQueryAdapter
            //mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), "Chronos");
            mainAdapter = new CustomPerformancesAdapter(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
                @Override
                public ParseQuery<ParseObject> create() {

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chronos");
                    return query;
                }
            });
            mainAdapter.setTextKey("task");
            // Do we really need to show the seconds ? mainAdapter.setTextKey("timeInSeconds");
            mainAdapter.setTextKey("timeInMinutes");

            // init ListView
            listView = (ListView) rootview.findViewById(R.id.perf_listView);
            listView.setAdapter(mainAdapter);
            mainAdapter.loadObjects();

        }

    }
}
