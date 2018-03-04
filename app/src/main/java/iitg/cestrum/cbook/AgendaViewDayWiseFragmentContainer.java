package iitg.cestrum.cbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AgendaViewDayWiseFragmentContainer extends Fragment {

    public  static final String TAG = "AgendaViewDayWise";
    private static final int num_pages = 11;
    private ViewPager viewPager;
    public  static AgendaViewPagerAdapter pagerAdapter;
    //private static Calendar date = Calendar.getInstance();
    //public  int prePage = 6,loopCount = 0;
    private EventsDayWiseUpdateHandler handler = new EventsDayWiseUpdateHandler();
    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.ENGLISH);



    public AgendaViewDayWiseFragmentContainer() {
        // Required empty public constructor
    }


    public static AgendaViewDayWiseFragmentContainer newInstance() {
        return new AgendaViewDayWiseFragmentContainer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handler = new EventsDayWiseUpdateHandler();
        pagerAdapter = new AgendaViewPagerAdapter(getActivity().getSupportFragmentManager());
        //handler.setCurrentDate(-6);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda_view_day_wise_fragment_container, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstance){

        viewPager = (ViewPager) view.findViewById(R.id.agenda_day_wise_view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(6, false);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {



                if(EventsDayWiseUpdateHandler.prePage > position){

                    if(position == 0)
                        EventsDayWiseUpdateHandler.loopCount--;


                }
                else if(EventsDayWiseUpdateHandler.prePage < position){
                    if(position == num_pages + 1)
                        EventsDayWiseUpdateHandler.loopCount++;

                }

                EventsDayWiseUpdateHandler.prePage = position%12;
                //     Log.d(TAG,"Pre page = " + prePage + " position = " + position + " After prePage = " + prePage);
                handleSetCurrentItemWithDelay(position);
                super.onPageSelected(position -1);

            }

            private void handleSetCurrentItemWithDelay(final int position) {
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int lastPosition = viewPager.getAdapter().getCount() - 1;
                        //Log.d(TAG,"Adapter count = " + lastPosition + " position = " + position);
                        if( position == 0) {
                            viewPager.setCurrentItem(lastPosition - 1, false);
                        } else if(position == lastPosition) {
                            viewPager.setCurrentItem(1, false);
                        }

                        Calendar date = handler.getDate(viewPager.getCurrentItem() + EventsDayWiseUpdateHandler.loopCount *11);
                        SimpleDateFormat dtf = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                        String header = dtf.format(date.getTime());
                        android.support.v7.app.ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
                        if (actionBar != null)
                            actionBar.setTitle(header);
                    }
                },300);
            }


            @Override
            public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
                //       Log.d(TAG,"Debuging position = " + position + " Position offset = " + positionOffset + " positionOffsetPixels = " + positionOffsetPixels);
                super.onPageScrolled(position-1,positionOffset,positionOffsetPixels);
            }





        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class AgendaViewPagerAdapter extends FragmentStatePagerAdapter {

        AgendaViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) {
                return AgendaViewDayWiseFragment.create(num_pages,handler.getDate(position + EventsDayWiseUpdateHandler.loopCount *11));
            }
            else if(position == num_pages + 1){
                return AgendaViewDayWiseFragment.create(1,handler.getDate(position + EventsDayWiseUpdateHandler.loopCount *11));
            }
            else{
                return AgendaViewDayWiseFragment.create(position,handler.getDate(position + EventsDayWiseUpdateHandler.loopCount *11));
            }

        }

        @Override
        public int getCount() {
            return num_pages + 2;
        }



    }



}
