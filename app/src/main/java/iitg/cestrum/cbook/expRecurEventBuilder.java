package iitg.cestrum.cbook;

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
    public Date eventPrevDate;
    public Time eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;
    public Date eventNewDate;
    public boolean deleteEvent;

    public expRecurEventBuilder(String id,String nam, String modifiedDate, boolean delete ,String newDate ,String newTime, String newDuration, String venue, String courName, String prof, String credit) throws ParseException {
        this.ID = parseInt(id);
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(newDuration);
        this.eventPrevDate =  new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDate);
        this.eventTime =   new Time(new SimpleDateFormat("HH:mm:ss").parse(newTime).getTime());
        this.eventNewDate =   new SimpleDateFormat("yyyy-MM-dd").parse(newDate);
        this.deleteEvent = delete;
    }


}
