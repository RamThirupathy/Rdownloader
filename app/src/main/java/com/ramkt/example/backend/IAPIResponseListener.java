/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.backend;

import com.ramkt.example.response.Pins;

import java.util.List;

/**
 * IAPIResponseListener interface that the app controller should implement
 *
 */
public interface IAPIResponseListener {

    /**
     * Listener Method to receive {@link Pins} result after successful API response
     */
     void onPinReceived(List<Pins> pins);

    /**
     * Listener Method to receive failure message after unsuccessful API response
     */
     void onFailure(String message);
}
