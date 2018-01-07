package iitg.cestrum.cbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by vikas on 06-01-2018.
 * Rhaegar
 */

public class agendaViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int EVENT = 1;
    public static final int MONTH = 2;
    public static final int DATE = 3;
    private static final String TAG = "ViewTests" ;

    private Context myContext;
    private ArrayList<Event> myEvents;




    public agendaViewAdapter(Context context, ArrayList<Event> events){
        this.myContext = context;
        this.myEvents = events;

    }

    private Context getContext() {
        return this.myContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        switch(viewType){
            case EVENT:
                View agendaView = inflater.inflate(R.layout.agenda_event_view,parent,false);
                viewHolder =  new EventViewHolder(agendaView);

                break;

            case MONTH:
                View monthView = inflater.inflate(R.layout.month_display,parent,false);

                viewHolder = new MonthViewHolder(monthView);

                break;

            case DATE:
                View dateView = inflater.inflate(R.layout.date_display,parent,false);
                viewHolder = new DateViewHolder(dateView);
                break;

            default:
                View view = inflater.inflate(R.layout.date_display,parent,false);
                viewHolder = new DateViewHolder(view);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {



        switch(viewHolder.getItemViewType()){
            case EVENT:

                //
                EventCacheBuilder ecb = (EventCacheBuilder) myEvents.get(position);
                EventViewHolder holder = (EventViewHolder) viewHolder;
                String a = String.valueOf(ecb.serialNo);
                holder.serialNo.setText(a);
                String time = null;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                try {
                    Calendar temp = ecb.getCalendarTime();
                    time = timeFormat.format(temp.getTime());
                    temp.add(Calendar.MINUTE,ecb.eventDuration);
                    time += " - ";
                    time += timeFormat.format(temp.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                holder.eventTime.setText(time);
                holder.eventName.setText(ecb.eventName);
                holder.courseName.setText(ecb.courseName);
                holder.credits.setText(ecb.credits);
                holder.venue.setText(ecb.eventVenue);
                holder.prof.setText(ecb.prof);


                break;

                // Event end
            case MONTH:
                MonthDisplay md = (MonthDisplay) myEvents.get(position);
                MonthViewHolder monthHolder = (MonthViewHolder) viewHolder;
                monthHolder.monthName.setText(md.month_name);
                break;
            case DATE:
                Event e  = (Event) myEvents.get(position);
                DateViewHolder dateHolder = (DateViewHolder) viewHolder;
                dateHolder.dateName.setText(e.getDate_name());
                break;

            default:
                    Log.d(TAG,"SomeThings Wrong.");
                    break;

        }



    }

    @Override
    public int getItemCount() {
        return myEvents.size();
    }

    @Override
    public int getItemViewType(int position){
       // Log.d(TAG,"This function is called for " + position + " Type : " + myEvents.get(position).getType());
        if(myEvents.get(position).getType() == Event.EVENT_CACHE_BUILDER){
           // Log.d(TAG,"Replied with event");
            return EVENT;
        }
        else if(myEvents.get(position).getType() == Event.MONTH_DISPLAY){
        //    Log.d(TAG,"Replied with month");
            return MONTH;
        }
        else if(myEvents.get(position).getType() == Event.DATE_DISPLAY){
         //   Log.d(TAG,"Replied with date");
            return DATE;
        }
        else
            return -1;
    }
}
