package iitg.cestrum.cbook;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vikas on 05-02-2018.
 */

public class FIDService extends FirebaseInstanceIdService {
    private final static String TAG = "FIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String refreshedToken){
        // TODO After adding login send the information to server.
    }
}
