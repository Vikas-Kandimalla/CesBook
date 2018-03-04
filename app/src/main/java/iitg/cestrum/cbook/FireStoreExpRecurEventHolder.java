package iitg.cestrum.cbook;

import java.text.ParseException;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 26-12-2017.
 */

public class FireStoreExpRecurEventHolder {
    public String ID;
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



    public FireStoreExpRecurEventHolder(String id, String nam, String modifiedDate, String newDate , String newTime, String newDuration, String delete, String venue, String courName, String prof, String credit) throws ParseException {

        this.ID = id;
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

    public FireStoreExpRecurEventHolder() {
    }

}
