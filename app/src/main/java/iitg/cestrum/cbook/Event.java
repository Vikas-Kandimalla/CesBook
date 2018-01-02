package iitg.cestrum.cbook;

/**
 * Created by vikas on 26-12-2017.
 */

public class Event {
    public static final int EVENT_BUILDER =1;
    public static final int RECUR_EVENT_BUILDER =2;
    public static final int EXP_RECUR_EVENT_BUILDER =3;
    public static final int EVENT_CACHE_BUILDER = 4;

    private int eventType;

    public Event(int type){
        this.eventType = type;
    }
    public void setType(int type){
        this.eventType = type;
    }
    public int getType(){
        return this.eventType;
    }
}
