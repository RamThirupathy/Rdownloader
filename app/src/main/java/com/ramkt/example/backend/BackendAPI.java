/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.backend;


import com.ramkt.example.utils.Logger;

/**
 * BackendAPI class to abstract all backed process
 */
public class BackendAPI {
    private static final String TAG = BackendAPI.class.getSimpleName();
    private PinService mPinService;

    /**
     * Constructor of class
     * create object of child class
     *
     * @param listener call back listener to receive network feedback
     */
    public BackendAPI(IAPIResponseListener listener) {
        Logger.i(TAG, "Backend API creation");
        mPinService = new PinService(listener);
    }

    /**
     * Method to trigger the API call to get list of pins
     * and the response will be asynchronously passed via listeners
     */
    public void getPins() {
        mPinService.getPins();
    }


    /**
     * Method to cancel the pin request
     */
    public void cancelRequest() {
        mPinService.cancelRequest();
    }

}
