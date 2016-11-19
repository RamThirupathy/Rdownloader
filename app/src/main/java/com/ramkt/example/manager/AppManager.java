/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.manager;

import android.content.Context;

import com.ramkt.example.R;
import com.ramkt.example.backend.BackendAPI;
import com.ramkt.example.backend.IAPIResponseListener;
import com.ramkt.example.response.Pins;
import com.ramkt.example.ui.HomeActivity;
import com.ramkt.example.utils.Alerts;
import com.ramkt.example.utils.CommonUtils;
import com.ramkt.example.utils.Logger;

import java.util.List;

/**
 * AppManager class is manager class controls UI and abstract between data
 * and ui layer
 */
public class AppManager implements IAPIResponseListener {
    private static final String TAG = AppManager.class.getSimpleName();
    private static AppManager mAppManager;
    private BackendAPI mBackendAPI;
    private Context mAppContext;
    private Alerts mAlertStack;


    /**
     * Private Constructor of class to achieve singleton
     *
     * @param context Current Activity.
     */
    private AppManager(Context context) {
        Logger.i(TAG, "Create");
        this.mAppContext = context;
        mBackendAPI = new BackendAPI(this);
        mAlertStack = new Alerts();
    }

    public void setContext(Context context) {
        this.mAppContext = context;
    }

    /**
     * Method to initialize the object if null and return it
     *
     * @param context Current Activity.
     */
    public static AppManager getInstance(Context context) {
        if (mAppManager == null) {
            mAppManager = new AppManager(context);
        }
        return mAppManager;
    }

    /**
     * Method to get pins if network is available
     * and if not alert message to be prompted to user and updates the UI
     * accordingly
     */
    public void getPins() {
        if (CommonUtils.getInstance().isNetworkAvailable(mAppContext)) {
            mBackendAPI.getPins();
        } else {
            Logger.i(TAG, "Network not available : Get popular movies");
            ((HomeActivity) mAppContext).pinsUnavailable();
            mAlertStack.displayAlert(mAppContext, mAppContext.getResources().getString(R.string.internet_connection_not_available));
        }
    }


    /**
     * Method handles the deinitialize the Backend layer
     * and if not alert message to be prompted to user
     */
    public void destroy() {
        Logger.i(TAG, "Destroy");
        this.mAppManager = null;
        this.mAppContext = null;
        this.mBackendAPI.cancelRequest();
        this.mBackendAPI = null;
    }

    /**
     * Method sends data to activity
     */
    @Override
    public void onPinReceived(List<Pins> pins) {
        ((HomeActivity) mAppContext).loadPins(pins);
    }

    /**
     * Method notify activity about network failure
     */
    @Override
    public void onFailure(String message) {
        Logger.i(TAG, "Error " + message);
        ((HomeActivity) mAppContext).pinsUnavailable();
    }
}
