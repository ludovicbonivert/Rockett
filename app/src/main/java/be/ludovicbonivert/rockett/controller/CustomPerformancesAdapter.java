package be.ludovicbonivert.rockett.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.model.MainActivity;

/**
 * Created by LudovicBonivert on 17/01/15.
 */
public class CustomPerformancesAdapter extends ParseQueryAdapter<ParseObject>{

    public CustomPerformancesAdapter(Context context, QueryFactory<ParseObject> queryFactory){

        // Use the QueryFactory to construct a PQA that will use the custom view
        super(context, queryFactory);


    }


    // customize the layout by overiding getItemView


    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {

        if(v == null){
            v = View.inflate(getContext(), R.layout.list_item, null);
        }

        super.getItemView(object, v, parent);

        // Add the timer view
        TextView timerTextView = (TextView) v.findViewById(R.id.item_timer);

        if(object.getNumber("timeInSeconds").intValue() < 60){
            timerTextView.setText(Math.round(object.getNumber("timeInSeconds").doubleValue()) + " sec");

        } else{
            Math.round(object.getNumber("timeInMinutes").doubleValue());
            timerTextView.setText(Math.round(object.getNumber("timeInMinutes").doubleValue()) + " min");

        }

        // Add the task view
        TextView taskTextView = (TextView) v.findViewById(R.id.item_task);
        taskTextView.setText(object.getString("task"));

        // Add the date view
        TextView dateTextView = (TextView) v.findViewById(R.id.item_date);

        // If we have an internetConnection we can get the online timestamp
        if(MainActivity.isConnectedToInternet){
            Date date = object.getCreatedAt();
            // format the received date to a shorter timestamp
            SimpleDateFormat formater = new SimpleDateFormat("M/d/yyyy");
            String datestring = formater.format(date);
            dateTextView.setText(datestring);
        }
        // If its not the case we take the custom date
        else{
            String datestring = object.getString("creationDate");
            dateTextView.setText(datestring);
        }
        return v;
    }
}
