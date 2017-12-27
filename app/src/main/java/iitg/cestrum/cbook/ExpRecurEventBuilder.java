package iitg.cestrum.cbook;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 26-12-2017.
 */

public class ExpRecurEventBuilder extends Event {
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

    public ExpRecurEventBuilder(String id, String nam, String modifiedDate, String newDate , String newTime, String newDuration,String delete, String venue, String courName, String prof, String credit) throws ParseException {

        super(Event.EXP_RECUR_EVENT_BUILDER);
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

    public ExpRecurEventBuilder() {
        super(Event.EXP_RECUR_EVENT_BUILDER);
    }

    public ExpRecurEventBuilder(Cursor c){
        super(Event.EXP_RECUR_EVENT_BUILDER);
        /*
        "  `ID` int(11) NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `modifiedDate` date NOT NULL," +
                "  `newDate` date NOT NULL," +
                "  `newStartTime` time NOT NULL," +
                "  `newDuration` int(11) NOT NULL," +
                "  `deleteEvent` int(1) NOT NULL," +
                "  `eventVenue` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
                "  `prof` varchar(30) DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +
         */

        this.ID = c.getInt(0);
        this.eventName = c.getString(1);
        this.eventPrevDate = c.getString(2);
        this.eventNewDate = c.getString(3);
        this.eventTime = c.getString(4);
        this.eventDuration = c.getInt(5);
        this.deleteEvent = c.getString(6);
        this.eventVenue = c.getString(7);
        this.courseName = c.getString(8);
        this.prof = c.getString(9);
        this.credits = c.getString(10);




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

    public void setEventData(Cursor c){
        this.ID = c.getInt(0);
        this.eventName = c.getString(1);
        this.eventPrevDate = c.getString(2);
        this.eventNewDate = c.getString(3);
        this.eventTime = c.getString(4);
        this.eventDuration = c.getInt(5);
        this.deleteEvent = c.getString(6);
        this.eventVenue = c.getString(7);
        this.courseName = c.getString(8);
        this.prof = c.getString(9);
        this.credits = c.getString(10);
    }


}
