package iitg.cestrum.cbook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgendaViewEndless extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AgendaViewFragment eventView;
    private MaterialCalendarView mcv;
    private Toolbar toolbar;
    public boolean isCalenderVisible = false;
    public boolean setToTodayClicked = false;
    private String toolbarHeader = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_view_endless);
        new initiateInBackground().start();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







       // if( getSupportActionBar() != null)
                //getSupportActionBar().setTitle("Jan 2018");



        LinearLayout r = (LinearLayout) findViewById(R.id.agenda_view_calendar_container);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mcv = (MaterialCalendarView) findViewById(R.id.agenda_view_calendar);
        //mcv.setTopbarVisible(false);
        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Date d = date.getDate();
                SimpleDateFormat dtf = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                toolbarHeader = dtf.format(date.getDate());
                toolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle(toolbarHeader);
                    }
                });

                if(isCalenderVisible)
                    eventView.scrollToMonth(d);
            }
        });

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(isCalenderVisible)
                    eventView.scrollToDate(date.getDate());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout r = (LinearLayout) findViewById(R.id.agenda_view_calendar_container);
                if(r.getVisibility() == LinearLayout.GONE){
                    mcv.post(new Runnable() {
                        @Override
                        public void run() {
                            mcv.setCurrentDate(eventView.getCurrentDate());
                        }
                    });
                    r.setVisibility(LinearLayout.VISIBLE);
                    isCalenderVisible = true;
                    eventView.disableOnScrollListener();

                }
                else if(r.getVisibility() == LinearLayout.VISIBLE){
                    r.setVisibility(LinearLayout.GONE);
                    isCalenderVisible = false;
                    eventView.enableOnScrollListener();
                }

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
        getMenuInflater().inflate(R.menu.agenda_view_endless, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_calendar_today){
            eventView.setToToday();
            return true;
        }
        else if (id == R.id.action_settings) {
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

    public class initiateInBackground extends Thread {

        @Override
        public void  run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
            eventView = new AgendaViewFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.agenda_fragment,eventView);
            transaction.commit();

        }


    }

    public void scrollToDate(Date date){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_fragment_event_view);

    }
}
