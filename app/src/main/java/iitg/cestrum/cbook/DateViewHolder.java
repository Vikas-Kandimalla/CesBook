package iitg.cestrum.cbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by vikas on 06-01-2018.
 */

public class DateViewHolder extends RecyclerView.ViewHolder {

    public TextView dateName;

    public DateViewHolder(View itemView) {
        super(itemView);
        dateName = itemView.findViewById(R.id.date_name);
    }
}
