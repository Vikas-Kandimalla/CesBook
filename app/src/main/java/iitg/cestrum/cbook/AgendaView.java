package iitg.cestrum.cbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;


public class AgendaView extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<Event> events;
    private agendaViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_view);



        DBaseHandler dBaseHandler = new DBaseHandler(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerEventView);

        try {
            events = dBaseHandler.generateEvents(null,0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter = new agendaViewAdapter(this,events);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new agendaViewScrollListener(dBaseHandler,recyclerView,adapter,events,Calendar.getInstance()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(15);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView.setAdapter(adapter);



    }

    @Override
    protected void onResume() {
        super.onResume();

        int currentDatePosition = events.indexOf(new Event(DBaseHandler.calendarToString(Calendar.getInstance())));

        //Log.d("NewDebug", "Current Date position = " + currentDatePosition  + "Today's Date = " + DBaseHandler.calendarToString(Calendar.getInstance()));

        recyclerView.scrollToPosition(currentDatePosition);

    }
}
