package iitg.cestrum.cbook;

import java.util.Calendar;

/**
 * Created by vikas on 15-01-2018.
 */

public class EventsDayWiseUpdateHandler {

    private static Calendar nowDate = Calendar.getInstance();

    public Calendar getCurrentDate() {
        return nowDate;
    }

    public void setCurrentDate(int offset){
        nowDate.add(Calendar.DATE,offset);
    }

    public void setCurrentDate(Calendar c){
        nowDate = c;
    }

    public Calendar getDate(int offset){
        Calendar temp = (Calendar) nowDate.clone();
        temp.add(Calendar.DATE,offset);
        return temp;
    }

}
