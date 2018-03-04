package iitg.cestrum.cbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  interface
 * to handle interaction events.
 * Use the {@link AgendaViewEndlessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaViewEndlessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String TAG = "EndlessFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private static ArrayList<Event> events;
    private agendaViewAdapter adapter;
    private EventsEndlessUpdateHandler updateHandler;
    private agendaViewScrollListener scrollListener;
    public android.support.v7.app.ActionBar actionBar;
    public boolean isCalenderVisible = false;
    public boolean setToTodayClicked = false;
    private MaterialCalendarView mcv;
    private BroadcastReceiver receiver;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendaViewEndlessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendaViewEndlessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaViewEndlessFragment newInstance(String param1, String param2) {
        AgendaViewEndlessFragment fragment = new AgendaViewEndlessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        receiver = new  BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG,"Receiver called For endless fragment");
                if (updateHandler != null){
                    if(scrollListener != null)
                        recyclerView.removeOnScrollListener(scrollListener);
                    updateHandler.refreshEvents();
                    if (scrollListener != null)
                        recyclerView.addOnScrollListener(scrollListener);
                }

            }
        };



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_agenda_view_endless, container, false);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_main);
        actionBar =  ((MainActivity)getActivity()).getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment_event_view);
        updateHandler = new EventsEndlessUpdateHandler(((MainActivity) getActivity()).dBaseHandler,recyclerView);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver ,new IntentFilter("EVENTS_UPDATED"));
        Log.d(TAG,"Receiver Created");

        try {
            if(updateHandler.isFirstDataLoaded())
                events = updateHandler.getEvents();
            else
                events = updateHandler.loadFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new agendaViewAdapter(getContext(),events);
        Log.d(TAG,events.toString());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(15);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView.setAdapter(adapter);
        scrollListener = new agendaViewScrollListener(((MainActivity)getActivity()).dBaseHandler, recyclerView, adapter, this);



    return view;

    }


    @Override
    public void onViewCreated(final View view , Bundle savedInstance){
        super.onViewCreated(view , savedInstance);
        mcv = (MaterialCalendarView) view.findViewById(R.id.fragment_agenda_view_calendar);
        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Date d = date.getDate();
                SimpleDateFormat dtf = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                final String toolbarHeader = dtf.format(date.getDate());
                actionBar.setTitle(toolbarHeader);
                //toolbarTitle.setText(toolbarHeader);
                if(isCalenderVisible)
                    scrollToMonth(d);
            }
        });

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(isCalenderVisible)
                    scrollToDate(date.getDate());
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout r = (LinearLayout) view.findViewById(R.id.fragment_agenda_view_calendar_container);
                if(r.getVisibility() == LinearLayout.GONE){
                    mcv.post(new Runnable() {
                        @Override
                        public void run() {
                            mcv.setCurrentDate(getCurrentDate());
                        }
                    });
                    r.setVisibility(LinearLayout.VISIBLE);
                    isCalenderVisible = true;
                    disableOnScrollListener();

                }
                else if(r.getVisibility() == LinearLayout.VISIBLE){
                    r.setVisibility(LinearLayout.GONE);
                    isCalenderVisible = false;
                    enableOnScrollListener();
                }

            }
        });

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //setToToday();
    }

    @Override
    public void onResume(){
        super.onResume();
        recyclerView.addOnScrollListener(scrollListener);
        setToToday();

    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.removeOnScrollListener(scrollListener);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView(){
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        Log.d(TAG,"Receiver Destroyed for endless events");
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_agenda_endless,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar_today) {
            setToToday();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setToToday(){


        if(events != null && events.size() > 0) {
            int currentDatePosition = events.indexOf(new Event(DBaseHandler.calendarToString(Calendar.getInstance())));

            if (currentDatePosition == -1){
                Calendar c = Calendar.getInstance();
                String id = String.format(Locale.ENGLISH,"%02d-%4d",c.get(Calendar.WEEK_OF_YEAR),c.get(Calendar.YEAR));
                Event weekView = new Event(true,id,"Blabla");
                currentDatePosition = events.indexOf(weekView);
            }

            LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();


            if (firstVisibleItem < currentDatePosition || lastVisibleItem < currentDatePosition) {
                mLinearLayoutManager.scrollToPositionWithOffset(currentDatePosition, 20);
                recyclerView.stopScroll();
            } else
                recyclerView.scrollToPosition(currentDatePosition);

            Calendar cc = Calendar.getInstance();
            String title = DBaseHandler.mon[cc.get(Calendar.MONTH)] + " " + cc.get(Calendar.YEAR);
            //toolbarTitle.setText(title);
            actionBar.setTitle(title);
            mcv.setCurrentDate(cc);

            this.setToTodayClicked = false;
        }
    }

    public void disableOnScrollListener() {
        recyclerView.removeOnScrollListener(scrollListener);
    }

    public void enableOnScrollListener() {
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void scrollToMonth(Date d){
        updateHandler.scrollToMonth(d);
    }

    public void scrollToDate(Date date){
        updateHandler.scrollToDate(date);
    }

    public Calendar getCurrentDate() {
        return agendaViewScrollListener.currentDate;
    }




}
