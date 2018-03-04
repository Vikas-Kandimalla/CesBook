package iitg.cestrum.cbook;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by vikas on 05-02-2018.
 */

public class FCMNotifications extends FirebaseMessagingService {

    private final static String TAG = "Firebase-Messaging";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){




        if(remoteMessage.getData().size() > 0)
            Log.d(TAG,"Message Data payload: " + remoteMessage.getData());


        if(remoteMessage.getNotification() != null){
            Log.d(TAG,"Notification data payload: "+remoteMessage.getNotification());
        }


    }


}
