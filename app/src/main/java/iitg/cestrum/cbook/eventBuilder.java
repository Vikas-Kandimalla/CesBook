package iitg.cestrum.cbook;

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
    public String name;
    public Date eventDate;
    public Time eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;

    // EXP_RECUR_EVENTS Properties
    public Date modifiedDate;
    public Date newDate;
    public Time newTime;
    public int newDuration;
    public boolean deleteEvent;

    // Recur event properties
    public Date eventEndDate;
    public short recurType;
    public int recurLength;
    public String recurData;


    public eventBuilder(String id,String nam, String date, String time, String duration, String venue, String courName, String prof, String credit) throws ParseException {

        this.ID = parseInt(id);
        this.name = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(duration);
        this.eventDate =   new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.eventTime =   new Time(new SimpleDateFormat("HH:mm:ss").parse(time).getTime());

    }

}


