package iitg.cestrum.cbook;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static iitg.cestrum.cbook.MainActivity.TAG;

/**
 * Created by vikas on 07-01-2018.
 */

public class agendaViewScrollListener extends RecyclerView.OnScrollListener {

    private agendaViewAdapter adapter;
    private Calendar sdate,edate;
    private ArrayList<Event> events;
    private DBaseHandler dBaseHandler;
    private int eStartSize,eEndSize , sStartSize;
    private RecyclerView recyclerView;
    private int previousItemCount = 0;
    private int mTotalEntries;
    private boolean mLoading;

    public agendaViewScrollListener(DBaseHandler dBaseHandler,RecyclerView recyclerView, agendaViewAdapter adapter, ArrayList<Event> events, Calendar calendar){
        this.adapter = adapter;
        this.dBaseHandler = dBaseHandler;
        this.events = events;
        this.recyclerView = recyclerView;
        this.edate= (Calendar) calendar.clone();
        edate.add(Calendar.DATE,2 * DBaseHandler.eventGenerateRange);
        this.sdate = (Calendar) calendar.clone();
        sdate.add(Calendar.DATE,-2 * DBaseHandler.eventGenerateRange);
    }
    public void setTotalEntries(int totalEntries) {
        mTotalEntries = totalEntries;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //Log.v(TAG,"On Scroll Called");

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int visibleThreshold = 6;
        if (totalItemCount <= lastVisibleItem + visibleThreshold) {
            updateAtLast();
        }
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItem <= visibleThreshold && mLoading) {
            mLoading = false;
            updateAtFirst();
        }
    }


    /*@Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            int diffCurrentFromPrevious = totalItemCount - previousItemCount;

            // check if current total is greater than previous (diff should be greater than 1, for considering placeholder)
            // and if current total is equal to the total in server
            if ((diffCurrentFromPrevious > 1) ||
                    totalItemCount >= mTotalEntries) {
                mLoading = false;
                previousItemCount = totalItemCount;
            }
        } else {

            if (totalItemCount >= mTotalEntries) {
                // do nothing, we've reached the end of the list
            } else {
                // check if the we've reached the end of the list,
                // and if the total items is less than the total items in the server
                if ((firstVisibleItem + visibleItemCount) >= totalItemCount &&
                        totalItemCount < mTotalEntries) {
                    //onLoadMore(++current_page);

                    mLoading = true;
                    previousItemCount = totalItemCount;
                }
            }
        }
    }*/


    public void updateAtFirst() {
       // Log.v(TAG,"On update at last Called");

        try {
            ArrayList<Event> eve = dBaseHandler.generateEvents(sdate,0);
            sStartSize = eve.size();
            events.addAll(0,eve);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sdate.add(Calendar.DATE,-2 * DBaseHandler.eventGenerateRange);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeInserted(0,sStartSize);
            }
        });

    }

    public void updateAtLast() {

        //    Log.v(TAG,"On update at last Called");
            eStartSize = events.size();
            try {
                events.addAll(dBaseHandler.generateEvents(edate,0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            eEndSize = events.size();
            edate.add(Calendar.DATE,2 * DBaseHandler.eventGenerateRange);

            recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeInserted(eStartSize,eEndSize);
            }
            });


    }
}
