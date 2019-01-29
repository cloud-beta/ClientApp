package com.example.user.pushapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.pushapp.microservices.information.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder>{

    private MainActivity mActivity;

    private List<Restaurant> mRestaurants;

    public RestaurantAdapter(MainActivity activity) {
        super();
        mActivity = activity;
        mRestaurants = new ArrayList<>();
    }

    @Override
    public RestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater
                .inflate(R.layout.list_item_base_content, parent, false);
        return new RestaurantHolder(view, mActivity);
    }

    @Override
    public void onBindViewHolder(RestaurantHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.bindContent(restaurant);
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public void changeContents(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
        notifyDataSetChanged();
    }

}