/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example.backend;

import com.ramkt.example.networking.APIFrameWork;

/**
 * Parent class for all module class(here its just {@link PinService})
 */
public abstract class Module extends APIFrameWork {
    private final static String TAG = Module.class.getSimpleName();
    protected IAPIResponseListener mCallBackListener;

    /**
     * Method to trigger the API call to get the response
     *
     * @param url
     */
    protected void getAPIResponse(String url) {
        getResponse(url);
    }

}
