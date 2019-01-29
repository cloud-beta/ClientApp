package com.example.user.pushapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.example.user.pushapp.microservices.information.model.Restaurant;

public class MainActivity extends AppCompatActivity implements RestaurantHolder.Callbacks, BookFragment.Callbacks {

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PhoneNum을 받아오기 위한 Permission 요청
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, 1);

        phoneNumber = getPhoneNumber();

        // Create a new Fragment to be placed in the activity layout
        RestaurantListFragment firstFragment = RestaurantListFragment.newInstance();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();


    }


    @Override
    public void onRequestBook(Restaurant restaurant) {

        BookFragment bookFragment = BookFragment.newInstance(phoneNumber, restaurant.getPlaceID());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, bookFragment)
                .addToBackStack("BOOK")
                .commit();
    }

    @Override
    public void onSubmit() {
        getSupportFragmentManager().popBackStack();
    }

    private String getPhoneNumber() {
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = "";
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                return "";

            phoneNumber = telephony.getLine1Number();
            phoneNumber = phoneNumber.replace("+82","0");
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }
}
