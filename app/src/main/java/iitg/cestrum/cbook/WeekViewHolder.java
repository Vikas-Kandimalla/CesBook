package iitg.cestrum.cbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vikas on 08-01-2018.
 */

public class WeekViewHolder extends RecyclerView.ViewHolder {

    public TextView weekName;

    public WeekViewHolder(View itemView) {
        super(itemView);
        weekName = itemView.findViewById(R.id.week_name);
    }
}
