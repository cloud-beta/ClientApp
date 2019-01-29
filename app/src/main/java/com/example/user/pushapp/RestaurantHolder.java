package com.example.user.pushapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.user.pushapp.microservices.information.model.Restaurant;
import com.example.user.pushapp.util.DebugUtil;

public class RestaurantHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "RestaurantHolder";

    private Callbacks mCallbacks;

    private Restaurant mRestaurant;
    private TextView mTitleTextView;
    private CardView mCardView;

    interface Callbacks {
        void onRequestBook(Restaurant restaurant);
    }

    public Restaurant getBindRestaurant() {
        return mRestaurant;
    }

    public boolean hasBindRestaurant() {
        return mRestaurant != null;
    }

    public RestaurantHolder(View itemView, Callbacks callbacks) {
        super(itemView);
        mCallbacks = callbacks;
        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mTitleTextView = (TextView)
                itemView.findViewById(R.id.textview_base_content_title);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onRequestBook(mRestaurant);
            }
        });
    }

    public void bindContent(Restaurant restaurant) {
        mRestaurant = restaurant;
        mTitleTextView.setText(mRestaurant.getName());

        DebugUtil.logD(TAG, String.format("bind content %s", mRestaurant.getName()));
    }

}