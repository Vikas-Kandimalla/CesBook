package iitg.cestrum.cbook;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyService extends Service {

    public static final String TAG = "serviceTest";
    private boolean sleepFlag = false;
    private boolean eventsUpdated = false;
    private static boolean mShouldContinue = false;
    private DBaseHandler dBaseHandler;
    private boolean shouldDownloadEvents = true;
    private boolean shouldDownloadNotifications = false;
    public final static String address = "http://10.1.2.74/";
    public final static String eventsURL =  address + "appData/appGetEvents.php";
    public final static String updateURL =  address + "appData/updateData.php";
    public final static String notificationsURL = address + "appData/notificationsData.php";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand started and running : ");
        dBaseHandler = new DBaseHandler(getApplicationContext());
        mShouldContinue = true;
        //new getData().execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShouldContinue = false;
    }


    public class getData extends AsyncTask<Void,Void,Void> {





        @Override
        protected Void doInBackground(Void... params) {

            Thread.currentThread();

            if(sleepFlag){

                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            HttpHandler web = new HttpHandler();
            String data = web.getWebPage(updateURL);
            while (data == null){
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data = web.getWebPage(updateURL);
                Log.d(TAG," Received Data :  " + data );
            }

            try {
                eventUpdateData(data);
                if(shouldDownloadNotifications){
                    String notifs = web.getWebPage(notificationsURL);
                    while (notifs == null){
                        Thread.sleep(60000);
                        notifs = web.getWebPage(notificationsURL);
                        Log.d(TAG,"Received notifications data = " + notifs);
                    }
                    updateNotificationsDatabase(notifs);
                    Log.d(TAG," Database updated");
                }
                if(shouldDownloadEvents){
                    data = web.getWebPage(eventsURL);
                    while (data == null){
                        Thread.sleep(60000);
                        data = web.getWebPage(eventsURL);
                        Log.d(TAG," Received Data :  " + data );
                    }
                    updateDBase(data);
                    eventsUpdated = true;
                    Log.d(TAG,"DataBase Updated.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

                if(eventsUpdated) {
                    eventsUpdated = false;
                    Intent i = new Intent("Database Updated");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                }

                Log.d(TAG,"Researching For data");
                sleepFlag = true;
                if (mShouldContinue)
                    new getData().execute();

        }
    }

    private void eventUpdateData(String data) throws JSONException, NullPointerException {
        JSONArray p = new JSONArray(data);
        JSONObject dat = p.getJSONObject(0);

        String events = dat.getString("events");
        String recurEvents = dat.getString("recurEvents");
        String expRecurEvents = dat.getString("expRecurEvents");
        String notifications = dat.getString("notifications");

        if( (events == null) || (recurEvents == null) || (expRecurEvents == null) )
                throw new NullPointerException("Received data is INCORRECT");

        SharedPreferences myPref = getApplicationContext().getSharedPreferences("updateData",MODE_PRIVATE);
        String prefEvents = myPref.getString("uEvents",null);
        String prefRecurEvents = myPref.getString("uRecurEvents",null);
        String prefExpRecurEvents = myPref.getString("uExpRecurEvents",null);
        String prefNotifications = myPref.getString("uNotifications",null);


        if ( prefEvents != null && prefRecurEvents != null && prefExpRecurEvents != null) {

            if (prefEvents.equalsIgnoreCase(events) &&
                    prefRecurEvents.equalsIgnoreCase(recurEvents) &&
                    prefExpRecurEvents.equalsIgnoreCase(expRecurEvents)) {

                this.shouldDownloadEvents = false;
                Log.d(TAG, "Update Data is up to date");


            }
        }
        else {

            this.shouldDownloadEvents = true;
            SharedPreferences.Editor editor = myPref.edit();
            editor.putString("uEvents", events);
            editor.putString("uRecurEvents", recurEvents);
            editor.putString("uExpRecurEvents", expRecurEvents);
            editor.apply();


            Log.d(TAG, "eventUpdateData = " + myPref.getString("uEvents", null));
            Log.d(TAG, "recurUpdateData = " + myPref.getString("uRecurEvents", null));
            Log.d(TAG, "expRecuUpdateData = " + myPref.getString("uExpRecurEvents", null));
        }

        if (prefNotifications != null){
            if(prefNotifications.equalsIgnoreCase(notifications)){
                this.shouldDownloadNotifications = false;
                Log.d(TAG,"Notifications data is up to date");
            }
        }
        else{
            this.shouldDownloadNotifications = true;
            SharedPreferences.Editor edito = myPref.edit();
            edito.putString("uNotifications",notifications);
            edito.apply();

            Log.d(TAG, "NotificationData = " + myPref.getString("uNotifications",null));
        }
    }

    private synchronized void updateDBase(String data) throws Exception {

        JSONObject parentObj = new JSONObject(data);

        JSONObject events = parentObj.getJSONObject("events");
        int numOfEvents = events.getInt("num");
        JSONArray eventData = events.getJSONArray("data");

        JSONObject recurEvents = parentObj.getJSONObject("recurEvents");
        int numOfRecurEvents = recurEvents.getInt("num");
        JSONArray recurData = recurEvents.getJSONArray("data");

        JSONObject expRecurEvents = parentObj.getJSONObject("expRecurEvents");
        int numOfERE = expRecurEvents.getInt("num");
        JSONArray expRecurData = expRecurEvents.getJSONArray("data");


        dBaseHandler.resetTables();
        for(int i = 0; i < numOfEvents; i++)
            dBaseHandler.addEvent(new EventBuilder(eventData.getJSONObject(i)));

        for(int i = 0; i < numOfRecurEvents; i++)
            dBaseHandler.addRecurEvent(new RecurEventBuilder(recurData.getJSONObject(i)));

        for(int i = 0; i < numOfERE; i++)
            dBaseHandler.addExpRecurEvent(new ExpRecurEventBuilder(expRecurData.getJSONObject(i)));


        //dBaseHandler.populateEventCache(null);

        dBaseHandler.close();
        Log.d(TAG,"Data written to the database");

    }

    private synchronized  void updateNotificationsDatabase(String data) throws JSONException {
        NotificationDatabaseHandler handler = new NotificationDatabaseHandler(getApplicationContext());
        JSONObject parent = new JSONObject(data);
        int num = parent.getInt("num");
        JSONArray notifications = parent.getJSONArray("data");
        handler.resetTable();
        for(int i = 0; i < num; i++){
            handler.insertRow(new NotificationBuilder(notifications.getJSONObject(i)));
        }
        handler.close();
        Log.d(TAG,"Notification Data written to the database");
    }
}