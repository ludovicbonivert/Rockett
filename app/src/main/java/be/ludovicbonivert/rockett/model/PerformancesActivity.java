package be.ludovicbonivert.rockett.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.controller.CustomPerformancesAdapter;
import be.ludovicbonivert.rockett.data.Chronos;

public class PerformancesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performances);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PerformancesFragment())
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
        if (id == R.id.wipe_data) {
            createConfirmationDialog();

            return true;
        }

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createConfirmationDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.askConfirmation);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.acceptWipe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Chronos.deleteAll(Chronos.class);
                ListView listview = (ListView) findViewById(R.id.perf_listView);
                listview.setAdapter(null);
            }
        });

        alertDialog.setNegativeButton(R.string.denieWipe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        //alertDialog.create().show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.WHITE);

        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.blue_800));

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PerformancesFragment extends Fragment {

        private ListView listView;
        public List<Chronos> chronos = Chronos.listAll(Chronos.class);
        // Adapter needs context so getActivity is maybe not the good one to give
        private CustomPerformancesAdapter mainAdapter;
        public Runnable run;

        // holding the current task the user is clicking in the listview
        public int currentTaskId;

        // get the selected object to remove
        static Chronos selectedobject;

        public PerformancesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_performances, container, false);
            initPerformancesListView(rootView);
            registerForContextMenu(listView);
            return rootView;
        }

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            currentTaskId = info.position;
            menu.add(getString(R.string.delete));
        }

        // User have selected an item. Now delete it
        @Override
        public boolean onContextItemSelected(MenuItem item) {

            // First we need to retrieve the parse object from the listview and query the server what's that object there
            selectedobject = (Chronos)listView.getItemAtPosition(currentTaskId);

            // reload the adapter. I implemented a runOnUiThread method, else this is not live refreshing
            run = new Runnable(){
                public void run(){
                    // reload content
                    selectedobject.delete();
                    chronos.clear();
                    chronos.addAll(Chronos.listAll(Chronos.class));
                    mainAdapter.notifyDataSetChanged();
                    listView.invalidateViews();
                    listView.refreshDrawableState();

                }

            };
            getActivity().runOnUiThread(run);
            return super.onContextItemSelected(item);
        }

        private void initPerformancesListView(View rootview){
            //mainAdapter = new CustomPerformancesAdapter(getActivity(), chronos);
            mainAdapter = new CustomPerformancesAdapter(getActivity(), chronos);
            // init ListView
            listView = (ListView) rootview.findViewById(R.id.perf_listView);

            // Create separator
            /*for(int i = 0; i < chronos.size(); i++){
                // Ici on doit check chaque date

                if(i + 1 < chronos.size()){

                    if(chronos.get(i).getCreatedAt().compareTo(chronos.get(i+1).getCreatedAt()) == 0){
                        Log.e("Performances", "The same");
                    }else if(chronos.get(i).getCreatedAt().compareTo(chronos.get(i+1).getCreatedAt()) < 0){
                        Log.e("Performances", "Before");
                    }else if(chronos.get(i).getCreatedAt().compareTo(chronos.get(i+1).getCreatedAt()) > 0){
                        Log.e("Performances", "After");
                    }
                }
            }*/

            listView.setAdapter(mainAdapter);
        }
    }

}
