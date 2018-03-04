package iitg.cestrum.cbook;

/**
 * Created by vikas on 26-12-2017.
 */

public class FireStoreRecurEventHolder {
    public String ID;
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

    public FireStoreRecurEventHolder(String id, String nam, String sDate, String sTime, int duration, String eDate, int rType, int rLength, String rData, String venue, String courName, String prof, String credit) {

        this.ID = id;
        this.eventName = nam;
        this.prof = prof;
        this.credits = credit;
        this.courseName = courName;
        this.eventVenue = venue;
        this.eventDuration = (duration);
        this.recurType = (rType);
        this.recurLength = (rLength);
        this.recurData = rData;
        this.eventDate =    sDate;                                                  //new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        this.eventTime =    sTime;                                                   //new Time(new SimpleDateFormat("HH:mm:ss").parse(sTime).getTime());
        this.eventEndDate = eDate;                                                        //new SimpleDateFormat("yyyy-MM-dd").parse(eDate);


    }

    public RecurEventBuilder build(String id){
        return new RecurEventBuilder(id,this.eventName,this.eventDate,this.eventTime,this.eventDuration,this.eventEndDate,this.recurType,this.recurLength,this.recurData,this.eventVenue,this.courseName,this.prof,this.credits);
    }




}