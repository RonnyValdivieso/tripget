package com.tripget.tripget.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripget.tripget.Model.Notification;
import com.tripget.tripget.R;

import java.util.List;

/**
 * Created by ivonne on 09/08/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<Notification> notificationList;

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        public TextView userActionNotification;
        public ImageView userImgNotification, tripImgNotification;
        public MyViewHolder(View itemView) {
            super(itemView);

            userActionNotification = (TextView) itemView.findViewById(R.id.userActionNoti);
            userImgNotification = (ImageView)  itemView.findViewById(R.id.userImgNoti);
            tripImgNotification = (ImageView) itemView.findViewById(R.id.tripImgNoti);
        }
    }

    public NotificationAdapter(Context mContext, List<Notification> notificationList) {
        this.mContext = mContext;
        this.notificationList = notificationList;
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_cardview, parent,false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {

        Notification notification = notificationList.get(position);
        holder.userActionNotification.setText(notification.getUsername() + " " + mContext.getString(R.string.user_action_noti));
        Glide.with(mContext).load(notification.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.userImgNotification);
        Glide.with(mContext).load(notification.getTrip_image()).apply(RequestOptions.circleCropTransform()).into(holder.tripImgNotification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


}
