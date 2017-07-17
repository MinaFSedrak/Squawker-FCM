package minasedrak.squawker;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import minasedrak.squawker.provider.SquawkContract;

/**
 * Created by MinaSedrak on 7/16/2017.
 */

public class SquawkAdapter extends RecyclerView.Adapter<SquawkAdapter.SquawkViewHolder> {

    private Cursor mData;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");


    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @Override
    public SquawkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_squawk_list, parent, false);

        SquawkViewHolder vh =  new SquawkViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(SquawkViewHolder holder, int position) {

        mData.moveToPosition(position);

        String message = mData.getString(MainActivity.COL_NUM_MESSAGE);
        String author = mData.getString(MainActivity.COL_NUM_AUTHOR);
        String autorKey = mData.getString(MainActivity.COL_NUM_AUTHOR_KEY);

        // Get the date for displaying
        long dateMillis = mData.getLong(MainActivity.COL_NUM_DATE);
        String date = "";
        long now = System.currentTimeMillis();


        // Displaying the date depending on whether it was written in the last minute / hour etc...
        if(now - dateMillis < (DAY_MILLIS)){

            if(now - dateMillis < (HOUR_MILLIS)){
                long minutes = Math.round( (now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            }else {
                long hours = Math.round( (now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(hours) + "h";
            }

        }else {
            Date dateDate = new Date(dateMillis);
            date = sDateFormat.format(dateDate);
        }

        // add dot to date
        date = "\u2022" + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);

        switch (autorKey) {
            case SquawkContract.ASSER_KEY:
                holder.authorImageView.setImageResource(R.drawable.asser);
                break;
            case SquawkContract.CEZANNE_KEY:
                holder.authorImageView.setImageResource(R.drawable.cezanne);
                break;
            case SquawkContract.JLIN_KEY:
                holder.authorImageView.setImageResource(R.drawable.jlin);
                break;
            case SquawkContract.LYLA_KEY:
                holder.authorImageView.setImageResource(R.drawable.lyla);
                break;
            case SquawkContract.NIKITA_KEY:
                holder.authorImageView.setImageResource(R.drawable.nikita);
                break;
            default:
                holder.authorImageView.setImageResource(R.drawable.test);
        }

    }

    @Override
    public int getItemCount() {

        if( mData == null){ return 0;}

        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mData = newCursor;
        notifyDataSetChanged();
    }

    public class SquawkViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView;
        final TextView messageTextView;
        final TextView dateTextView;
        final ImageView authorImageView;

        public SquawkViewHolder(View layoutView) {
            super(layoutView);
            authorTextView = (TextView) layoutView.findViewById(R.id.author_textView);
            messageTextView = (TextView) layoutView.findViewById(R.id.message_textView);
            dateTextView = (TextView) layoutView.findViewById(R.id.date_textView);
            authorImageView = (ImageView) layoutView.findViewById(
                    R.id.author_imageView);
        }
    }
}
