package iitg.cestrum.cbook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AgendaViewDayVise extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int num_pages = 11;
    private static final String TAG = "CesBook" ;
    private ViewPager viewPager;
    public AgendaViewPagerAdapter pagerAdapter;
    private static Calendar date = Calendar.getInstance();
    public int prePage,loopCount = 0;
    private EventsDayWiseUpdateHandler handler;
    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_view_day_vise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Day wise agenda");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handler = new EventsDayWiseUpdateHandler();
        viewPager = (ViewPager) findViewById(R.id.agenda_day_wise_view_pager);
        pagerAdapter = new AgendaViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(6,false);
        handler.setCurrentDate(-6);
        prePage = 6;





        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {



            @Override
            public void onPageSelected(int position) {


                if(prePage > position){

                    if(position == 0)
                        loopCount--;


                }
                else if(prePage < position){
                    if(position == 12)
                        loopCount++;

                }

                prePage = position%12;
           //     Log.d(TAG,"Pre page = " + prePage + " position = " + position + " After prePage = " + prePage);
                handleSetCurrentItemWithDelay(position);
                super.onPageSelected(position -1);


            }

            private void handleSetCurrentItemWithDelay(final int position) {
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int lastPosition = viewPager.getAdapter().getCount() - 1;
                        Log.d(TAG,"Adapter count = " + lastPosition + " position = " + position);
                        if(position == 0) {
                            viewPager.setCurrentItem(lastPosition - 1, false);
                        } else if(position == lastPosition) {
                            viewPager.setCurrentItem(1, false);
                        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.agenda_view_day_vise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public class AgendaViewPagerAdapter extends FragmentStatePagerAdapter {

        AgendaViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if(position == 0) {
                return AgendaViewDayWiseFragment.create(num_pages,handler.getDate(position + loopCount*11));
            }
            else if(position == num_pages + 1){
                return AgendaViewDayWiseFragment.create(1,handler.getDate(position + loopCount*11));
            }
            else{
                return AgendaViewDayWiseFragment.create(position,handler.getDate(position + loopCount*11));
            }

            //Log.d(TAG,"Pre page = " + prePage + " position = " + position);
            //return AgendaViewDayWiseFragment.create(position,handler.getDate(position + loopCount*11));
        }

        @Override
        public int getCount() {
            return num_pages + 2;
        }



    }



}
