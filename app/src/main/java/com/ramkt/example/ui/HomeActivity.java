/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;


import com.ramkt.example.R;
import com.ramkt.example.manager.AppManager;
import com.ramkt.example.response.Pins;

import java.util.List;

/**
 * Home Activity of the application,
 * acts as model and is controlled by {@link AppManager}
 */
public class HomeActivity extends AppCompatActivity {
    private FrameLayout mflContainer;
    private PinsFragment mPinFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mflContainer = (FrameLayout) findViewById(R.id.fl_container);
        mPinFragment = new PinsFragment();
        addFragment(mPinFragment, false, null);
        getPins();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.action_bar);
        if (actionBarToolbar != null)
            actionBarToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * Method to get pins from controller
     */
    public void getPins() {
        AppManager.getInstance(this).getPins();
    }

    /**
     * Call back method from Appmanager to load pins
     */
    public void loadPins(final List<Pins> pins) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPinFragment.setPinList(pins);
            }
        });

    }

    /**
     * Call back method from Appmanager when load pins fails
     */
    public void pinsUnavailable() {
        mPinFragment.error();
    }

    /**
     * Method starts the {@link DetailsActivity}
     */
    public void showDetailsFragment(Pins pin) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title", pin.getCategory());
        intent.putExtra("created", pin.getCreated_at());
        intent.putExtra("likes", pin.getLikes());
        intent.putExtra("pins", pin.getPinCount());
        intent.putExtra("color", pin.getColor());
        intent.putExtra("image", pin.getImageUrl());
        intent.putExtra("width", pin.getWidth());
        intent.putExtra("height", pin.getHeight());
        startActivity(intent);
    }

    /**
     * Generic method to add fragments in the class
     *
     * @param fragment       fragment to be added
     * @param addToBackStack boolean to decide whether fragment should add to the back stack
     * @param tag            key value
     */
    private void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }
}
