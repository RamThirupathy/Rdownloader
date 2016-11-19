/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.backend;


import com.ramkt.example.response.Pins;
import com.ramkt.example.utils.GenericConstants;
import com.ramkt.example.utils.JsonParser;
import com.ramkt.example.utils.Logger;
import com.ramkt.rdownloader.RDowloaderError;

import java.util.ArrayList;
import java.util.List;

/**
 * PinService is child class of {@link Module}
 * and handles Pin data functionality
 */
public class PinService extends Module {
    private final static String TAG = PinService.class.getSimpleName();
    public List<Pins> mPins;

    /**
     * Constructor of class and initialize all final values
     *
     * @param listener call back listener to receive result
     */
    public PinService(IAPIResponseListener listener) {
        Logger.i(TAG, "Pin Service constructor");
        this.mCallBackListener = listener;
        mPins = new ArrayList<Pins>();
    }

    /**
     * Method to get pins
     * and the result will be send asynchronously
     */
    public void getPins() {
        getAPIResponse(GenericConstants.getInstance().getAPIUrl());
    }

    /**
     * Method to cancel the request already triggered(in the queue) from this class
     */
    public void cancelRequest() {
        cancelRequest(GenericConstants.getInstance().getAPIUrl());
    }


    /**
     * Method to add new pins to the list
     *
     * @param pins
     */
    public void addNewPins(List<Pins> pins) {
        mPins.addAll(0, pins);
        if (mCallBackListener != null)
            mCallBackListener.onPinReceived(mPins);
    }

    /**
     * Method to send response to UI
     *
     * @param s
     */
    @Override
    public void onResponse(String s) {
        List<Pins> pins = JsonParser.getJsonParserInstance().parsePins(s);
        Logger.i(TAG, "Pin service response" + mPins.size());
        addNewPins(pins);
    }

    /**
     * Method to send details about the download failure
     */
    @Override
    public void onError(RDowloaderError error) {
        mCallBackListener.onFailure(error.getMessage());
    }
}
