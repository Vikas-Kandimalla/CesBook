package iitg.cestrum.cbook;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;




public class MainActivity extends AppCompatActivity {
    public static final String TAG = "CesBook";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MyService.class));



        Button iv= (Button) findViewById(R.id.search_bar);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });




    }

    @Override
    protected  void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy(){

        Log.d(TAG,"Service Should Stop.");
        stopService(new Intent(this, MyService.class));
        super.onDestroy();
    }
}
