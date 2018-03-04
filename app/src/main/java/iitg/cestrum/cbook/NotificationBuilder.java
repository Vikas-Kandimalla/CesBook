package iitg.cestrum.cbook;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vikas on 02-02-2018.
 */

public class NotificationBuilder {

    public String ID;
    public String subject,postedBy,timeStamp,message;
    public int isRead = 0;

    NotificationBuilder(String id, String sub, String post, String timeStamp, String message){
        this.ID = id;
        this.subject = sub;
        this.postedBy = post;
        this.timeStamp = timeStamp;
        this.message = message;

    }

    NotificationBuilder() {
    }

    NotificationBuilder(JSONObject o) throws JSONException {
        this.ID = o.getString("ID");
        this.subject = o.getString("subject");
        this.timeStamp = o.getString("timeStamp");
        this.postedBy = o.getString("postedBy");
        this.message = o.getString("message");
    }

    NotificationBuilder(Cursor c){
        this.ID = c.getString(0);
        this.subject = c.getString(1);
        this.timeStamp = c.getString(2);
        this.postedBy = c.getString(3);
        this.message = c.getString(4);
        this.isRead = c.getInt(5);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("ID",this.ID);
        values.put("subject",this.subject);
        values.put("timeStamp",this.timeStamp);
        values.put("postedBy",this.postedBy);
        values.put("message",this.message);

        return values;
    }
    public void setData(Cursor c){
        this.ID = c.getString(0);
        this.subject = c.getString(1);
        this.timeStamp = c.getString(2);
        this.postedBy = c.getString(3);
        this.message = c.getString(4);
    }

    public Calendar getCalendarTime() throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(f.parse(this.timeStamp));
        return c;
    }
}
