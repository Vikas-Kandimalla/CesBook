package iitg.cestrum.cbook;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 26-12-2017.
 */

public class recurEventBuilder {
    public int ID;
    public String name;
    public Date eventDate;
    public Time eventTime;
    public int eventDuration;
    public String eventVenue;
    public String courseName;
    public String prof;
    public String credits;

    public Date eventEndDate;
    public short recurType;
    public int recurLength;
    public boolean[] recurData = new boolean[7];

    public recurEventBuilder(String id, String nam, String sDate, String sTime, String duration, String eDate, short rType, int rLength, String rData, String venue, String courName, String prof, String credit) throws ParseException {


        this.ID = parseInt(id);
        this.name = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = parseInt(duration);


        this.recurType = rType;
        this.recurLength = rLength;
        for(int i = 0 ; i < rData.length() ; i++){
            if(rData.charAt(i) == '1'){
                this.recurData[i] = true;
            }
            else
                this.recurData[i] = false;
        }

        this.eventDate =   new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        this.eventTime =   new Time(new SimpleDateFormat("HH:mm:ss").parse(sTime).getTime());
        this.eventEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);


    }
}