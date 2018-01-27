package iitg.cestrum.cbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        new Thread(new Runnable() {
            @Override
            public void run() {
                EventsUpdateHandler handler =  new EventsUpdateHandler(getApplicationContext());
                try {
                    handler.loadFirst();
                    Log.d("CesBook"," First Data Loaded");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    handler.close();
                }

            }
        }).start();

        Button btn = (Button) findViewById(R.id.tte);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getApplicationContext(),AgendaViewEndless.class);
                startActivity(i1);
            }


        });
        Button bt = (Button) findViewById(R.id.dayWise);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getApplicationContext(),AgendaViewDayVise.class);
                startActivity(i1);
            }


        });
    }
}
