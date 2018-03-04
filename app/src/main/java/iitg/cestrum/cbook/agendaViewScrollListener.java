package iitg.cestrum.cbook;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static iitg.cestrum.cbook.AgendaViewEndlessFragment.TAG;

/**
 * Created by vikas on 07-01-2018.
 */

public class agendaViewScrollListener extends RecyclerView.OnScrollListener {

    private agendaViewAdapter adapter;
    private RecyclerView recyclerView;
    private EventsEndlessUpdateHandler updateHandler;
    private AgendaViewEndlessFragment fragment;
    static Calendar currentDate = Calendar.getInstance();

    agendaViewScrollListener(DBaseHandler handler, RecyclerView recyclerView, agendaViewAdapter adapter, AgendaViewEndlessFragment fragment ){
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.fragment = fragment;
        updateHandler = new EventsEndlessUpdateHandler(handler,this.recyclerView);

    }






    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int threshold = 15;
        int visibleItemCount = recyclerView.getChildCount();

        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();



        if( ! fragment.isCalenderVisible && ! fragment.setToTodayClicked) {
            ArrayList<Event> events = updateHandler.getEvents();
            Event e = events.get(firstVisibleItem);
            String date;
            switch (e.getType()) {
                case Event.EVENT_CACHE_BUILDER:
                    date = ((EventCacheBuilder) e).eventDate;
                    break;

                case Event.DATE_DISPLAY:
                    date = e.getDate_name();
                    break;

                case Event.MONTH_DISPLAY:
                    date = ((MonthDisplay) e).month_date;
                    break;

                case Event.WEEK_DISPLAY:
                    date = e.getWeek_date();
                    break;

                default:
                    date = null;
                    break;
            }

            if (date != null) {
                try {
                    Calendar cc = DBaseHandler.stringToCalender(date);
                    currentDate = cc;
                    String title = DBaseHandler.mon[cc.get(Calendar.MONTH)] + " " + cc.get(Calendar.YEAR);
                        fragment.actionBar.setTitle(title);
                 //   MaterialCalendarView mcw = (MaterialCalendarView) activity.findViewById(R.id.agenda_view_calendar);
                 //   mcw.setCurrentDate(CalendarDay.from(cc));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


            }
        }

        int lastVisibleItem  = mLinearLayoutManager.findLastVisibleItemPosition();
        if(EventsEndlessUpdateHandler.dataLoaded){

            if(lastVisibleItem >= totalItemCount - threshold && !recyclerView.isComputingLayout()){
                updateHandler.loadNext(adapter);
                Log.d(TAG,"Load at last last visible item = " + lastVisibleItem + " total item count = " + totalItemCount);
            }
            if(firstVisibleItem <= threshold && !recyclerView.isComputingLayout()){
                updateHandler.loadPrev(adapter);
                Log.d(TAG,"Load at first. First visible item count = " + firstVisibleItem + " total item count = " + totalItemCount);
            }


        }
    }







}
