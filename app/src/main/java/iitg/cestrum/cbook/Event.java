package iitg.cestrum.cbook;

import android.util.Log;

/**
 * Created by vikas on 26-12-2017.
 */

public class Event extends Object {
    public static final int EVENT_BUILDER =1;
    public static final int RECUR_EVENT_BUILDER =2;
    public static final int EXP_RECUR_EVENT_BUILDER =3;
    public static final int EVENT_CACHE_BUILDER = 4;
    public static final int MONTH_DISPLAY = 5;
    public static final int DATE_DISPLAY = 6;


    private int eventType;
    private String date_name;

    public Event(int type){
        this.eventType = type;
    }
    public void setType(int type){
        this.eventType = type;
    }
    public int getType(){
        return this.eventType;
    }

    public Event(String date){
        this.eventType = DATE_DISPLAY;
        this.date_name = date;
    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof  Event)
        Log.d("CBookvv","this event data = " + this.date_name + " Object date " + ((Event)o).getDate_name() );

        return o instanceof Event && this.date_name.equals(((Event) o).getDate_name());

    }

    public String getDate_name() {
        return this.date_name;
    }
}
