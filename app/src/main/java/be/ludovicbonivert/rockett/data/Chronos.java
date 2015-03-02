package be.ludovicbonivert.rockett.data;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LudovicBonivert on 04/02/15.
 * Chronos is the Data class (RocketTimer)
 * I use the same data fields as the initial ParseObject
 */

public class Chronos extends SugarRecord<Chronos> implements Comparable<Chronos>{

    Date createdAt;
    String dateFormatted;
    String task;
    double timeInSeconds;
    double timeInMinutes;
    boolean isSection;


    public Chronos(){
        // init Date object
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //createdAt = Calendar.getInstance().getTime();
        createdAt = date;
        dateFormatted = dateFormat.format(date);
    }

    public Chronos(boolean isSection){
        this.isSection = isSection;
    }



    public Chronos(Date createdAt, String task, int timeInSeconds, int timeInMinutes, String dateFormatted){
        this.createdAt = createdAt;
        this.dateFormatted = dateFormatted;
        this.task = task;
        this.timeInSeconds = timeInSeconds;
        this.timeInMinutes = timeInMinutes;

    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public double getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(double timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public double getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(double timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }


    @Override
    public int compareTo(Chronos another) {
        return getDateFormatted().compareTo(another.getDateFormatted());
    }


    public boolean getIsSection() {
        return isSection;
    }
}
