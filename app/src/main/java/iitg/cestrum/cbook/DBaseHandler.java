package iitg.cestrum.cbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by vikas on 25-12-2017.
 * Database files for events
 *
 */

public class DBaseHandler extends SQLiteOpenHelper {

    final static int eventGenerateRange = 42;

    private final static String TAG = "DBaseHandler";
    private final static String dBaseName = "localDB";
    private static final int dBaseVersion = 1;

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE `events` ( " +
            "`ID` VARCHAR(255) PRIMARY KEY, " +
            "`name` VARCHAR(50) NOT NULL, " +
            "`eventDate` DATE NOT NULL," +
            "`eventTime` TIME NOT NULL," +
            "`eventDuration` INT NOT NULL," +
            "`eventVenue` VARCHAR(50) DEFAULT NULL," +
            "`courseName` VARCHAR(100) DEFAULT NULL," +
            "`prof` VARCHAR(50) DEFAULT NULL," +
            "`credits` VARCHAR(50) DEFAULT NULL" +
            ");";

    private static final String CREATE_TABLE_RECUR_EVENTS = "CREATE TABLE `recur_events` (" +
            "  `ID` VARCHAR PRIMARY KEY NOT NULL," +
            "  `name` varchar(255) NOT NULL," +
            "  `eventDate` date NOT NULL," +
            "  `eventTime` time NOT NULL," +
            "  `eventDuration` int(11) NOT NULL," +
            "  `eventEndDate` date NOT NULL DEFAULT '9999-12-31'," +
            "  `recurType` INTEGER NOT NULL," +
            "  `recurLength` INTEGER NOT NULL," +
            "  `recurData` varchar(10) NOT NULL," +
            "  `eventVenue` varchar(10) DEFAULT NULL," +
            "  `courseName` varchar(255) DEFAULT NULL," +
            "  `prof` varchar(30)  DEFAULT NULL," +
            "  `credits` varchar(10) DEFAULT NULL" +
            ");";


    private static final String TABLE_EXP_RECUR_EVENTS = "CREATE TABLE `exp_recur_events` (" +
            "  `ID` VARCHAR NOT NULL," +
            "  `name` varchar(255) NOT NULL," +
            "  `modifiedDate` date NOT NULL," +
            "  `newDate` date NOT NULL," +
            "  `newStartTime` time NOT NULL," +
            "  `newDuration` INTEGER NOT NULL," +
            "  `deleteEvent` INTEGER NOT NULL," +
            "  `eventVenue` varchar(10) DEFAULT NULL," +
            "  `courseName` varchar(255) DEFAULT NULL," +
            "  `prof` varchar(30) DEFAULT NULL," +
            "  `credits` varchar(10) DEFAULT NULL," +
            "   CONSTRAINT myConstraint UNIQUE(`ID`,`modifiedDate`)" +
            ");";

    static final String[] months = new String[]{"January","February","March","April","May","June","July","August","Sepetember","October","November","December"};
    static final String[] mon = new String[]{"Jan","Feb","Mar","Apr" ,"May", "Jun" ,"Jul" , "Aug" , "Sep" , "Oct", "Nov" , "Dec"};

    DBaseHandler(Context context) {
        super(context,dBaseName,null,dBaseVersion);
        super.setWriteAheadLoggingEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db){


        /*String TABLE_EVENTS_CACHE = "CREATE TABLE `events_cache` (" +
                "   `ID` varchar(30) PRIMARY KEY NOT NULL," +
                "   `name` VARCHAR(50) NOT NULL, " +
                "   `eventDate` DATE NOT NULL," +
                "   `eventTime` TIME NOT NULL," +
                "   `eventDuration` INT NOT NULL," +
                "   `eventVenue` VARCHAR(50) DEFAULT NULL," +
                "   `courseName` VARCHAR(100) DEFAULT NULL," +
                "   `prof` VARCHAR(50) DEFAULT NULL," +
                "   `credits` VARCHAR(50) DEFAULT NULL," +
                "   `parentID` INTEGER NOT NULL," +
                "   `parentRecurType` INTEGER NOT NULL," +
                "   `parentRecurLength` INTEGER NOT NULL," +
                "   `parentRecurData` VARCHAR(10) DEFAULT NULL," +
                "   `isModified` INTEGER DEFAULT 0" +
                "    );";*/


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

    private void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS `events` ");
        db.execSQL("DROP TABLE IF EXISTS `recur_events`");
        db.execSQL("DROP TABLE IF EXISTS `exp_recur_events` ");
        db.close();
    }

    public void resetTables() {
        deleteTables();
        createTables();
    }

    private void createTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_RECUR_EVENTS);
        db.execSQL(TABLE_EXP_RECUR_EVENTS);
        db.close();
    }

    public long addEvent(EventBuilder event) {
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("events",null,event.getContentValues(),SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }

    public long addExpRecurEvent(ExpRecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("exp_recur_events",null,event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }

    public long addRecurEvent(RecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("recur_events",null,event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }


    public Event getEvent( String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        db.close();
        if (cursor == null ){
            return null;
        }
        cursor.moveToFirst();
        Event event;
        switch (table) {
            case "events":
                    event = new EventBuilder(cursor);
                    break;
            case "recur_events":
                    event = new RecurEventBuilder(cursor);
                    break;
            case "exp_recur_events":
                    event = new ExpRecurEventBuilder(cursor);
                    break;
            default:
                    event = null;
                    break;
        }
        cursor.close();
        return event;
    }

    private Cursor query(String sql){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql,null);


    }

    synchronized ArrayList<Event> generateEvents(Calendar calendar) throws Exception {

        ArrayList<EventCacheBuilder> events = new ArrayList<>(100);

        if (calendar == null )
             calendar = Calendar.getInstance();

        int diff = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,1-diff);
        Calendar sdate = (Calendar) calendar.clone();
        Calendar edate = (Calendar) calendar.clone();
        edate.add(Calendar.DATE,DBaseHandler.eventGenerateRange);
        sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -1);

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String eDateString = dtFormat.format(edate.getTime());
        String sDateString = dtFormat.format(sdate.getTime());



        String sql1 = "SELECT * FROM `events` WHERE `eventDate` >= \""+ sDateString + "\"AND `eventDate` <= \""+ eDateString + "\" ORDER BY `eventDate` ASC, `eventTime` ASC, `eventDuration` DESC ";
        Cursor  c1 = query(sql1);
        int i;
        if (c1.moveToFirst() ) {

            do{
                events.add(new EventCacheBuilder(c1));
            }while(c1.moveToNext());
        }
        c1.close();

        Cursor c = query("SELECT * FROM `recur_events` WHERE `eventDate`  <= \"" + eDateString + "\"AND  `eventEndDate`  >= \""+ sDateString +"\" ORDER BY `eventDate` ASC, `eventTime` ASC ");

        if (c.moveToFirst()){

            Calendar finalSdate,finalEdate;
            do{
                RecurEventBuilder recurEventBuilders = new RecurEventBuilder(c);

                int recurType = recurEventBuilders.recurType;
                int recurLength = recurEventBuilders.recurLength;


                // Calculate the date b/w Start and end Date
                // Set the actual start and end dates by comparing it to the object
                Calendar eventStartDate = stringToCalender(recurEventBuilders.eventDate);
                Calendar eventEndDate = stringToCalender(recurEventBuilders.eventEndDate);

                if ( sdate.compareTo(eventStartDate) > 0){         // if ( sdate > eventStartDate)
                    finalSdate = sdate;
                }
                else
                    finalSdate = eventStartDate;

                if (edate.compareTo(eventEndDate) > 0)
                    finalEdate = eventEndDate;
                else
                    finalEdate = edate;


                // Log.d("NewDebug","sDateString = " + sDateString + ", eDateString = " + eDateString + ", eventStartDate = " + recurEventBuilders.eventDate + ", eventEndDate = " + recurEventBuilders.eventEndDate + " finalSdate = " + calendarToString(finalSdate) + ", finalEdate = " + calendarToString(finalEdate));


                int sdayDiff =   (int)((finalEdate.getTimeInMillis() - finalSdate.getTimeInMillis())/(1000 * 60 * 60 * 24));
                Calendar temp;

                switch (recurType){

                    case 1:
                        // RECURRING DAY WISE
                        // use a loop
                        int iter = 0;
                        Cursor t1;
                        boolean flag = false;
                        while (iter < sdayDiff ){


                            if(flag) {

                                temp = (Calendar) finalSdate.clone();
                                temp.add(Calendar.DATE,iter);
                                String tempDate = calendarToString(temp);
                                String id = recurEventBuilders.ID + "_" + tempDate;
                                //  Log.d("NewDebug","This loop is executed : " + id + " iter : " + iter + " date : " +calendarToString(AgendaViewEndless));
                                String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " + "AND `modifiedDate` = " + calendarToString(temp);
                                t1 = query(sql);

                                if (t1.moveToFirst()){
                                    // There are no exceptions for this case
                                    ExpRecurEventBuilder er = new ExpRecurEventBuilder(t1);
                                    events.add(new EventCacheBuilder(er, recurEventBuilders,id,tempDate));


                                }
                                else{

                                    events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                }
                                t1.close();




                                iter += recurLength;

                            }
                            else {

                                temp = (Calendar) finalSdate.clone();
                                temp.add(Calendar.DATE, iter);

                                if( ( (date_diff(temp,eventStartDate)) % recurLength) == 0  ){   // See if the event comes under current iteration  dates

                                    // Check for the exp recurring events
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " + "AND `modifiedDate` = " + calendarToString(temp);
                                    Cursor t = query(sql);
                                    String tempDate = calendarToString(temp);
                                    String id = recurEventBuilders.ID + "_" +tempDate;
                                    if (t.moveToFirst()){
                                        // There are no exceptions for this case
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(t);

                                        events.add(new EventCacheBuilder(er, recurEventBuilders,id,tempDate));


                                    }
                                    else{

                                        events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                    }
                                    t.close();
                                    flag = true;
                                    //   Log.d("NewDebug : ", id + " iter : " + iter + " date : " +calendarToString(AgendaViewEndless) );
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

                        Calendar eventStartDateSunday = (Calendar) eventStartDate.clone();
                        int diffC = eventStartDateSunday.get(Calendar.DAY_OF_WEEK);
                        eventStartDateSunday.add(Calendar.DATE,1-diffC);


                        //  Log.d("NewDebug" , "eventStartSunday = " + calendarToString(eventStartSunday));

                        int numofweeks = date_diff(eventEndSunday,eventStartSunday)/7;
                        for (iter = 0 ; iter <= numofweeks ; iter++){
                            temp = (Calendar) eventStartSunday.clone();
                            temp.add(Calendar.DATE, iter*7);
                            if (  ((date_diff(temp,eventStartDateSunday)/7)%recurLength) == 0 ){  // IF Current week is a multiple of recur length
                                // Check if the current date is in exception recur table
                                boolean[] recurData = recurEventBuilders.getRecurData();
                                for (int ii = 1; ii < 8;ii++){
                                    // See From Sunday to Saturday anc add events as per recurData
                                    // Check if the event end date is exceeded


                                    if( (eventEndDate.getTimeInMillis() - temp.getTimeInMillis()) < 0 ){
                                        // if the date exceeds end date then stop

                                        break;
                                    }

                                    if( recurData[ii-1] && ((temp.getTimeInMillis() - eventStartDate.getTimeInMillis()) >= 0) ){
                                        // Check exp recur table for the current date.
                                        String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID +" "+ "AND `modifiedDate` = " + calendarToString(temp);
                                        Cursor tt = query(sql);
                                        String tempDate  =  calendarToString(temp);
                                        String id = recurEventBuilders.ID + "_" + tempDate;
                                        if (tt.moveToFirst()){
                                            // There are no exceptions for this case
                                            ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                            events.add( new EventCacheBuilder(er, recurEventBuilders,id,tempDate));


                                        }
                                        else{

                                            events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                        }
                                        tt.close();
                                    }


                                    temp.add(Calendar.DATE,1);
                                }


                            }


                        }

                        break;

                    case 3:

                        // Recurring Monthly
                        // get the start month to end month. Max months are 3
                        // see if the iter month has a event
                        int eventDate = eventStartDate.get(Calendar.DATE);
                        Calendar temp1 = (Calendar) finalSdate.clone();
                        int len = month_diff(finalEdate,finalSdate);
                        //     Log.d("NewDebug" , "Length of difference = " +len);
                        for(int j = 0; j <= len; j++ ){

                            if( (month_diff(temp1,eventStartDate)%recurLength == 0 ) ){

                                // validate the iter date
                                // Add an event in this loop
                                // check if there is any exception in the exp_recur_table

                                if( eventDate <= temp1.getActualMaximum(Calendar.DATE)){

                                    // Check for the exception
                                    String tempDate = String.format(Locale.ENGLISH,"%04d-%02d-%02d",temp1.get(Calendar.YEAR),(temp1.get(Calendar.MONTH) + 1),eventDate);
                                    String id = recurEventBuilders.ID + "_" +tempDate;
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " +"AND `modifiedDate` = " + tempDate;
                                    Cursor tt = query(sql);
                                    if(tt.moveToFirst()){
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                        events.add(new EventCacheBuilder(er, recurEventBuilders,id,tempDate));

                                    }
                                    else{
                                        events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

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

        events.trimToSize();
        int size = events.size();

        EventCacheBuilder[] ec =  events.toArray(new EventCacheBuilder[size]);
        Arrays.sort(ec,new Comparator<EventCacheBuilder>(){

            @Override
            public int compare(EventCacheBuilder max, EventCacheBuilder min) {

                int compare =  max.eventDate.compareTo(min.eventDate);

                if(compare > 0){
                    return 1;
                }
                else if(compare < 0){
                    return -1;
                }
                else{

                    int ct = max.eventTime.compareTo(min.eventTime);
                    if( ct > 0){
                        return 1;
                    }
                    else if( ct < 0){
                        return 1;
                    }
                    else{
                        int a = max.eventDuration - min.eventDuration;
                        if(a > 0){
                            return 1;
                        }
                        else if(a < 0){
                            return -1;
                        }
                        else return 0;
                    }


                }


            }
        });

        Calendar iter = (Calendar) sdate.clone();
        ArrayList<Event> finalEvents = new ArrayList<>(100 + events.size());
        String date;

        int it = 0;
        while(edate.compareTo(iter) > 0){
            if( iter.get(Calendar.DATE) == 1){

                finalEvents.add(new MonthDisplay(months[iter.get(Calendar.MONTH)] +" "+ iter.get(Calendar.YEAR),calendarToString(iter),null));



                Calendar sunday = (Calendar) iter.clone();
                int dif = sunday.get(Calendar.DAY_OF_WEEK);
                sunday.add(Calendar.DATE,1-dif);
                Calendar saturday = (Calendar) sunday.clone();
                saturday.add(Calendar.DATE,6);
                int week = saturday.get(Calendar.WEEK_OF_YEAR);
                int startday = sunday.get(Calendar.DATE);
                int endday = saturday.get(Calendar.DATE);
                int sunmon = sunday.get(Calendar.MONTH);
                int satmon = saturday.get(Calendar.MONTH);
                String temp;
                if( sunmon == satmon) {
                    temp = String.format(Locale.ENGLISH, "Week %2d, %02d - %02d %s",week,startday,endday,mon[sunmon]);
                }
                else
                    temp = String.format(Locale.ENGLISH, "Week %2d, %02d %s - %02d %s",week,startday,mon[sunmon],endday,mon[satmon]);

                String id = String.format(Locale.ENGLISH,"%02d-%4d",week,sunday.get(Calendar.YEAR));
                Event w = new Event(true, id , temp);
                w.setWeek_date(calendarToString(iter));
                finalEvents.add(w);
            }

            else if (iter.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Calendar sunday = (Calendar) iter.clone();
                Calendar saturday = (Calendar) sunday.clone();
                saturday.add(Calendar.DATE,6);
                int week = saturday.get(Calendar.WEEK_OF_YEAR);
                int startday = sunday.get(Calendar.DATE);
                int endday = saturday.get(Calendar.DATE);
                int sunmon = sunday.get(Calendar.MONTH);
                int satmon = saturday.get(Calendar.MONTH);
                String temp;
                if( sunmon == satmon) {
                    temp = String.format(Locale.ENGLISH, "Week %2d, %02d - %02d %s",week,startday,endday,mon[sunmon]);
                }
                else
                    temp = String.format(Locale.ENGLISH, "Week %2d, %02d %s - %02d %s",week,startday,mon[sunmon],endday,mon[satmon]);

                String id = String.format(Locale.ENGLISH,"%02d-%4d",week,sunday.get(Calendar.YEAR));
                Event w = new Event(true, id , temp);
                w.setWeek_date(calendarToString(sunday));
                finalEvents.add(w);
            }


            // Log.d(TAG+"Eve","dates called = "+calendarToString(calendar));
            date  = calendarToString(iter);
            boolean dateFlag = true;
            i = 0;
           // Log.d(TAG,"For dates called sdate = " + sDateString + " Edate date = " + eDateString + " int it  = " + it + " size = " + size) ;

            while( (it < size) && date.equals(ec[it].eventDate)){
                if(dateFlag){
                    finalEvents.add(new Event(date));
                    dateFlag = false;
                }
                ec[it].serialNo = ++i;
                finalEvents.add(ec[it]);
                it++;
            }

            //events.add(new Event(date));





            iter.add(Calendar.DATE,1);
        }

        finalEvents.trimToSize();
        return finalEvents;

    }

    EventCacheBuilder[] getEventsForDate(Calendar calendar) throws Exception {
        ArrayList<EventCacheBuilder> events = new ArrayList<>(100);

        if (calendar == null )
            calendar = Calendar.getInstance();

        String dateString = calendarToString(calendar);


        String sql1 = "SELECT * FROM `events` WHERE `eventDate` >= \""+ dateString + "\"AND `eventDate` <= \""+ dateString + "\" ORDER BY `eventDate` ASC, `eventTime` ASC, `eventDuration` DESC ";
        Cursor  c1 = query(sql1);
        if (c1.moveToFirst() ) {

            do{
                events.add(new EventCacheBuilder(c1));
            }while(c1.moveToNext());
        }
        c1.close();

        Cursor c = query("SELECT * FROM `recur_events` WHERE `eventDate`  <= \"" + dateString + "\"AND  `eventEndDate`  >= \""+ dateString +"\" ORDER BY `eventDate` ASC, `eventTime` ASC ");

        if (c.moveToFirst()){



            do{
                RecurEventBuilder recurEventBuilders = new RecurEventBuilder(c);

                int recurType = recurEventBuilders.recurType;
                int recurLength = recurEventBuilders.recurLength;


                // Calculate the date b/w Start and end Date
                // Set the actual start and end dates by comparing it to the object
                Calendar eventStartDate = stringToCalender(recurEventBuilders.eventDate);
                Calendar eventEndDate = stringToCalender(recurEventBuilders.eventEndDate);



                // Log.d("NewDebug","sDateString = " + sDateString + ", eDateString = " + eDateString + ", eventStartDate = " + recurEventBuilders.eventDate + ", eventEndDate = " + recurEventBuilders.eventEndDate + " finalSdate = " + calendarToString(finalSdate) + ", finalEdate = " + calendarToString(finalEdate));


                switch (recurType){

                    case 1:

                                if( ( (date_diff(calendar,eventStartDate)) % recurLength) == 0  ){   // See if the event comes under current iteration  dates

                                    // Check for the exp recurring events
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " + "AND `modifiedDate` = " + calendarToString(calendar);
                                    Cursor t = query(sql);
                                    String tempDate = calendarToString(calendar);
                                    String id = recurEventBuilders.ID + "_" +tempDate;
                                    if (t.moveToFirst()){
                                        // There are no exceptions for this case
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(t);

                                        events.add(new EventCacheBuilder(er, recurEventBuilders,id,tempDate));


                                    }
                                    else{

                                        events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                    }
                                    t.close();
                                    //   Log.d("NewDebug : ", id + " iter : " + iter + " date : " +calendarToString(AgendaViewEndless) );



                            }








                        break;

                    case 2:
                        // Event is recurring week type
                        // get the iter the for loop according to weeks of start and end date
                        // find the sunday closest to final start date and end date
                        // See if the current week comes under the event week and update the events list

                        Calendar eventStartSunday = (Calendar) calendar.clone();
                        int diffa = eventStartSunday.get(Calendar.DAY_OF_WEEK);
                        eventStartSunday.add(Calendar.DATE,1-diffa);



                        Calendar eventStartDateSunday = (Calendar) eventStartDate.clone();
                        int diffC = eventStartDateSunday.get(Calendar.DAY_OF_WEEK);
                        eventStartDateSunday.add(Calendar.DATE,1-diffC);


                        //  Log.d("NewDebug" , "eventStartSunday = " + calendarToString(eventStartSunday));



                            if (  ((date_diff(eventStartSunday,eventStartDateSunday)/7)%recurLength) == 0 ){  // IF Current week is a multiple of recur length
                                // Check if the current date is in exception recur table
                                boolean[] recurData = recurEventBuilders.getRecurData();
                                Calendar temp = (Calendar) eventStartSunday.clone();
                                for (int ii = 1; ii < 8;ii++){
                                    // See From Sunday to Saturday anc add events as per recurData
                                    // Check if the event end date is exceeded


                                    if( (eventEndDate.getTimeInMillis() - temp.getTimeInMillis()) < 0 ){
                                        // if the date exceeds end date then stop
                                        break;
                                    }

                                    if( recurData[ii-1] && (date_diff(calendar,temp) == 0) && ((temp.getTimeInMillis() - eventStartDate.getTimeInMillis()) >= 0) ){
                                        // Check exp recur table for the current date.
                                        String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID +" "+ "AND `modifiedDate` = " + calendarToString(temp);
                                        Cursor tt = query(sql);
                                        String tempDate  =  calendarToString(temp);
                                        String id = recurEventBuilders.ID + "_" + tempDate;
                                        if (tt.moveToFirst()){
                                            // There are no exceptions for this case
                                            ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                            events.add( new EventCacheBuilder(er, recurEventBuilders,id,tempDate));


                                        }
                                        else{

                                            events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                        }
                                        tt.close();
                                    }


                                    temp.add(Calendar.DATE,1);
                                }


                            }




                        break;

                    case 3:

                        // Recurring Monthly
                        // get the start month to end month. Max months are 3
                        // see if the iter month has a event
                        int eventDate = eventStartDate.get(Calendar.DATE);
                        //     Log.d("NewDebug" , "Length of difference = " +len);

                            if( (month_diff(calendar,eventStartDate)%recurLength == 0 ) ){

                                // validate the iter date
                                // Add an event in this loop
                                // check if there is any exception in the exp_recur_table

                                if( eventDate <= calendar.getActualMaximum(Calendar.DATE)){

                                    // Check for the exception
                                    String tempDate = String.format(Locale.ENGLISH,"%04d-%02d-%02d",calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH) + 1),eventDate);
                                    String id = recurEventBuilders.ID + "_" +tempDate;
                                    String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " +"AND `modifiedDate` = " + tempDate;
                                    Cursor tt = query(sql);
                                    if(tt.moveToFirst()){
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                        events.add(new EventCacheBuilder(er, recurEventBuilders,id,tempDate));

                                    }
                                    else{
                                        events.add(new EventCacheBuilder(recurEventBuilders,id,tempDate));

                                    }
                                    tt.close();

                                }

                                // else event cannot be in this month






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

        events.trimToSize();
        int size = events.size();

        EventCacheBuilder[] ec =  events.toArray(new EventCacheBuilder[size]);
        Arrays.sort(ec,new Comparator<EventCacheBuilder>(){

            @Override
            public int compare(EventCacheBuilder max, EventCacheBuilder min) {

                int compare =  max.eventDate.compareTo(min.eventDate);

                if(compare > 0){
                    return 1;
                }
                else if(compare < 0){
                    return -1;
                }
                else{

                    int ct = max.eventTime.compareTo(min.eventTime);
                    if( ct > 0){
                        return 1;
                    }
                    else if( ct < 0){
                        return 1;
                    }
                    else{
                        int a = max.eventDuration - min.eventDuration;
                        if(a > 0){
                            return 1;
                        }
                        else if(a < 0){
                            return -1;
                        }
                        else return 0;
                    }


                }


            }
        });
        return ec;
    }

    static Calendar stringToCalender(String date) throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(f.parse(date));

        return c;

    }

    static String calendarToString(Calendar c){
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
    // Do we have to update an event??
    // most probably No


}

