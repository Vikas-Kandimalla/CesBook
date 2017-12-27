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

public class RecurEventBuilder extends Event {
    public int ID;
    public String eventName;
    public String eventDate;
    public String eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;
    public String eventEndDate;
    public int recurType;
    public int recurLength;
    public String recurData;

    private ContentValues contentValues;

    public RecurEventBuilder(String id, String nam, String sDate, String sTime, String duration, String eDate, String rType, String rLength, String rData, String venue, String courName, String prof, String credit) throws ParseException {
        super(Event.RECUR_EVENT_BUILDER);
        this.ID = parseInt(id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(duration);
        this.recurType = parseInt(rType);
        this.recurLength = parseInt(rLength);
        this.recurData = rData;
        this.eventDate =    sDate;                                                  //new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        this.eventTime =    sTime;                                                   //new Time(new SimpleDateFormat("HH:mm:ss").parse(sTime).getTime());
        this.eventEndDate = eDate;                                                        //new SimpleDateFormat("yyyy-MM-dd").parse(eDate);


    }
    public RecurEventBuilder() {
        super(Event.RECUR_EVENT_BUILDER);
    }

    public RecurEventBuilder(Cursor c){
        super(Event.RECUR_EVENT_BUILDER);

        /*

        CREATE TABLE `recur_events` (" +
                "  `ID` int(11) PRIMARY KEY NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `eventDate` date NOT NULL," +
                "  `eventTime` time NOT NULL," +
                "  `eventDuration` int(11) NOT NULL," +
                "  `eventEndDate` date NOT NULL DEFAULT '9999-12-31'," +
                "  `recurType` int(11) NOT NULL," +
                "  `recurLength` int(11) NOT NULL," +
                "  `recurData` varchar(10) NOT NULL," +
                "  `eventVenue` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
                "  `prof` varchar(30)  DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +

         */

        this.ID = c.getInt(0);
        this.eventName = c.getString(1);
        this.eventDate = c.getString(2);
        this.eventTime = c.getString(3);
        this.eventDuration = c.getInt(4);
        this.eventEndDate = c.getString(5);
        this.recurType = c.getInt(6);
        this.recurLength = c.getInt(7);
        this.recurData = c.getString(8);
        this.eventVenue = c.getString(9);
        this.courseName = c.getString(10);
        this.prof = c.getString(11);
        this.credits = c.getString(12);

    }

    public Date getEventDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(this.eventDate);
    }

    public Date getEventEndDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(this.eventEndDate);
    }
    public Time getEventTime() throws ParseException {
        return new Time(new SimpleDateFormat("HH:mm:ss").parse(this.eventTime).getTime());
    }
    public boolean[] getRecurData() {
        boolean[] ret = new boolean[7];
        for(int i = 0 ; i < this.recurData.length() ; i++){
            if(this.recurData.charAt(i) == '1'){
                ret[i] = true;
            }
            else
                ret[i] = false;
        }
        return ret;
    }

    public ContentValues getContentValues() {
        contentValues = new ContentValues();
        contentValues.put("ID" , this.ID);
        contentValues.put("name" , this.eventName);
        contentValues.put("eventDate", this.eventDate);
        contentValues.put("eventTime",this.eventTime);
        contentValues.put("eventDuration", this.eventDuration);

        contentValues.put("eventEndDate",this.eventEndDate);
        contentValues.put("recurData",this.recurData);
        contentValues.put("recurType",this.recurType);
        contentValues.put("recurLength",this.recurLength);

        contentValues.put("eventVenue",this.eventVenue);
        contentValues.put("prof",this.prof);
        contentValues.put("credits",this.credits);
        contentValues.put("courseName",this.courseName);

        return contentValues;

    }

    public void setEventData(Cursor c){
        this.ID = c.getInt(0);
        this.eventName = c.getString(1);
        this.eventDate = c.getString(2);
        this.eventTime = c.getString(3);
        this.eventDuration = c.getInt(4);
        this.eventEndDate = c.getString(5);
        this.recurType = c.getInt(6);
        this.recurLength = c.getInt(7);
        this.recurData = c.getString(8);
        this.eventVenue = c.getString(9);
        this.courseName = c.getString(10);
        this.prof = c.getString(11);
        this.credits = c.getString(12);
    }


}