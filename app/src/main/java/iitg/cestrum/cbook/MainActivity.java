package iitg.cestrum.cbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CamBook";
    AgendaViewEndlessFragment schedule;
    AgendaViewDayWiseFragmentContainer daywise;
    public DBaseHandler dBaseHandler;
    public NotificationDatabaseHandler notificationHandler;
    private FirebaseAuth mAuth;
    private String name = "user";
    private String email = "user@iitg.ernet.in";
    private TextView tname,temail;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"Message received");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dBaseHandler = new DBaseHandler(getApplicationContext());
        notificationHandler = new NotificationDatabaseHandler(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
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
        View headerView = navigationView.getHeaderView(0);
        tname = (TextView) headerView.findViewById(R.id.nav_name);
        temail = (TextView) headerView.findViewById(R.id.nav_email);

        schedule = new AgendaViewEndlessFragment();
        daywise = new AgendaViewDayWiseFragmentContainer();
        getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container,daywise)
                                    .commit();



        startService(new Intent(getApplicationContext(),EventsUpdateService.class));



    }

    @Override
    public void onResume(){

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null ){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        else if (! user.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if(user != null) {
            email = user.getEmail();
            temail.setText(email);
            firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, documentSnapshot.getData().toString());
                        name = ((String) documentSnapshot.getData().get("firstName")) + " " + ((String) documentSnapshot.getData().get("LastName"));
                        tname.setText(name);
                    }
                }
            });
        }
        super.onResume();
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current instanceof AgendaViewEndlessFragment){
            Log.d("CesBook","This is called");
            ((AgendaViewEndlessFragment)current).setToToday();
        }







    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }


    @Override
    public void onDestroy(){
        dBaseHandler.close();
        notificationHandler.close();
        super.onDestroy();

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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_schedule) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.replace(R.id.fragment_container, new AgendaViewEndlessFragment());
            transaction.commit();
            // Handle the camera action
        } else if (id == R.id.nav_day_wise) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.replace(R.id.fragment_container, new AgendaViewDayWiseFragmentContainer());
            transaction.commit();

        } else if (id == R.id.nav_notifications) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.replace(R.id.fragment_container, new NotificationsFragment());
            transaction.commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
