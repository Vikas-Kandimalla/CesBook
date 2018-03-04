package iitg.cestrum.cbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static java.lang.Integer.parseInt;

/**
 * Created by vikas on 02-02-2018.
 */

public class NotificationDatabaseHandler extends SQLiteOpenHelper {

    private static final String dBaseName = "notificationDatabase";
    private static final int dBaseVersion = 1;

    private static final String TABLE_NOTIFICATIONS = "CREATE TABLE `notifications` (" +
            "`ID` VARCHAR(255) PRIMARY KEY," +
            "`subject` VARCHAR(255) NOT NULL, " +
            "`timeStamp` VARCHAR(20) NOT NULL," +
            "`postedBy` VARCHAR(50) NOT NULL," +
            "`message` TEXT NOT NULL," +
            "`isRead` INT(1) DEFAULT 0" +
            ");";

    NotificationDatabaseHandler(Context context) {
        super(context,dBaseName,null,dBaseVersion);
        super.setWriteAheadLoggingEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS `notifications`");
            onCreate(db);
    }

    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS `notifications`");
        db.execSQL(TABLE_NOTIFICATIONS);
    }

    public long insertRow(NotificationBuilder nb){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.insertWithOnConflict("notifications",null,nb.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return temp;
    }

    public long insertRow(NotificationBuilder nb, boolean replace){
        SQLiteDatabase db = this.getWritableDatabase();
        long temp;
        if (replace)
            temp = db.insertWithOnConflict("notifications",null,nb.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
        else
            temp = db.insertWithOnConflict("notifications",null,nb.getContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        return temp;
    }

    public void removeRow(String id){
        String sql = "DELETE FROM `notifications` WHERE `ID` = \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void toggleRead(String id){
        String sql = "UPDATE `notifications` SET `isRead` = 1 WHERE `ID` = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public NotificationBuilder[] getNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM `notifications` ORDER BY `timeStamp` DESC",null);
        NotificationBuilder[] nb = null;
        if (c != null && c.moveToFirst() ){

            nb = new NotificationBuilder[ c.getCount() ];
            int i = 0;
            do{
                nb[i++] = new NotificationBuilder(c);
            }while(c.moveToNext());

            c.close();
        }
        db.close();

        return nb;
    }

    /**
     * Created by vikas on 25-12-2017.
     */

    public static class FireStoreEventHolder {

        public String eventName;
        public String eventDate;
        public String eventTime;
        public String eventDuration;
        public String eventVenue;
        public String courseName;
        public String prof;
        public String credits;


        public FireStoreEventHolder(String nam, String date, String time, String duration, String venue, String courName, String prof, String credit) {

            this.eventName = nam;
            this.prof = prof;
            this.credits = credit;
            this.courseName = courName;
            this.eventVenue = venue;
            this.eventDuration =  (duration);
            this.eventDate = date;                                                      // new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.eventTime = time;                                                      //new Time(new SimpleDateFormat("HH:mm:ss").parse(time).getTime());

        }

        public EventBuilder build(String id){
            return new EventBuilder(id,this.eventName,this.eventDate,this.eventTime, parseInt(this.eventDuration) ,this.eventVenue,this.courseName,this.prof,this.credits);
        }

        public FireStoreEventHolder() {

        }



    }
}
