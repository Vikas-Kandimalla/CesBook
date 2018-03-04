package iitg.cestrum.cbook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventsUpdateService extends Service {
    private static final String TAG = "ServiceTest";

    public EventsUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int startID, int flags){

        // Get the events;

        String userGroup = "ece_sem_5";
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
/*
        firestore.collection(userGroup).document("events").collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                Log.d(TAG,documentSnapshots.toString());

                if(! documentSnapshots.getMetadata().isFromCache()) {

                    List<EventBuilder> list = new ArrayList<>();
                    DBaseHandler dBaseHandler = new DBaseHandler(getApplicationContext());
                    dBaseHandler.resetTables();
                    for (DocumentSnapshot doc : documentSnapshots) {

                        if(doc != null) {

                            Log.d(TAG,"ID = "+ doc.getId() +" Data = " + doc.getData());
                            EventBuilder eb = new EventBuilder(doc);
                            Log.d(TAG,eb.getContentValues().toString());
                            list.add(eb);
                            dBaseHandler.addEvent(eb);
                        }
                    }

                    dBaseHandler.close();

                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                    localBroadcastManager.sendBroadcast(new Intent("EVENTS_UPDATED"));
                    Log.d(TAG, list.toString());

                }
            }
        });
        */

        firestore.collection(userGroup).document("events").collection("data").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                Log.d(TAG,documentSnapshots.toString());

                if(! documentSnapshots.getMetadata().isFromCache()) {

                    List<EventBuilder> list = new ArrayList<>();
                    DBaseHandler dBaseHandler = new DBaseHandler(getApplicationContext());
                    dBaseHandler.resetTables();
                    for (DocumentSnapshot doc : documentSnapshots) {

                        if(doc != null) {

                            Log.d(TAG,"ID = "+ doc.getId() +" Data = " + doc.getData());
                            EventBuilder eb = new EventBuilder(doc);
                            Log.d(TAG,eb.getContentValues().toString());
                            list.add(eb);
                            dBaseHandler.addEvent(eb);
                        }
                    }

                    dBaseHandler.close();

                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                    localBroadcastManager.sendBroadcast(new Intent("EVENTS_UPDATED"));
                    Log.d(TAG, list.toString());

                }
            }
        });


        return START_STICKY;
    }
}
