package iitg.cestrum.cbook;

import java.util.Calendar;

/**
 * Created by vikas on 15-01-2018.
 */

public class EventsDayWiseUpdateHandler {

    private static Calendar nowDate = Calendar.getInstance();
    static int prePage = 6,loopCount = 0;

    EventsDayWiseUpdateHandler() {
        nowDate = Calendar.getInstance();
        prePage = 6; loopCount = 0;
        nowDate.add(Calendar.DATE,-6);
    }

    public Calendar getCurrentDate() {
        return nowDate;
    }

    public void setCurrentDate(int offset){
        nowDate.add(Calendar.DATE,offset);
    }

    void setCurrentDate(Calendar c){
        nowDate = c;
    }

    Calendar getDate(int offset){
        Calendar temp = (Calendar) nowDate.clone();
        temp.add(Calendar.DATE,offset);
        return temp;
    }

}
