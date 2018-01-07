package iitg.cestrum.cbook;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vikas on 29-07-2017.
 * New Commit
 */

public class AvEventView {


    private Context context = null;
    private String id = null ,name = null ,time = null ,venue = null ,prof = null ,courseName = null ,credits = null ,date = null ,serialNo = null ;
    private int duration;
    private AvEvent av_event = new AvEvent();


    AvEventView(Context context, String id, String name, String time , String venue, String prof, String courseName, String credits, String date, String serialNo){

        this.context = context;
        this.id = id;
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.prof = prof;
        this.courseName = courseName;
        this.credits = credits;
        this.date = date;
        this.serialNo = serialNo;
        av_event.mainId = id;


    }

    public AvEventView(Context context,EventCacheBuilder eve, String serialNo){
        this.context = context;
        this.id = eve.ID;
        this.name = eve.eventName;
        this.date = eve.eventDate;
        this.time = eve.eventTime;
        this.duration = eve.eventDuration;
        this.courseName = eve.courseName;
        this.credits = eve.credits;
        this.prof = eve.prof;
        this.venue = eve.eventVenue;
        this.serialNo = serialNo;
        av_event.mainId = id;

    }

    public View getView() {

        av_event.verticalHeader = RelativeLayout.generateViewId();

        // This is the main layout



        RelativeLayout avEventId = new RelativeLayout(this.context);
        RelativeLayout.LayoutParams myParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        myParams.setMargins(5,5,5,5);
        avEventId.setLayoutParams(myParams);

        av_event.id = RelativeLayout.generateViewId();

        avEventId.setId(av_event.id);
        avEventId.setBackgroundColor(Color.parseColor("#b3c6ff"));

        // main layout end


        // head relative layout

        RelativeLayout avEventIdHead = new RelativeLayout(this.context);
        RelativeLayout.LayoutParams myParams1 =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        myParams1.setMargins(0,5,5,0);
        myParams1.addRule(RelativeLayout.END_OF,av_event.verticalHeader);
        avEventIdHead.setLayoutParams(myParams1);

        av_event.head = RelativeLayout.generateViewId();

        avEventIdHead.setId(av_event.head);
        avEventIdHead.setBackgroundColor(Color.parseColor("#d8d8d8"));



        // head relative layout end

        // course relative layout

        TextView avEventCourse = new TextView(this.context);
        RelativeLayout.LayoutParams paramCourse = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramCourse.addRule(RelativeLayout.ALIGN_PARENT_END);
        avEventCourse.setLayoutParams(paramCourse);

        av_event.name = TextView.generateViewId();

        avEventCourse.setId(av_event.name);
        avEventCourse.setPadding(5,5,5,5);
        avEventCourse.setText((this.name != null)? this.name : "EE 340");
        avEventCourse.setTextColor(Color.parseColor("#000000"));
        avEventCourse.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);



        avEventIdHead.addView(avEventCourse);
        // course relative layout


        // time relative layout

        TextView avEventTime = new TextView(this.context);
        RelativeLayout.LayoutParams paramTime = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramTime.addRule(RelativeLayout.ALIGN_PARENT_START);
        avEventTime.setLayoutParams(paramTime);

        av_event.time = TextView.generateViewId();

