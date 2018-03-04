package iitg.cestrum.cbook;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 25-12-2017.
 */

public class EventBuilder extends  Event{

    public String ID;
    public String eventName;
    public String eventDate;
    public String eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;

    // EXP_RECUR_EVENTS Properties


    // Recur event properties
    private ContentValues contentValues;

    public EventBuilder(Cursor c){

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

    }

    public EventBuilder(){
        super(Event.EVENT_BUILDER);
    }

    public EventBuilder(String id, String nam, String date, String time, int duration, String venue, String courName, String prof, String credit) {
        super(Event.EVENT_BUILDER);
        this.ID =  (id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = (duration);
        this.eventDate = date;                                                      // new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.eventTime = time;                                                      //new Time(new SimpleDateFormat("HH:mm:ss").parse(time).getTime());

    }

    public EventBuilder(JSONObject j) throws JSONException {
        super(Event.EVENT_BUILDER);
        this.ID = j.getString("ID");
        this.eventName = j.getString("name");
        this.eventDate = j.getString("eventDate");
        this.eventTime = j.getString("eventTime");
        this.eventDuration = j.getInt("eventDuration");
        this.eventVenue = j.getString("eventVenue");
        this.courseName = j.getString("courseName");
        this.prof = j.getString("prof");
        this.credits = j.getString("credits");
    }

    public EventBuilder(DocumentSnapshot doc){
        super(Event.EVENT_BUILDER);
        this.ID = doc.getId();
        Map<String, Object> data = doc.getData();
        this.eventName = (String) data.get("eventName");
        this.eventDate = (String) data.get("eventDate");
        this.eventTime = (String) data.get("eventTime");
        this.eventDuration =  parseInt((String) data.get("eventDuration"));

        this.eventVenue = (String) data.get("eventVenue");
        this.courseName = (String) data.get("courseName");
        this.prof = (String) data.get("prof");
        this.credits = (String) data.get("credits");


    }

    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(this.eventDate);
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

    }


}


