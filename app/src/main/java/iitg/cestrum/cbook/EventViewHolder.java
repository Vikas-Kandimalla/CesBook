package iitg.cestrum.cbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vikas on 06-01-2018.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView serialNo,eventTime,eventName,courseName,prof,credits,venue;
    public RelativeLayout eventHeader;
    public LinearLayout eventContainer;
    public EventViewHolder(View itemView) {
        super(itemView);
        eventContainer = (LinearLayout) itemView.findViewById(R.id.event_agenda_view);
        serialNo = (TextView) itemView.findViewById(R.id.serialNo);
        eventName = (TextView) itemView.findViewById(R.id.event_name);
        eventTime = (TextView) itemView.findViewById(R.id.event_time);
        courseName = (TextView) itemView.findViewById(R.id.course_name);
        prof = (TextView) itemView.findViewById(R.id.event_prof);
        credits = (TextView) itemView.findViewById(R.id.event_credits);
        venue = (TextView) itemView.findViewById(R.id.event_venue);
        eventHeader = (RelativeLayout) itemView.findViewById(R.id.event_header);

    }
}
