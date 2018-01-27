package iitg.cestrum.cbook;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
 * Use the {@link AgendaViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CesBook";
    private RecyclerView recyclerView;
    private static ArrayList<Event> events;
    private agendaViewAdapter adapter;
    private EventsUpdateHandler updateHandler;
    private agendaViewScrollListener scrollListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendaViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendaViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaViewFragment newInstance(String param1, String param2) {
        AgendaViewFragment fragment = new AgendaViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_agenda_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment_event_view);
        ActionBar actionBar = getActivity().getActionBar();
        updateHandler = new EventsUpdateHandler(getContext(),recyclerView);
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
        scrollListener = new agendaViewScrollListener(getContext(), recyclerView, adapter, getActivity());
        recyclerView.addOnScrollListener(scrollListener);
        setToToday();

    return view;

    }


    @Override
    public void onViewCreated(View view , Bundle savedInstance){
        super.onViewCreated(view , savedInstance);

    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        updateHandler.close();

    }

    public void setToToday(){

        AgendaViewEndless t = ((AgendaViewEndless) getActivity());
        t.setToTodayClicked = true;
        int currentDatePosition = events.indexOf(new Event(DBaseHandler.calendarToString(Calendar.getInstance())));

        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItem  = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();


        if ( firstVisibleItem < currentDatePosition || lastVisibleItem < currentDatePosition){
            //PointF p = mLinearLayoutManager.computeScrollVectorForPosition(currentDatePosition);
            //recyclerView.scrollBy(((int)p.x),((int) p.y));
            mLinearLayoutManager.scrollToPositionWithOffset(currentDatePosition,20);
            recyclerView.stopScroll();
           // int visible = mLinearLayoutManager.getChildCount();
           // recyclerView.scrollToPosition(currentDatePosition + visible);
        }
        else
            recyclerView.scrollToPosition(currentDatePosition);

        Calendar cc = Calendar.getInstance();
        String title = DBaseHandler.mon[cc.get(Calendar.MONTH)] + " " + cc.get(Calendar.YEAR);
        if (t.getSupportActionBar() != null)
            t.getSupportActionBar().setTitle(title);

        MaterialCalendarView mcv = (MaterialCalendarView) t.findViewById(R.id.agenda_view_calendar);
        mcv.setCurrentDate(cc);

        t.setToTodayClicked = false;
    }

    public void disableOnScrollListener() {
        recyclerView.removeOnScrollListener(scrollListener);
    }

    public void enableOnScrollListener() {
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void scrollToMonth(Date d){
        SimpleDateFormat dtf = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        Log.d(TAG,"Yu hooo, This is working " + dtf.format(d));
        updateHandler.scrollToMonth(d);
    }

    public void scrollToDate(Date date){
        updateHandler.scrollToDate(date);
    }

    public Calendar getCurrentDate() {
        return agendaViewScrollListener.currentDate;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
