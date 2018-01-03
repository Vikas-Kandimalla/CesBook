package iitg.cestrum.cbook;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 28-12-2017.
 */

public class EventCacheBuilder extends Event {



    public String ID;
    public String eventName;
    public String eventDate;
    public String eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;

    public int parentID,parentRecurType,parentRecurLength;
    public String parentRecurData;
    public int isModified;

    // Parent properties if the event if a recur event Properties



    private ContentValues contentValues;


    public EventCacheBuilder(){
        super(Event.EVENT_CACHE_BUILDER);
    }

    public EventCacheBuilder(Cursor c){

        /*
        "`ID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` VARCHAR(50) NOT NULL, " +
                "`eventDate` DATE NOT NULL," +
                "`eventTime` TIME NOT NULL," +
                "`eventDuration` INT NOT NULL," +
                "`eventVenue` VARCHAR(50) DEFAULT NULL," +
                "`courseName` VARCHAR(100) DEFAULT NULL," +
                "`prof` VARCHAR(50) DEFAULT NULL," +
                "`credits` VARCHAR(50) DEFAULT NULL" +
        */

        super(Event.EVENT_BUILDER);
        this.ID = c.getString(0);
        this.eventName = c.getString(1);
        this.eventDate = c.getString(2);
        this.eventTime = c.getString(3);
        this.eventDuration =  c.getInt(4);
        this.eventVenue = c.getString(5);
        this.courseName = c.getString(6);
        this.prof = c.getString(7);
        this.credits = c.getString(8);

        this.parentID = -1;
        this.parentRecurType = -1;
        this.parentRecurLength = -1;
        this.parentRecurData = null;
        this.isModified = 0;

    }

    public EventCacheBuilder(RecurEventBuilder r, String id, String date) {
        super(Event.EVENT_CACHE_BUILDER);
        this.ID = id;
        this.eventName = r.eventName;
        this.eventDate = date;
        this.eventTime = r.eventTime;
        this.eventDuration = r.eventDuration;
        this.eventVenue = r.eventVenue;
        this.courseName = r.courseName;
        this.prof = r.prof;
        this.credits = r.credits;

        this.parentID = r.ID;
        this.parentRecurType = r.recurType;
        this.parentRecurLength = r.recurLength;
        this.parentRecurData = r.recurData;
        this.isModified = 0;

    }

    public EventCacheBuilder(ExpRecurEventBuilder e,RecurEventBuilder r,String id,String date){
        super(Event.EVENT_CACHE_BUILDER);

        this.ID = id;
        this.eventName = e.eventName;
        this.eventDate = e.eventNewDate;
        this.eventTime = e.eventTime;
        this.eventDuration = e.eventDuration;
        this.eventVenue = e.eventVenue;
        this.courseName = e.courseName;
        this.prof = e.prof;
        this.credits = e.credits;

        this.parentID = r.ID;
        this.parentRecurType = r.recurType;
        this.parentRecurLength = r.recurLength;
        this.parentRecurData = r.recurData;
        this.isModified = 1;
    }


    public EventCacheBuilder(String id, String nam, String date, String time, String duration, String venue, String courName, String prof, String credit) throws ParseException {
        super(Event.EVENT_BUILDER);
        this.ID = (id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(duration);
        this.eventDate = date;                                                      // new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.eventTime = time;                                                      //new Time(new SimpleDateFormat("HH:mm:ss").parse(time).getTime());
        this.parentID = -1;
        this.parentRecurType = -1;
        this.parentRecurLength = -1;
        this.parentRecurData = null;
        this.isModified = 0;
    }

    public Time getTime() throws ParseException {
        return new Time(new SimpleDateFormat("HH:mm:ss").parse(this.eventTime).getTime());
    }

    public ContentValues getContentValues() {
        contentValues = new ContentValues();
        contentValues.put("ID" , this.ID);
        contentValues.put("name" , this.eventName);
        contentValues.put("eventDate", this.eventDate);
        contentValues.put("eventTime",this.eventTime);
        contentValues.put("eventDuration", this.eventDuration);
        contentValues.put("eventVenue",this.eventVenue);
        contentValues.put("prof",this.prof);
        contentValues.put("credits",this.credits);
        contentValues.put("courseName",this.courseName);

        contentValues.put("parentID",this.parentID);
        contentValues.put("parentRecurType",this.parentRecurType);
        contentValues.put("parentRecurLength",this.parentRecurLength);
        contentValues.put("parentRecurData",this.parentRecurData);
        contentValues.put("isModified",this.isModified);

        return contentValues;

    }

    public void setEventData(Cursor c){

        this.ID = c.getString(0);
        this.eventName = c.getString(1);
        this.eventDate = c.getString(2);
        this.eventTime = c.getString(3);
        this.eventDuration =  c.getInt(4);
        this.eventVenue = c.getString(5);
        this.courseName = c.getString(6);
        this.prof = c.getString(7);
        this.credits = c.getString(8);
        this.parentID = c.getInt(9);
        this.parentRecurType = c.getInt(10);
        this.parentRecurLength = c.getInt(11);
        this.parentRecurData = c.getString(12);
        this.isModified = c.getInt(13);


    }



}
