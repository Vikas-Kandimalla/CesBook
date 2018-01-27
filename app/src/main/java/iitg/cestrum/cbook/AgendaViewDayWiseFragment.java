package iitg.cestrum.cbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static iitg.cestrum.cbook.MainActivity.TAG;

/**
 * A simple .
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AgendaViewDayWiseFragment extends Fragment {

    private Calendar date;
    private int position;
    private DBaseHandler dBaseHandler;
    public ListView listView;
    private MyAdapter adapter;

    public AgendaViewDayWiseFragment() {
        // Required empty public constructor
    }

    public static AgendaViewDayWiseFragment create(int position,Calendar date){

        AgendaViewDayWiseFragment fragment = new AgendaViewDayWiseFragment();

        Bundle args = new Bundle();
        args.putLong("Date", date.getTimeInMillis());
        args.putInt("position",position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * A new instance of fragment AgendaViewDayWiseFragment.
     */



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date =  Calendar.getInstance();
            date.setTimeInMillis(getArguments().getLong("Date"));
            position = getArguments().getInt("position");
        }
        dBaseHandler = new DBaseHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_agenda_view_day_wise,container,false);
        TextView textView = rootView.findViewById(R.id.daywiseView);
        SimpleDateFormat dt = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.ENGLISH);
        String temp = dt.format(date.getTime());
        textView.setText(temp);
        listView = (ListView) rootView.findViewById(R.id.agenda_view_day_wise_list_view);
        final ViewPager pager = getActivity().findViewById(R.id.agenda_day_wise_view_pager);
        final PagerAdapter pagerAdapter = ((AgendaViewDayVise) getActivity()).pagerAdapter;
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG,"Day = " + dayOfMonth + " Month = " + month + " year = " + year);
                pagerAdapter.startUpdate(pager);
                EventsDayWiseUpdateHandler handler = new EventsDayWiseUpdateHandler();
                Calendar c = Calendar.getInstance();
                c.set(year,month,dayOfMonth);
                int position = pager.getCurrentItem();
                if(position < 9){
                    position += 2;
                }
                else
                    position -= 2;

                c.add(Calendar.DATE,-1 * position);
                handler.setCurrentDate(c);

                ((AgendaViewDayVise) getActivity()).loopCount = 0;
                ((AgendaViewDayVise) getActivity()).prePage = position;
                pager.setCurrentItem(position);
                pagerAdapter.finishUpdate(pager);

            }
        },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        // update events in to view for the given date;
        try {
            EventCacheBuilder[] e = dBaseHandler.getEventsForDate(date);
            if(e != null) {
                adapter = new MyAdapter(getContext(), R.id.agenda_view_day_wise_list_view, e);
                listView.setAdapter(adapter);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ImageButton next = (ImageButton) rootView.findViewById(R.id.imageButton_forward);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem( pager.getCurrentItem() + 1);
            }
        });
        ImageButton prev = (ImageButton) rootView.findViewById(R.id.imageButton_backward);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem( pager.getCurrentItem() - 1);
            }
        });


        return rootView;
    }



    public Calendar getFragmentDate(){
        return this.date;
    }

    private View getAgendaView(LayoutInflater inflater,ViewGroup container,EventCacheBuilder ecb,int serial){
        View itemView = inflater.inflate(R.layout.agenda_event_view,container,false);
        TextView serialNo,eventTime,eventName,courseName,prof,credits,venue;
        RelativeLayout eventHeader;
        LinearLayout eventContainer;
        eventContainer = (LinearLayout) itemView.findViewById(R.id.event_agenda_view);
        serialNo = (TextView) itemView.findViewById(R.id.serialNo);
        eventName = (TextView) itemView.findViewById(R.id.event_name);
        eventTime = (TextView) itemView.findViewById(R.id.event_time);
        courseName = (TextView) itemView.findViewById(R.id.course_name);
        prof = (TextView) itemView.findViewById(R.id.event_prof);
        credits = (TextView) itemView.findViewById(R.id.event_credits);
        venue = (TextView) itemView.findViewById(R.id.event_venue);
        eventHeader = (RelativeLayout) itemView.findViewById(R.id.event_header);

        String a = String.valueOf(serial);
        serialNo.setText(a);
        switch (ecb.eventName){
            case "EE 102":
               serialNo.setBackgroundColor(Color.parseColor("#e60000"));
               eventHeader.setBackgroundColor(Color.parseColor("#ff9999"));
                break;
            case "EE 350":
               serialNo.setBackgroundColor(Color.parseColor("#004080"));
               eventHeader.setBackgroundColor(Color.parseColor("#99ccff"));
                break;

            default:
               serialNo.setBackgroundColor(Color.parseColor("#3d3d3d"));
               eventHeader.setBackgroundColor(Color.parseColor("#d8d8d8"));
                break;
        }


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


        eventTime.setText(time);
        eventName.setText(ecb.eventName);
        courseName.setText(ecb.courseName);
        credits.setText(ecb.credits);
        venue.setText(ecb.eventVenue);
        prof.setText(ecb.prof);

        return itemView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dBaseHandler.close();
    }

    public class MyAdapter extends ArrayAdapter {

        private Context context;
        private EventCacheBuilder[] obj;

        public MyAdapter(@NonNull Context context, int resource, EventCacheBuilder[] objects) {
            super(context, resource, objects);
            this.context = context;
            this.obj = objects;

        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return getAgendaView(inflater,parent,obj[position],position + 1);

        }

        @Override
        public int getCount(){
            return obj.length;
        }
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
