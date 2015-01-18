package be.ludovicbonivert.rockett.controller;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by LudovicBonivert on 13/01/15.
 */
public class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener{

    public static String itemSelected;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getItemSelected(){
        if(itemSelected == null){
            return "No item selected ! ";
        } else{
            return itemSelected;
        }
    }

}
