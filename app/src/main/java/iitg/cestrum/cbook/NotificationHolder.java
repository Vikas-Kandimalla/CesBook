package iitg.cestrum.cbook;

/**
 * Created by vikas on 04-02-2018.
 */

public class NotificationHolder {

    public String subject,message,postedBy,timeStamp;

    public NotificationHolder(String sub, String mess, String posted, String time) {
        this.subject = sub;
        this.message=  mess;
        this.postedBy = posted;
        this.timeStamp = time;
    }

    public NotificationBuilder getNotificationBuilder(String id){
        return new NotificationBuilder(id,this.subject,this.postedBy,this.timeStamp,this.message);
    }
}