        avEventTime.setId(av_event.time);
        avEventTime.setPadding(5,5,5,5);
        avEventTime.setText((this.time != null)? this.time : "8:30 - 9:30");
        avEventTime.setTextColor(Color.parseColor("#000000"));
        avEventTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);



        avEventIdHead.addView(avEventTime);
        // end time relative layout


        // relative layout body


        RelativeLayout avEventIdBody = new RelativeLayout(this.context);
        RelativeLayout.LayoutParams ParamsBody =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ParamsBody.setMargins(0,5,5,8);
        ParamsBody.addRule(RelativeLayout.ALIGN_START,av_event.head);
        ParamsBody.addRule(RelativeLayout.BELOW,av_event.head);
        avEventIdBody.setLayoutParams(ParamsBody);

        av_event.body = RelativeLayout.generateViewId();

        avEventIdBody.setId(av_event.body);
        avEventIdBody.setBackgroundColor(Color.parseColor("#ffffff"));



        // relative layout body end

        // venue
        TextView avEventVenue = new TextView(this.context);
        RelativeLayout.LayoutParams paramVenue = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramVenue.addRule(RelativeLayout.ALIGN_PARENT_START);
        avEventVenue.setLayoutParams(paramVenue);

        av_event.venue = TextView.generateViewId();

        avEventVenue.setId(av_event.venue);
        avEventVenue.setPadding(5,5,5,5);
        avEventVenue.setText((this.venue != null )?this.venue:"2201");
        avEventVenue.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


        TextView avEventProf = new TextView(this.context);
        RelativeLayout.LayoutParams paramProf = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramProf.addRule(RelativeLayout.ALIGN_PARENT_END);
        avEventProf.setLayoutParams(paramProf);

        av_event.prof = TextView.generateViewId();

        avEventProf.setId(av_event.prof);
        avEventProf.setPadding(5,5,5,5);
        avEventProf.setText((this.prof != null)?this.prof : "Prof. Srinivas");
        avEventProf.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        TextView avEventCourseName = new TextView(this.context);
        RelativeLayout.LayoutParams paramCName = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramCName.addRule(RelativeLayout.BELOW,av_event.venue);
        avEventCourseName.setLayoutParams(paramCName);

        av_event.courseName = TextView.generateViewId();

        avEventCourseName.setId(av_event.courseName);
        avEventCourseName.setPadding(5,5,5,5);
        avEventCourseName.setText((this.courseName != null ) ? this.courseName : "Control Systems");
        avEventCourseName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        TextView avEventCredits = new TextView(this.context);
        RelativeLayout.LayoutParams paramCredits = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramCredits.addRule(RelativeLayout.BELOW,av_event.venue);
        paramCredits.addRule(RelativeLayout.ALIGN_END,av_event.prof);
        avEventCredits.setLayoutParams(paramCredits);

        av_event.credits = TextView.generateViewId();

        avEventCredits.setId(av_event.credits);
        avEventCredits.setPadding(5,5,5,5);
        avEventCredits.setText((this.credits != null) ? this.credits : "3-0-0-6");
        avEventCredits.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


        // vertical header

        RelativeLayout avEventVerticalHeader = new RelativeLayout(this.context);
        RelativeLayout.LayoutParams ParamsVerticalHeader =  new RelativeLayout.LayoutParams(50, RelativeLayout.LayoutParams.MATCH_PARENT);
        ParamsVerticalHeader.addRule(RelativeLayout.ALIGN_BOTTOM,av_event.body);
        ParamsVerticalHeader.addRule(RelativeLayout.ALIGN_TOP,av_event.head);
        avEventVerticalHeader.setLayoutParams(ParamsVerticalHeader);

        avEventVerticalHeader.setId(av_event.verticalHeader);
        avEventVerticalHeader.setBackgroundColor(Color.parseColor("#3d3d3d"));

        TextView avEventSerialNo = new TextView(this.context);
        RelativeLayout.LayoutParams paramSerialNo = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramSerialNo.addRule(RelativeLayout.CENTER_IN_PARENT);
        avEventSerialNo.setLayoutParams(paramSerialNo);

        avEventSerialNo.setId(av_event.serialNo);
        avEventSerialNo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        avEventSerialNo.setText(this.serialNo);
        avEventSerialNo.setTextColor(Color.parseColor("#ffffff"));
        avEventSerialNo.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        avEventVerticalHeader.addView(avEventSerialNo);

        // vertical header end

        avEventIdBody.addView(avEventVenue);
        avEventIdBody.addView(avEventProf);
        avEventIdBody.addView(avEventCourseName);
        avEventIdBody.addView(avEventCredits);

        avEventId.addView(avEventVerticalHeader);
        avEventId.addView(avEventIdBody);
        avEventId.addView(avEventIdHead);



        return avEventId;

    }


    public AvEvent getEventId() {

        return this.av_event;

    }


}
