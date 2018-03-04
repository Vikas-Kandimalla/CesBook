package iitg.cestrum.cbook;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class NotificationsFragment extends Fragment {

    private static final String TAG = "FireStore";
    private NotificationDatabaseHandler handler;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private NotificationAdapter adapter;
    private ListView listView;
    private ListenerRegistration registration;


    public NotificationsFragment() {
        // Required empty public constructor
    }


    public static NotificationsFragment newInstance(String param1, String param2) {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance){
        handler = ((MainActivity)getActivity()).notificationHandler;
        listView = (ListView) view.findViewById(R.id.notification_list_view);

        NotificationBuilder[] nb = handler.getNotifications();
        if(nb != null) {
            adapter = new NotificationAdapter(getContext(), R.id.notification_list_view, nb);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public  void onResume(){
        super.onResume();
        Query query = firestore.collection("ece_sem_5/notifications/data").orderBy("timeStamp", Query.Direction.DESCENDING);

        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(e != null){
                    Log.w(TAG,"Listen:Error " + e);
                    return;
                }
                ArrayList<NotificationBuilder> notifs = new ArrayList<>();

                boolean flag = false;
                String source = documentSnapshots.getMetadata().hasPendingWrites() ? "Local" : "Server";
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if( Objects.equals(source,"Server")) {
                        switch (doc.getType()) {
                            case ADDED:
                                flag = true;
                                NotificationBuilder temp = new NotificationBuilder();
                                temp.ID = doc.getDocument().getId();
                                temp.message = (String) doc.getDocument().get("message");
                                temp.subject = (String) doc.getDocument().get("subject");
                                temp.postedBy = (String) doc.getDocument().get("postedBy");
                                temp.timeStamp = (String) doc.getDocument().get("timeStamp");

                                notifs.add(temp);
                                handler.insertRow(temp,false);
                                Log.d(TAG, "New city: " + doc.getDocument().getData());
                                break;
                            case MODIFIED:
                                Log.e(TAG, "Modified:Error Modified case: " + doc.getDocument().getData());
                                break;
                            case REMOVED:
                                flag = true;
                                handler.removeRow(doc.getDocument().getId());
                                Log.d(TAG, "Removed city: " + doc.getDocument().getData());
                                break;
                        }
                    }
                }


                if (flag)
                    populateList();



            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        registration.remove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void populateList() {
        NotificationBuilder[] nb = handler.getNotifications();
        NotificationAdapter adapter = new NotificationAdapter(getContext(),R.id.notification_list_view,nb);
        listView.setAdapter(adapter);
    }

    public class NotificationAdapter extends ArrayAdapter {

        private Context context;
        private NotificationBuilder[] obj;

        public NotificationAdapter(@NonNull Context context, int resource, NotificationBuilder[] objects) {
            super(context, resource, objects);
            this.context = context;
            this.obj = objects;

        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return getAgendaView(inflater,parent,obj[position]);

        }

        @Override
        public int getCount(){
            return obj.length;
        }
    }

    private View getAgendaView(LayoutInflater inflater,ViewGroup container,NotificationBuilder nb){
        View itemView = inflater.inflate(R.layout.display_notifications,container,false);
        TextView subject = (TextView) itemView.findViewById(R.id.notifications_subject);
        TextView message = (TextView) itemView.findViewById(R.id.notifications_message);
        TextView postDetails = (TextView) itemView.findViewById(R.id.notifications_post_details);

        subject.setText(nb.subject);
        String msg = String.format("       %s",nb.message);
        message.setText(msg);

        SimpleDateFormat dt = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
        String time = "??/?? ??:??";
        try {
            time = dt.format(nb.getCalendarTime().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String post_details = "posted by " + nb.postedBy + " on " + time;
        postDetails.setText(post_details);

        return itemView;
    }


}
