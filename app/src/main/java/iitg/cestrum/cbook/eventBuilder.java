package iitg.cestrum.cbook;

import android.content.ContentValues;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 25-12-2017.
 */

public class eventBuilder {
    public int ID;
    public String eventName;
  //  public Date eventDate;
    public String eventDate;
  //  public Time eventTime;
    public String eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;

    // EXP_RECUR_EVENTS Properties


    // Recur event properties
    private ContentValues contentValues;

    public eventBuilder(String id,String nam, String date, String time, String duration, String venue, String courName, String prof, String credit) throws ParseException {

        this.ID = parseInt(id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(duration);
        this.eventDate = date;                                                      // new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.eventTime = time;                                                      //new Time(new SimpleDateFormat("HH:mm:ss").parse(time).getTime());

    }

    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(this.eventDate);
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

        return contentValues;

    }

}


