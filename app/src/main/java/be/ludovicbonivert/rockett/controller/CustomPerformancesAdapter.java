package be.ludovicbonivert.rockett.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import be.ludovicbonivert.rockett.R;
import be.ludovicbonivert.rockett.data.Chronos;

/**
 * Created by LudovicBonivert on 17/01/15.
 */
public class CustomPerformancesAdapter extends BaseAdapter{

    Context context;
    List<Chronos> items;
    LayoutInflater inflater;

    public CustomPerformancesAdapter(Context context, List<Chronos> items){
        super();
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.sort(items, new Comparator<Chronos>() {
            @Override
            public int compare(Chronos object1, Chronos object2) {
                Date a = object1.getCreatedAt();
                Date b = object2.getCreatedAt();

                if(a.after(b)){
                    return -1;
                }else if(a.equals(b)){
                    return 0;
                }else{
                    return 1;
                }

            }
        });
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        // Create the cell and populate it with the element from the list
        if(convertView == null){

            if(items.get(position) != null){
                if(items.get(position).getIsSection()){
                    convertView = inflater.inflate(R.layout.list_separator, parent, false);


                }else {
                    convertView = inflater.inflate(R.layout.list_item, parent, false);
                    // Add the timer view
                    TextView timerTextView = (TextView) convertView.findViewById(R.id.item_timer);
                    if(items.get(position).getTimeInSeconds() < 60){
                        timerTextView.setText(Math.round(items.get(position).getTimeInSeconds()) + " sec");

                    } else{
                        Math.round(items.get(position).getTimeInMinutes());
                        timerTextView.setText(Math.round(items.get(position).getTimeInMinutes()) + " min");

                    }

                    // Add the task view
                    TextView taskTextView = (TextView) convertView.findViewById(R.id.item_task);
                    taskTextView.setText(items.get(position).getTask());

                    // Add the date view
                    TextView dateTextView = (TextView) convertView.findViewById(R.id.item_date);
                    dateTextView.setText(items.get(position).getDateFormatted());

                }
            }

        }
    return convertView;
    }
}
