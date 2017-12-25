package iitg.cestrum.cbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by vikas on 25-12-2017.
 */

public class DBaseHandler extends SQLiteOpenHelper {

    private final String dBaseName;
    private final int dBaseVersion;
    private final String tableName = "events";
    private final String[] columns = {"ID","name" , "eventData" , "eventTime" , "eventDuration" ,"eventVenue", "courseName", "prof" , "credits"};

    public DBaseHandler(Context context,String dbName,int dbVersion) {
        super(context,dbName,null,dbVersion);
        this.dBaseName = dbName;
        this.dBaseVersion = dbVersion;

    }



    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_TABLE_EVENTS = "CREATE TABLE events ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50) NOT NULL, " +
                "eventDate DATE NOT NULL" +
                "eventTime TIME NOT NULL" +
                "eventDuration INT NOT NULL" +
                "eventVenue VARCHAR(50) DEFAULT NULL" +
                "courseName VARCHAR(100) DEFAULT NULL" +
                "prof VARCHAR(50) DEFAULT NULL" +
                "credits VARCHAR(50) DEFAULT NULL" +
                "recordTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP );";

        String CREATE_TABLE_RECUR_EVENTS = "CREATE TABLE `recur_events` (" +
                "  `ID` int(11) PRIMARY KEY NOT NULL," +
                "  `name` varchar(255) NOT NULL," +
                "  `startTime` time NOT NULL," +
                "  `duration` int(11) NOT NULL," +
                "  `startDate` date NOT NULL," +
                "  `endDate` date NOT NULL DEFAULT '9999-12-31'," +
                "  `recurType` int(11) NOT NULL," +
                "  `recurLength` int(11) NOT NULL," +
                "  `recurData` varchar(10) NOT NULL," +
                "  `eventVenue` varchar(10) DEFAULT NULL," +
                "  `prof` varchar(30)  DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
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
                "  `prof` varchar(30) DEFAULT NULL," +
                "  `credits` varchar(10) DEFAULT NULL," +
                "  `courseName` varchar(255) DEFAULT NULL" +
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

    public void addEvent() {

    }



}

