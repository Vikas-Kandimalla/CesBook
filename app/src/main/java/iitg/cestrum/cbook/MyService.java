package iitg.cestrum.cbook;



import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;


public class MyService extends Service {

    public static final String TAG = "serviceTest";
    private getData gdata = new getData();
    String data="";
    private boolean flag = false;
    public final String URL = "http://192.168.42.90/appData/appGetEvents.php";
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

        new getData().execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    public class  getData extends AsyncTask<Void,Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            if(flag){
                SystemClock.sleep(50000);
            }

            HttpHandler web = new HttpHandler();
            data = web.getWebPage(URL);
            while (data == null){
                SystemClock.sleep(10000);
                data = web.getWebPage(URL);
                Log.d(TAG," Received Data :  " + data );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {

                Intent i = new Intent("JSONdata");
                String filename = "eventData";
                File file = new File(getFilesDir() , filename);
                i.putExtra("JSONArray",data);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                Log.d(TAG," Broadcast Sent " + file.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();

                Log.d(TAG," File written. Name : data");




                if ( true ){
                    Log.d(TAG," If LOOP Applied ");
                    flag = true;
                    gdata = new  getData();
                    gdata.execute();
                }
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