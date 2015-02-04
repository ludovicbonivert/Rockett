package be.ludovicbonivert.rockett.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

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

        return convertView;
    }
}
