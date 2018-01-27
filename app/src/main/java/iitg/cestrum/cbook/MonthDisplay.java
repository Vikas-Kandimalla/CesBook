package iitg.cestrum.cbook;

/**
 * Created by vikas on 07-01-2018.
 */

public class MonthDisplay extends Event {
    public String month_name;
    public String month_date;
    public String month_image = null;

    public MonthDisplay(String month, String image){
        super(Event.MONTH_DISPLAY);
        this.month_name = month;
        this.month_image = image;
    }
    public MonthDisplay(String month){
        super(Event.MONTH_DISPLAY);
        this.month_name = month;
    }

    public MonthDisplay(String month,String date,String image){
        super(Event.MONTH_DISPLAY);
        this.month_name = month;
        this.month_date = date;
        this.month_image = image;

    }
}
