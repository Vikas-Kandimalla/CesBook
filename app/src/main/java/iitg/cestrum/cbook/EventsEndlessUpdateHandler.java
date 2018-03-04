package iitg.cestrum.cbook;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vikas on 08-01-2018.
 * Deals with updating the events from the database with out activity dealing with it
 *
 */

 class EventsEndlessUpdateHandler {
    // used to update the events

    private static final String TAG = "CesBook";
    private static Calendar edate;
    private static Calendar sdate;
    private static ArrayList<Event> events = new ArrayList<>(200);
    private static Calendar nowDate;
    private RecyclerView recyclerView;

    private DBaseHandler dBaseHandler;
    private static boolean firstDataLoaded =  false;
    private int position;
    private int weekPosition,eStartSize,eEndSize,sEndSize;
    private static boolean scrollingProgramatically = false;

    static boolean dataLoaded = false;





    EventsEndlessUpdateHandler(DBaseHandler handler, RecyclerView recyclerView){

        this.recyclerView = recyclerView;
        dBaseHandler = handler;
        //dBaseHandler.resetTempCache();
        sdate = Calendar.getInstance();
        sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -2);
        edate = Calendar.getInstance();
        edate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * 2);
        nowDate = Calendar.getInstance();
     //   new loadFirstThread().start();

    }

    EventsEndlessUpdateHandler(DBaseHandler handler){

        dBaseHandler = handler;
        nowDate = Calendar.getInstance();
    }

    void close() {
        Log.d(TAG,"Data base closed");
        dBaseHandler.close();
    }


    synchronized void refreshEvents() {
        try {

            sdate = Calendar.getInstance();
            sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -2);
            edate = Calendar.getInstance();
            edate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * 2);
            nowDate = Calendar.getInstance();
            ArrayList<Event> temp = dBaseHandler.generateEvents(nowDate);
            events.clear();
            addEvents(temp,false);

            if(recyclerView != null){
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        scrollToDate(Calendar.getInstance().getTime());
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void addEvents(ArrayList<Event> e, boolean atStart){
        if( e != null){
            if(atStart)
                events.addAll(0,e);
            else
                events.addAll(e);
        }
    }

    ArrayList<Event> getEvents() {
        return EventsEndlessUpdateHandler.events;
    }

    synchronized ArrayList<Event> loadFirst() throws Exception {
        dataLoaded = false;
        if(!firstDataLoaded){
            ArrayList<Event> temp = dBaseHandler.generateEvents(nowDate);
            addEvents(temp,false);
            Log.d(TAG,"Load First is called");
            firstDataLoaded = true;

        }
        dataLoaded = true;
        return EventsEndlessUpdateHandler.events;
    }

    boolean isFirstDataLoaded() {
        return  firstDataLoaded;
    }

    void loadNext(agendaViewAdapter adapter) {
        dataLoaded = false;
        if(adapter == null) {
            dataLoaded = true;
            Log.e(TAG,"Should call this after setting the adapter");
        }
        else {
            new loadNextThread(adapter).start();
            //dataLoaded = true;
        }
    }

    void loadPrev(agendaViewAdapter adapter) {
        dataLoaded = false;
        if(adapter == null) {
            dataLoaded = true;
            Log.e(TAG,"Should call this after setting the adapter");
        }
        else {
            Log.d(TAG,"Load Prev is called");
            new loadPrevThread(adapter).start();
            //dataLoaded = true;
        }
    }

    private  class loadPrevThread extends  Thread {

        private agendaViewAdapter adapter;
        private int sEndSize = 0;
        loadPrevThread(agendaViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public synchronized void run() {
            ArrayList<Event> eve;
            try {
                eve = dBaseHandler.generateEvents(sdate);
                sEndSize = eve.size();
              //  Log.d(TAG,"Start end size = " + eve.size() + " returned events = " + eve.toString());
                sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -2);
                addEvents(eve,true);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyItemRangeInserted(0,sEndSize);
                        //  adapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }






            dataLoaded = true;
           // Log.d(TAG,"Loading completed");
        }
    }

    private  class loadNextThread extends  Thread {

        private int eStartSize,eEndSize;
        private agendaViewAdapter adapter;
        loadNextThread(agendaViewAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public synchronized void run() {
            eStartSize = events.size();
            try {

                addEvents(dBaseHandler.generateEvents(edate),false);
                eEndSize = events.size();
                edate.add(Calendar.DATE,2 * DBaseHandler.eventGenerateRange);


                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyItemRangeInserted(eStartSize,eEndSize - eStartSize);
                        //  adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }




            dataLoaded = true;
        }
    }

    synchronized void scrollToMonth(Date date){
        scrollingProgramatically = true;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        Calendar tempEDate = (Calendar) edate.clone();
        Calendar tempSDate = (Calendar) sdate.clone();

        tempEDate.add(Calendar.DATE,-1 * DBaseHandler.eventGenerateRange);
        tempSDate.add(Calendar.DATE,DBaseHandler.eventGenerateRange);


        if((tempEDate.compareTo(c) > 0) && (c.compareTo(tempSDate) > 0)){
            Log.d(TAG,"Edate = " + DBaseHandler.calendarToString(edate) + " Sdate = " + DBaseHandler.calendarToString(sdate));
            int position = events.indexOf(new MonthDisplay(DBaseHandler.months[month]  + " " + c.get(Calendar.YEAR)));
            Log.d(TAG,"This is running " + position + " Month = " + DBaseHandler.months[month]);
            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
        }
        else if( tempEDate.compareTo(c) <= 0 && dataLoaded){

             eStartSize = events.size();
            try {
                addEvents(dBaseHandler.generateEvents(edate),false);
                 eEndSize = events.size();
                edate.add(Calendar.DATE,2 * DBaseHandler.eventGenerateRange);
                position = events.indexOf(new MonthDisplay(DBaseHandler.months[month] + " " + c.get(Calendar.YEAR)));
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyItemRangeInserted(eStartSize,eEndSize - eStartSize);
                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(c.compareTo(tempSDate) <= 0 && dataLoaded){


            ArrayList<Event> eve;
            try {
                eve = dBaseHandler.generateEvents(sdate);
                sEndSize = eve.size();
                sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -2);
                addEvents(eve,true);
                position = events.indexOf(new MonthDisplay(DBaseHandler.months[month]  + " " + c.get(Calendar.YEAR)));

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyItemRangeInserted(0,sEndSize);
                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        scrollingProgramatically = false;
    }

    synchronized void scrollToDate(Date date){
        scrollingProgramatically = true;
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar tempEDate = (Calendar) edate.clone();
        Calendar tempSDate = (Calendar) sdate.clone();

        tempEDate.add(Calendar.DATE,-1 * DBaseHandler.eventGenerateRange);
        tempSDate.add(Calendar.DATE,DBaseHandler.eventGenerateRange);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        String id = String.format(Locale.ENGLISH,"%02d-%4d",week,c.get(Calendar.YEAR));
        Event dateView = new Event(DBaseHandler.calendarToString(c));
        Event weekView = new Event(true,id,"Blabla");

        if((tempEDate.compareTo(c) > 0) && (c.compareTo(tempSDate) > 0)){

            int position = events.indexOf(dateView);
            if(position != -1){
                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
            }
            else{
                position = events.indexOf(weekView);
                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
            }
        }
        else if( tempEDate.compareTo(c) <= 0 && dataLoaded){

            eStartSize = events.size();
            try {
                addEvents(dBaseHandler.generateEvents(edate),false);
                eEndSize = events.size();
                edate.add(Calendar.DATE,2 * DBaseHandler.eventGenerateRange);
                position = events.indexOf(dateView);
                weekPosition = events.indexOf(weekView);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyItemRangeInserted(eStartSize,eEndSize - eStartSize);

                        if(position != -1){
                            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
                        }
                        else{
                            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(weekPosition, 10);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(c.compareTo(tempSDate) <= 0 && dataLoaded){


            ArrayList<Event> eve;
            try {
                eve = dBaseHandler.generateEvents(sdate);
                sEndSize = eve.size();
                sdate.add(Calendar.DATE,DBaseHandler.eventGenerateRange * -2);
                addEvents(eve,true);
                position = events.indexOf(dateView);
                weekPosition = events.indexOf(weekView);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyItemRangeInserted(0,sEndSize);

                        if(position != -1){
                            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
                        }
                        else{

                            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 10);
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    scrollingProgramatically = false;
    }

    public boolean isScrolledProgramatically(){
        return scrollingProgramatically;
    }

}
