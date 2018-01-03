package iitg.cestrum.cbook;



import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;


public class MyService extends Service {

    public static final String TAG = "serviceTest";

    private DBaseHandler dBaseHandler;
    private String data;
    private boolean flag = false;
    public final static String URL = "http://192.168.0.103/appData/appGetEvents.php";
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
        new getData().execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class getData extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if(flag){
                SystemClock.sleep(5000);
            }

            HttpHandler web = new HttpHandler();
            data = web.getWebPage(URL);
            while (data == null){
                SystemClock.sleep(1000);
                data = web.getWebPage(URL);
                Log.d(TAG," Received Data :  " + data );
            }

            try {
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


                dBaseHandler.populateEventCache(null);
                Log.d(TAG,"Data written to the database");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {




                  /*  Log.d(TAG," Received Data :  " + data );
                    JSONObject dj = new JSONObject(data);
                    JSONObject gg = dj.getJSONObject("eventData");
                    JSONArray eventData = gg.getJSONArray("data");
                    JSONObject c = eventData.getJSONObject(0);
                    Log.d(TAG,"ID : " + c.getString("ID") + ", EventDate : " + c.getString("eventDate"));
                 */

               /* Intent i = new Intent("JSON data");
                String filename = "eventData";
                File file = new File(getFilesDir() , filename);
                i.putExtra("JSONArray",data);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                Log.d(TAG," Broadcast Sent " + file.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                Log.d(TAG," File written. Name : data");*/

                Log.d(TAG," If LOOP Applied ");
                flag = true;
                new getData().execute();

            }
            catch (NullPointerException e){
                Log.d("serviceTest"," This is not working.");
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}