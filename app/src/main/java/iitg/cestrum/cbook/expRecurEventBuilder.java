package iitg.cestrum.cbook;

import android.content.ContentValues;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 26-12-2017.
 */

public class expRecurEventBuilder {
    public int ID;
    public String eventName;
    public String eventPrevDate;
    public String eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;
    public String eventNewDate;
    public String deleteEvent;

    private ContentValues contentValues;

    public expRecurEventBuilder(String id,String nam, String modifiedDate, String delete ,String newDate ,String newTime, String newDuration, String venue, String courName, String prof, String credit) throws ParseException {
        this.ID = parseInt(id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(newDuration);
        this.eventPrevDate =  modifiedDate;
        this.eventTime =      newTime;                              //new Time(new SimpleDateFormat("HH:mm:ss").parse(newTime).getTime());
        this.eventNewDate =    newDate;                             //new SimpleDateFormat("yyyy-MM-dd").parse(newDate);
        this.deleteEvent = delete;
    }

    public Date getEventPrevDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(this.eventPrevDate);
    }

    public Date getEventNewDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(this.eventNewDate);
    }

    public Time getEventTime() throws ParseException {
        return new Time(new SimpleDateFormat("HH:mm:ss").parse(this.eventTime).getTime());
    }

    public boolean isDeleted() {
        if ( this.deleteEvent.charAt(0) == '0')
            return false;
        else
            return true;
    }


    public ContentValues getContentValues() {
        contentValues = new ContentValues();

        contentValues.put("ID" , this.ID);
        contentValues.put("name" , this.eventName);


        contentValues.put("modifiedDate", this.eventPrevDate);
        contentValues.put("newDate",this.eventNewDate);
        contentValues.put("newTime",this.eventTime);
        contentValues.put("newDuration", this.eventDuration);
        contentValues.put("deleteEvent",this.deleteEvent);


        contentValues.put("eventVenue",this.eventVenue);
        contentValues.put("prof",this.prof);
        contentValues.put("credits",this.credits);
        contentValues.put("courseName",this.courseName);

        return contentValues;

    }


}
