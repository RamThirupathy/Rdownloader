/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.lib.rdownloader;

/**
 * ResponseListener to pass by the application to get the response
 */
public abstract class ResponseListener<K> {

    /**
     * Method to get response
     */
    public abstract void onResponse(K k);

    /**
     * Method to get error if any
     */
    public abstract void onError(RDowloaderError error);

}
