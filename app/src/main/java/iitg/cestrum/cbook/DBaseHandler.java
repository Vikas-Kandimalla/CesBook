package iitg.cestrum.cbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vikas on 25-12-2017.
 */

public class DBaseHandler extends SQLiteOpenHelper {

    private final static String TAG = "CBook";
    private final String dBaseName;
    private static final int dBaseVersion = 1;
    private final String[] columns = {"ID","name" , "eventData" , "eventTime" , "eventDuration" ,"eventVenue", "courseName", "prof" , "credits"};
    private EventBuilder[] eventBuilders;
    private RecurEventBuilder[] recurEventBuilders;
    private ExpRecurEventBuilder[] expRecurEventBuilders;
    private static int size=100;


    public DBaseHandler(Context context,String dbName) {
        super(context,dbName,null,this.dBaseVersion);
        //super.setWriteAheadLoggingEnabled(true);
        this.dBaseName = dbName;

    }



    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_TABLE_EVENTS = "CREATE TABLE `events` ( " +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` VARCHAR(50) NOT NULL, " +
                "`eventDate` DATE NOT NULL," +
                "`eventTime` TIME NOT NULL," +
                "`eventDuration` INT NOT NULL," +
                "`eventVenue` VARCHAR(50) DEFAULT NULL," +
                "`courseName` VARCHAR(100) DEFAULT NULL," +
                "`prof` VARCHAR(50) DEFAULT NULL," +
                "`credits` VARCHAR(50) DEFAULT NULL" +
                ");";

        String CREATE_TABLE_RECUR_EVENTS = "CREATE TABLE `recur_events` (" +
                "  `ID` int(11) PRIMARY KEY NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `eventDate` date NOT NULL," +
                "  `eventTime` time NOT NULL," +
                "  `eventDuration` int(11) NOT NULL," +
                "  `eventEndDate` date NOT NULL DEFAULT '9999-12-31'," +
                "  `recurType` int(11) NOT NULL," +
                "  `recurLength` int(11) NOT NULL," +
                "  `recurData` varchar(10) NOT NULL," +
                "  `eventVenue` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
                "  `prof` varchar(30)  DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +
                ");";


        String TABLE_EXP_RECUR_EVENTS = "CREATE TABLE `exp_recur_events` (" +
                "  `ID` int(11) NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `modifiedDate` date NOT NULL," +
                "  `newDate` date NOT NULL," +
                "  `newStartTime` time NOT NULL," +
                "  `newDuration` int(11) NOT NULL," +
                "  `deleteEvent` int(1) NOT NULL," +
                "  `eventVenue` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
                "  `prof` varchar(30) DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +
                "   CONSTRAINT myConstraint UNIQUE(`ID`,`modifiedDate`)" +
                ");";


        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_RECUR_EVENTS);
        db.execSQL(TABLE_EXP_RECUR_EVENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS `events` ");
        db.execSQL("DROP TABLE IF EXISTS `recur_events`");
        db.execSQL("DROP TABLE IF EXISTS `exp_recur_events` ");

        onCreate(db);

    }

    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS `events` ");
        db.execSQL("DROP TABLE IF EXISTS `recur_events`");
        db.execSQL("DROP TABLE IF EXISTS `exp_recur_events` ");
        db.close();
    }

    public long addEvent(EventBuilder event) {
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("events",null,event.getContentValues(),SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }

    public long addEvent(ExpRecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("exp_recur_events",null,event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }
    public long addEvent(RecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("exp_recur_events",null,event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }

    public Event getEvent( String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) throws Exception{
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        db.close();
        if (cursor == null ){
            return null;
        }
        cursor.moveToFirst();
        if(table.equals("events")){
            EventBuilder event = new EventBuilder(cursor);
            return event;
        }
        else if (table.equals("recur_events")){
            RecurEventBuilder recurEventBuilder = new RecurEventBuilder(cursor);
            return recurEventBuilder;
        }
        else if (table.equals("exp_recur_events")) {
            ExpRecurEventBuilder expRecurEventBuilder = new ExpRecurEventBuilder(cursor);
            return expRecurEventBuilder;
        }
        else return null;
    }

    public void populateEventCache(Date date) throws Exception {

        // populate the cache table for event generation
        // update the event class to add the required flags
        // table Name is event cache
        // Convert the PHP code to JAVA in this section
        // Adds only the prev 2 weeks,current week,next 2 weeks events to the table
        // if date is not in the above mentioned

        Calendar calendar = Calendar.getInstance();
        if (date != null )
            calendar.setTime(date);

        int diff = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,1-diff);
        Calendar sdate = (Calendar) calendar.clone();
        Calendar edate = (Calendar) calendar.clone();
        edate.add(Calendar.DATE,21);
        sdate.add(Calendar.DATE,-14);

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
        String eDateString = dtFormat.format(edate.getTime());
        String sDateString = dtFormat.format(sdate.getTime());





        ArrayList<EventCacheBuilder> events = new ArrayList<>(size);

        Cursor c;
        SQLiteDatabase db = this.getReadableDatabase();
        c = db.rawQuery("SELECT * FROM `events`",null);
        int i = 0;
        if (c.moveToFirst() ) {
            eventBuilders = new EventBuilder[c.getCount()];

            do{
                   eventBuilders[i++].setEventData(c);
                   events.add(new EventCacheBuilder(c));

               }while(c.moveToNext());
        }

        c = db.rawQuery("SELECT * FROM `recur_events` WHERE `eventDate`  <= " + eDateString + "AND  `eventEndDate`  >="+ sDateString +" ORDER BY `eventDate` ASC, `eventTime` ASC ",null);

        if (c.moveToFirst()){
            i = 0;
            recurEventBuilders = new RecurEventBuilder[c.getCount()];
            Calendar finalSdate,finalEdate;
            do{
                recurEventBuilders[i++].setEventData(c);

                int recurType = recurEventBuilders[i].recurType;
                int recurLength = recurEventBuilders[i].recurLength;


                // Calculate the date b/w Start and end Date
                // Set the actual start and end dates by comparing it to the object
                Calendar eventStartDate = stringToCalender(recurEventBuilders[i].eventDate);
                Calendar eventEndDate = stringToCalender(recurEventBuilders[i].eventEndDate);

                if ( sdate.compareTo(eventStartDate) > 0){         // if ( sdate > eventStartDate)
                    finalSdate = sdate;
                }
                else
                    finalSdate = eventStartDate;

                if (edate.compareTo(eventEndDate) < 0)
                    finalEdate = eventEndDate;
                else
                    finalEdate = edate;




                int sdayDiff =   (int)((finalEdate.getTimeInMillis() - finalSdate.getTimeInMillis())/(1000 * 60 * 60 * 24));
                Calendar temp;

                switch (recurType){

                    case 1:
                        // RECURRING DAY WISE
                        // use a loop
                        int iter = 0;
                        boolean flag = false;
                        while (iter < sdayDiff ){


                            if(flag) {

                                temp = (Calendar) finalSdate.clone();
                                temp.add(Calendar.DATE,iter);
                                String id = recurEventBuilders[i].ID + "_" +recurEventBuilders[i].eventDate;
                                events.add(new EventCacheBuilder(recurEventBuilders[i],id,calendarToString(temp)));
                                iter += recurLength;

                            }
                            else {

                                temp = (Calendar) finalSdate.clone();
                                temp.add(Calendar.DATE, iter);

                                if( ( (date_diff(temp,eventStartDate)) % recurLength) == 0  ){   // See if the event comes under current iteration  dates

                                    // Check for the exp recurring events
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders[i].ID + "AND `modifiedDate` = " + calendarToString(temp);
                                    Cursor t = db.rawQuery(sql,null);
                                    String id = recurEventBuilders[i].ID + "_" +recurEventBuilders[i].eventDate;
                                    if (t.moveToFirst()){
                                        // There are no exceptions for this case
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(t);
                                        events.add(new EventCacheBuilder(er,recurEventBuilders[i],id,calendarToString(temp)));

                                    }
                                    else{
                                        events.add(new EventCacheBuilder(recurEventBuilders[i],id,calendarToString(temp)));
                                    }
                                    t.close();
                                    flag = true;
                                    iter += recurLength;
                                    continue;

                                }

                                iter++;
                            }


                        }




                        break;

                    case 2:
                        // Event is recurring week type
                        // get the iter the for loop according to weeks of start and end date
                        // find the sunday closest to final start date and end date
                        // See if the current week comes under the event week and update the events list

                        Calendar eventStartSunday = (Calendar) finalSdate.clone();
                        int diffa = eventStartSunday.get(Calendar.DAY_OF_WEEK);
                        eventStartSunday.add(Calendar.DATE,1-diffa);

                        Calendar eventEndSunday = (Calendar) finalEdate.clone();
                        int diffb = eventEndSunday.get(Calendar.DAY_OF_WEEK);
                        eventEndSunday.add(Calendar.DATE,8-diffb);


                        int numofweeks = date_diff(eventEndSunday,eventStartSunday)/7;
                        int t = 0;
                        for (iter = 0 ; iter <= numofweeks ; iter++){
                            temp = (Calendar) finalSdate.clone();
                            temp.add(Calendar.DATE, iter*7);
                            if (  ((date_diff(temp,eventStartDate)/7)%recurLength) == 0){  // IF Current week is a multiple of recur length
                                // Check if the current date is in exception recur table
                                boolean[] recurData = recurEventBuilders[i].getRecurData();
                                for (int ii = 1; ii < 8;ii++){
                                    // See From Sunday to Saturday anc add events as per recurData
                                    // Check if the event end date is exceeded
                                    temp.add(Calendar.DATE,ii-1);

                                    if( (eventEndDate.getTimeInMillis() - temp.getTimeInMillis()) < 0 ){
                                        // if the date exceeds end date then stop

                                        break;
                                    }

                                    if( recurData[ii] ){
                                        // Check exp recur table for the current date.
                                        String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders[i].ID + "AND `modifiedDate` = " + calendarToString(temp);
                                        Cursor tt = db.rawQuery(sql,null);
                                        String id = recurEventBuilders[i].ID + "_" +recurEventBuilders[i].eventDate;
                                        if (tt.moveToFirst()){
                                            // There are no exceptions for this case
                                            ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                            events.add(new EventCacheBuilder(er,recurEventBuilders[i],id,calendarToString(temp)));

                                        }
                                        else{
                                            events.add(new EventCacheBuilder(recurEventBuilders[i],id,calendarToString(temp)));
                                        }
                                        tt.close();
                                    }



                                }


                            }


                        }

                        break;

                    case 3:

                        // Recurring Monthly
                        // get the start month to end month. Max months are 3
                        // see if the iter month has a event
                        int startMonth = finalSdate.get(Calendar.MONTH);
                        int eventDate = eventStartDate.get(Calendar.DATE);
                        Calendar temp1 = (Calendar) finalSdate.clone();
                        int len = month_diff(finalEdate,finalSdate);
                        for(int j = 0; j < len; j++ ){

                            if( (month_diff(temp1,eventStartDate)%recurLength == 0 ) ){

                                // validate the iter date
                                // Add an event in this loop
                                // check if there is any exception in the exp_recur_table

                                if( eventDate <= temp1.getActualMaximum(Calendar.DATE)){

                                    // Check for the exception
                                    String tempDate = String.format(Locale.ENGLISH,"%04d-%02d-%02d",temp1.get(Calendar.YEAR),temp1.get(Calendar.MONTH),eventDate);
                                    String id = recurEventBuilders[i].ID + "_" +recurEventBuilders[i].eventDate;
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders[i].ID + "AND `modifiedDate` = " + tempDate;
                                    Cursor tt = db.rawQuery(sql,null);
                                    if(tt.moveToFirst()){
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                        events.add(new EventCacheBuilder(er,recurEventBuilders[i],id,tempDate));
                                    }
                                    else{
                                        events.add(new EventCacheBuilder(recurEventBuilders[i],id,tempDate));
                                    }
                                    tt.close();

                                }

                                // else event cannot be in this month



                            }

                            temp1.add(Calendar.MONTH,1);
                        }






                        break;

                    case 4:
                        // Recurring yearly
                        Log.e(TAG," Yearly recurring type not added yet");
                        break;

                    default:
                        // Error unknown recurType
                        // Throw an exception
                        throw new Exception("Unknown Recurring Type");
                }





            }while(c.moveToNext());
        }






    }

    public Event[] getEvents(Date date){
        // Getting the list of events in date
        // Need to calculate the overlap's if present
        // Used in normal

        return null;
    }

    private Calendar stringToCalender(String date) throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(f.parse(date));

        return c;

    }

    private String calendarToString(Calendar c){
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        return dtFormat.format(c.getTime());
    }

    private int date_diff(Calendar max,Calendar min){
        return   (int)((max.getTimeInMillis() - min.getTimeInMillis())/(1000 * 60 * 60 * 24));
    }

    private int month_diff(Calendar max,Calendar min) {
        int maxMonth = max.get(Calendar.MONTH);
        int maxYear = max.get(Calendar.YEAR);
        int minMonth = min.get(Calendar.MONTH);
        int minYear = min.get(Calendar.YEAR);

        return  (maxMonth - minMonth) + ((maxYear - minYear)*(12));


    }

    private Calendar setNearestSunday(Calendar c){
        int diff = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DATE,1-diff);
        return c;
    }
    private Calendar setFarthestSunday(Calendar c){
        int diff = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DATE,8-diff);
        return c;
    }

    // Do we have to update an event??
    // most probably No


}

