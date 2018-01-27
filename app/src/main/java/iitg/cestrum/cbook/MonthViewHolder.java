package iitg.cestrum.cbook;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vikas on 06-01-2018.
 */

public class MonthViewHolder extends RecyclerView.ViewHolder {

    public TextView monthImage;
    public TextView monthName;

    public MonthViewHolder(View itemView) {
        super(itemView);
        monthImage = itemView.findViewById(R.id.month_image);
        monthName = itemView.findViewById(R.id.month_name);
    }
}
