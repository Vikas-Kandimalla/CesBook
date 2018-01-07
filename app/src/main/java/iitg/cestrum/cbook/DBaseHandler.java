package iitg.cestrum.cbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Process;
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

    public final static int eventGenerateRange = 42;

    private final static String TAG = "CBook";
    private final static String dBaseName = "localDB";
    private static final int dBaseVersion = 1;
    private static boolean isTempCachePopulated = false;

    private Context context;
    private static Calendar nextCachedDate;
    private static Calendar prevCachedDate;


    public String[] months = new String[]{"January","February","March","April","May","June","July","August","Sepetember","October","November","December"};


    public DBaseHandler(Context context) {
        super(context,dBaseName,null,dBaseVersion);
        super.setWriteAheadLoggingEnabled(true);
        this.context = context;


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
                "  `ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
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


        String TABLE_EXP_RECUR_EVENTS = "CREATE TABLE `exp_recur_events` (" +
                "  `ID` INTEGER NOT NULL," +
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
        String TABLE_EVENTS_CACHE = "CREATE TABLE `events_cache` (" +
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
                "    );";

        String TABLE_TEMP_EVENTS_CACHE = "CREATE TABLE `temp_events_cache` (" +
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
                ");";

        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_RECUR_EVENTS);
        db.execSQL(TABLE_EXP_RECUR_EVENTS);
        db.execSQL(TABLE_EVENTS_CACHE);
        db.execSQL(TABLE_TEMP_EVENTS_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS `events` ");
        db.execSQL("DROP TABLE IF EXISTS `recur_events`");
        db.execSQL("DROP TABLE IF EXISTS `exp_recur_events` ");
        db.execSQL("DROP TABLE IF EXISTS `events_cache`");
        db.execSQL("DROP TABLE IF EXISTS `temp_events_cache`");
        onCreate(db);

    }

    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS `events` ");
        db.execSQL("DROP TABLE IF EXISTS `recur_events`");
        db.execSQL("DROP TABLE IF EXISTS `exp_recur_events` ");
        db.execSQL("DROP TABLE IF EXISTS `events_cache`");
        db.execSQL("DROP TABLE IF EXISTS `temp_events_cache`");
        db.close();
    }

    public void resetTables() {
        deleteTables();
        createTables();
    }

    public void createTables() {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_TABLE_EVENTS = "CREATE TABLE `events` ( " +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` VARCHAR(50) NOT NULL, " +
                "`eventDate` DATE NOT NULL," +
                "`eventTime` TIME NOT NULL," +
                "`eventDuration` INTEGER NOT NULL," +
                "`eventVenue` VARCHAR(50) DEFAULT NULL," +
                "`courseName` VARCHAR(100) DEFAULT NULL," +
                "`prof` VARCHAR(50) DEFAULT NULL," +
                "`credits` VARCHAR(10) DEFAULT NULL" +
                ");";

        String CREATE_TABLE_RECUR_EVENTS = "CREATE TABLE `recur_events` (" +
                "  `ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `eventDate` date NOT NULL," +
                "  `eventTime` time NOT NULL," +
                "  `eventDuration` INTEGER NOT NULL," +
                "  `eventEndDate` date NOT NULL DEFAULT '9999-12-31'," +
                "  `recurType` INTEGER NOT NULL," +
                "  `recurLength` INTEGER NOT NULL," +
                "  `recurData` varchar(10) NOT NULL," +
                "  `eventVenue` varchar(30) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL," +
                "  `prof` varchar(30)  DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL" +
                ");";


        String TABLE_EXP_RECUR_EVENTS = "CREATE TABLE `exp_recur_events` (" +
                "  `ID` INTEGER NOT NULL," +
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
        String TABLE_EVENTS_CACHE = "CREATE TABLE `events_cache` (" +
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
                ");";

        String TABLE_TEMP_EVENTS_CACHE = "CREATE TABLE `temp_events_cache` (" +
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
                ");";


        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_RECUR_EVENTS);
        db.execSQL(TABLE_EXP_RECUR_EVENTS);
        db.execSQL(TABLE_EVENTS_CACHE);
        db.execSQL(TABLE_TEMP_EVENTS_CACHE);

        db.close();
    }


    public void clearEventsCache() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS `events_cache`");

        String TABLE_EVENTS_CACHE = "CREATE TABLE `events_cache` (" +
                "   `ID` varchar(30) PRIMARY KEY NOT NULL," +
                "   `name` VARCHAR(50) NOT NULL, " +
                "   `eventDate` DATE NOT NULL," +
                "   `eventTime` TIME NOT NULL," +
                "   `eventDuration` INTEGER NOT NULL," +
                "   `eventVenue` VARCHAR(50) DEFAULT NULL," +
                "   `courseName` VARCHAR(100) DEFAULT NULL," +
                "   `prof` VARCHAR(50) DEFAULT NULL," +
                "   `credits` VARCHAR(50) DEFAULT NULL," +
                "   `parentID` INTEGER NOT NULL," +
                "   `parentRecurType` INTEGER NOT NULL," +
                "   `parentRecurLength` INTEGER NOT NULL," +
                "   `parentRecurData` VARCHAR(10) DEFAULT NULL," +
                "   `isModified` INTEGER DEFAULT 0" +
                ");";

        db.execSQL(TABLE_EVENTS_CACHE);

        db.close();
    }

    public void resetTempCache() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS `temp_events_cache`");

        String TABLE_TEMP_EVENTS_CACHE = "CREATE TABLE `temp_events_cache` (" +
                "   `ID` varchar(30) PRIMARY KEY NOT NULL," +
                "   `name` VARCHAR(50) NOT NULL, " +
                "   `eventDate` DATE NOT NULL," +
                "   `eventTime` TIME NOT NULL," +
                "   `eventDuration` INTEGER NOT NULL," +
                "   `eventVenue` VARCHAR(50) DEFAULT NULL," +
                "   `courseName` VARCHAR(100) DEFAULT NULL," +
                "   `prof` VARCHAR(50) DEFAULT NULL," +
                "   `credits` VARCHAR(50) DEFAULT NULL," +
                "   `parentID` INTEGER NOT NULL," +
                "   `parentRecurType` INTEGER NOT NULL," +
                "   `parentRecurLength` INTEGER NOT NULL," +
                "   `parentRecurData` VARCHAR(10) DEFAULT NULL," +
                "   `isModified` INTEGER DEFAULT 0" +
                ");";

        db.execSQL(TABLE_TEMP_EVENTS_CACHE);

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


    public long addEvent(EventCacheBuilder event,boolean tempCache){

        SQLiteDatabase db = this.getWritableDatabase();
        if(!tempCache) {

            long temp = db.insertWithOnConflict("events_cache", null, event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);

            db.close();
            return temp;
        }
        else {
            long temp = db.insertWithOnConflict("temp_events_cache", null, event.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            return temp;
        }

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

    public void populateEventCache(Date date) throws Exception{
         populateEventCache(date,false);
    }

    public void populateEventCache(Date date,boolean tempCache) throws Exception {

        // populate the cache table for event generation
        // update the event class to add the required flags
        // table Name is event cache
        // Convert the PHP code to JAVA in this section
        // Adds only the prev 2 weeks,current week,next 2 weeks events to the table
        // if date is not in the above mentioned
        // Writes into temp Cache if tempCache is true


        if(tempCache && date == null ){
          //  if(tempCache) {
                isTempCachePopulated = true;
                resetTempCache();
                nextCachedDate = Calendar.getInstance();
                prevCachedDate = Calendar.getInstance();
                Log.d(TAG+"eve","Temp Cache Reset");
        }
        Calendar calendar = Calendar.getInstance();
        if (date != null )
            calendar.setTime(date);

        int diff = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,1-diff);
        Calendar sdate = (Calendar) calendar.clone();
        Calendar edate = (Calendar) calendar.clone();
        edate.add(Calendar.DATE,DBaseHandler.eventGenerateRange);
        sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -1);

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        String eDateString = dtFormat.format(edate.getTime());
        String sDateString = dtFormat.format(sdate.getTime());





        Cursor c;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql1 = "SELECT * FROM `events` WHERE `eventDate` >= \""+ sDateString + "\"AND `eventDate` <= \""+ eDateString + "\" ORDER BY `eventDate` ASC, `eventTime` ASC, `eventDuration` DESC ";
        c = db.rawQuery(sql1,null);
        int i = 0;
        if (c.moveToFirst() ) {

            do{
                addEvent(new EventCacheBuilder(c),tempCache);
            }while(c.moveToNext());
        }
        c.close();

        c = db.rawQuery("SELECT * FROM `recur_events` WHERE `eventDate`  <= \"" + eDateString + "\"AND  `eventEndDate`  >= \""+ sDateString +"\" ORDER BY `eventDate` ASC, `eventTime` ASC ",null);

        if (c.moveToFirst()){
            i = 0;

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
                              //  Log.d("NewDebug","This loop is executed : " + id + " iter : " + iter + " date : " +calendarToString(temp));
                                String sql = "SELECT * FROM `exp_recur_events` WHERE `ID` = " + recurEventBuilders.ID + " " + "AND `modifiedDate` = " + calendarToString(temp);
                                t1 = query(sql,null);

                                if (t1.moveToFirst()){
                                    // There are no exceptions for this case
                                    ExpRecurEventBuilder er = new ExpRecurEventBuilder(t1);
                                    addEvent(new EventCacheBuilder(er, recurEventBuilders,id,tempDate),tempCache);


                                }
                                else{

                                    addEvent(new EventCacheBuilder(recurEventBuilders,id,tempDate),tempCache);

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
                                    Cursor t = query(sql,null);
                                    String tempDate = calendarToString(temp);
                                    String id = recurEventBuilders.ID + "_" +tempDate;
                                    if (t.moveToFirst()){
                                        // There are no exceptions for this case
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(t);

                                        addEvent(new EventCacheBuilder(er, recurEventBuilders,id,tempDate),tempCache);


                                    }
                                    else{

                                        addEvent(new EventCacheBuilder(recurEventBuilders,id,tempDate),tempCache);

                                    }
                                    t.close();
                                    flag = true;
                                 //   Log.d("NewDebug : ", id + " iter : " + iter + " date : " +calendarToString(temp) );
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
                        int t = 0;
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
                                        Cursor tt = query(sql,null);
                                        String tempDate  =  calendarToString(temp);
                                        String id = recurEventBuilders.ID + "_" + tempDate;
                                        if (tt.moveToFirst()){
                                            // There are no exceptions for this case
                                            ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                            addEvent( new EventCacheBuilder(er, recurEventBuilders,id,tempDate),tempCache);


                                        }
                                        else{

                                            addEvent(new EventCacheBuilder(recurEventBuilders,id,tempDate),tempCache);

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
                        int startMonth = finalSdate.get(Calendar.MONTH);
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
                                    Cursor tt = query(sql,null);
                                    if(tt.moveToFirst()){
                                        ExpRecurEventBuilder er = new ExpRecurEventBuilder(tt);
                                        addEvent(new EventCacheBuilder(er, recurEventBuilders,id,tempDate),tempCache);

                                    }
                                    else{
                                        addEvent(new EventCacheBuilder(recurEventBuilders,id,tempDate),tempCache);

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
                        db.close();
                        throw new Exception("Unknown Recurring Type");
                }





            }while(c.moveToNext());
        }


        db.close();



    }

    private Cursor query(String sql, String nullColumn){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql,null);


    }

    public EventCacheBuilder[] getEvents(Calendar date){
        // Getting the list of events in date
        // Need to calculate the overlap's if present
        String sql = "SELECT * FROM `events_cache` WHERE `eventDate` = \"" + calendarToString(date) + "\" ORDER BY `eventTime` ASC, `eventDuration` DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);
        if( c == null){
            db.close();
            return null;
        }
        EventCacheBuilder[] event = new EventCacheBuilder[c.getCount()];
        c.moveToFirst();
        int i = 0;
        do{
            event[i++].setEventData(c);
        }while(c.moveToNext());
        c.close();
        db.close();
        return event;
    }

    public ArrayList<Event> generateEvents(Calendar calendar,int np) throws Exception {
        if(np < -2 || np > 2 ){
            Log.e(TAG,"Error Np should be in the range of -1 to 1");
            return null;
        }

        Calendar[] dates = new Calendar[2];
        if(calendar == null) {
            calendar = Calendar.getInstance();
        }
        if(!isTempCachePopulated) {
            Log.d(TAG+"Eve" ,"This is running ");
            populateEventCache(null, true);
        }

        if(date_diff(calendar,prevCachedDate) != 0 || date_diff(calendar,nextCachedDate) != 0){
            Log.d(TAG+"Eve"," Error Date = " + calendarToString(calendar));
            populateEventCache(calendar.getTime(),true);
        }
        if(date_diff(calendar,prevCachedDate) == 0) {
            Log.d(TAG+"Eve"," Error Date in prev = " + calendarToString(calendar));
            new backgroundDBasePopulation(context, calendar, -1).start();
        }
        if(date_diff(calendar,nextCachedDate) == 0) {
            Log.d(TAG+"Eve"," Error Date in next = " + calendarToString(calendar));
            new backgroundDBasePopulation(context, calendar, 1).start();
        }

        //populateEventCache(calendar.getTime(),true);

        // Start The backgroung threads for the next and prev cache



        int diff = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,1-diff);

        dates[0]= (Calendar) calendar.clone();
        dates[1] = (Calendar) calendar.clone();

        dates[0].add(Calendar.DATE,DBaseHandler.eventGenerateRange * -1);
        dates[1].add(Calendar.DATE,DBaseHandler.eventGenerateRange);

        //calendar.add(Calendar.DATE,DBaseHandler.eventGenerateRange * np * 2);

        ArrayList<Event> events = new ArrayList<>(eventGenerateRange * 4);


        Log.d(TAG + "Eve","The calendar prev date = " + calendarToString(prevCachedDate) + " next date = " + calendarToString(nextCachedDate));
        calendar = (Calendar) dates[0].clone();
        String sql,date;
        boolean flag = false;
        while(dates[1].compareTo(calendar) > 0) {

            if( calendar.get(Calendar.DATE) == 1){
                flag = true;

                events.add(new MonthDisplay(months[calendar.get(Calendar.MONTH)]));
            }
            date  = calendarToString(calendar);
            sql = "SELECT * FROM `temp_events_cache` WHERE `eventDate` = \"" + date + "\" ORDER BY `eventTime` ASC, `eventDuration` DESC";
            Cursor c = query(sql, null);


            if(c != null) {
                if (c.moveToFirst()) {
                    events.add(new Event(date));
                    int i = 0;
                    do {
                        events.add(new EventCacheBuilder(c,++i));
                    } while (c.moveToNext());

                }
                else{
                    if( date.equals(calendarToString(Calendar.getInstance()))) {
                        Log.d(TAG + "vv", "Inserting at position = " + events.size());
                        events.add(new Event(date));
                    }
                }
                c.close();
            }else{
                if( date.equals(calendarToString(Calendar.getInstance()))) {
                    Log.d(TAG + "vv", "Inserting at position = " + events.size());
                    events.add(new Event(date));
                }
            }


            calendar.add(Calendar.DATE,1);
        }

        return events;

    }

    private class backgroundDBasePopulation extends Thread{
        private Calendar date;
        private Context context;

        backgroundDBasePopulation(Context context,Calendar calendar, int n){
            this.date = (Calendar) calendar.clone();
            this.context = context;
            this.date.add(Calendar.DATE,DBaseHandler.eventGenerateRange * 2 * n);

        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            try {
                DBaseHandler dBaseHandler = new DBaseHandler(context);
                dBaseHandler.populateEventCache(date.getTime(),true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            nextCachedDate = (Calendar) this.date.clone();
        }

    }


    public ArrayList<Event> getAllEvents() {
        String sql = "SELECT * FROM `events_cache` ORDER BY `eventTime` ASC, `eventDuration` DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);
        if( c == null){
            db.close();
            return null;
        }
        ArrayList<Event> events = new ArrayList<>(c.getCount());
        c.moveToFirst();

        do{
            events.add(new EventCacheBuilder(c));
        }while(c.moveToNext());
        c.close();
        db.close();
        return events;
    }

    public static Calendar stringToCalender(String date) throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(f.parse(date));

        return c;

    }

    public static String calendarToString(Calendar c){
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

