package com.example.kezdesu;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = NotificationRecyclerViewAdapter.class.getSimpleName();

    private final List<ParseObject> mValues;
    private final NotificationsFragment.OnFragmentInteractionListener mListener;

    public NotificationRecyclerViewAdapter(List<ParseObject> items, NotificationsFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false);
        Log.i(TAG, "in onCreateViewHolder!!!");
        return new ViewHolder(view);
    }

    public String notificationMessage(String fromUserId, String type) {
        String fromUsername = "";
        try {
            fromUsername = ParseUser.getQuery().get(fromUserId).getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fromUsername + " wants to invite you for a " + type;
    }

    @Override
    public void onBindViewHolder(final NotificationRecyclerViewAdapter.ViewHolder holder, final int position) {

        ParseObject object = mValues.get(position);

        holder.mNotification = object;
        holder.mFromUserView.setText(notificationMessage(object.getString("from"), object.getString("type")));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Uri uri = Uri.parse("example.com");
                    mListener.onFragmentInteraction(uri);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;        // Current View
        public final TextView mFromUserView; // username
        public ParseObject mNotification; // User itself

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFromUserView = (TextView) view.findViewById(R.id.notification_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFromUserView.getText() + "'";
        }
    }
}
