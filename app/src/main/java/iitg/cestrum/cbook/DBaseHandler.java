package iitg.cestrum.cbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by vikas on 25-12-2017.
 */

public class DBaseHandler extends SQLiteOpenHelper {

    private final String dBaseName;
    private static final int dBaseVersion = 1;
    private final String[] columns = {"ID","name" , "eventData" , "eventTime" , "eventDuration" ,"eventVenue", "courseName", "prof" , "credits"};
    private EventBuilder[] eventBuilders;
    private RecurEventBuilder[] recurEventBuilders;
    private ExpRecurEventBuilder[] expRecurEventBuilders;



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
        long temp = db.insert("events",null,event.getContentValues());
        db.close();
        return temp;
    }

    public long addEvent(ExpRecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insert("exp_recur_events",null,event.getContentValues());
        db.close();
        return temp;
    }
    public long addEvent(RecurEventBuilder event){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insert("exp_recur_events",null,event.getContentValues());
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

    public void populateEventCache(){

        // populate the cache table for event generation
        // update the event class to add the required flags
        // table Name is event cache
        // Convert the PHP code to JAVA in this section
        // Adds only the prev 2 weeks,current week,next 2 weeks events to the table
        // if date is not in the above mentioned
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        c = db.rawQuery("SELECT * FROM `events`",null);
        int i = 0;
        if (c.moveToFirst() ) {
            eventBuilders = new EventBuilder[c.getCount()];
            do{
                   eventBuilders[i++].setEventData(c);
               }while(c.moveToNext());
        }

        c = db.rawQuery("SELECT * FROM `recur_events`",null);
        if (c.moveToFirst()){
            i = 0;
            recurEventBuilders = new RecurEventBuilder[c.getCount()];
            do{
                recurEventBuilders[i++].setEventData(c);
            }while(c.moveToNext());
        }

        c = db.rawQuery("SELECT * FROM `exp_recur_events`",null);
        if(c.moveToFirst()){
            i = 0;
            expRecurEventBuilders = new ExpRecurEventBuilder[c.getCount()];
            do{
                expRecurEventBuilders[i++].setEventData(c);
            }while(c.moveToNext());
        }

    }

    public Event[] getEvents(Date date){
        // Getting the list of events in date
        // Need to calculate the overlap's if present
        // Used in normal

        return null;
    }

    // Do we have to update an event??
    // most probably No


}

