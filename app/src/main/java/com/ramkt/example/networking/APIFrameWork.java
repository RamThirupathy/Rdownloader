/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.networking;

import com.ramkt.example.utils.Logger;
import com.ramkt.rdownloader.ResponseListener;
import com.ramkt.rdownloader.RDownloader;
import com.ramkt.rdownloader.RequestTool.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * APIFramework class interact with any third party plugin(here {@link RDownloader})
 * to get data from network
 */
public abstract class APIFrameWork extends ResponseListener<String> {
    private static final String TAG = APIFrameWork.class.getSimpleName();
    private static final int TIME_OUT_IN_MILLISECONDS = 5000;
    private static final int NO_OF_RETRIES = 2;
    private static final int CACHE_SIZE = 0;
    private RDownloader<String> mRDownloader;
    private Map<String, String> mConnectionProperty;

    /**
     * Constructor of the class, initialize custom request header
     */
    public APIFrameWork() {
        mRDownloader = new RDownloader<String>(CACHE_SIZE);
        mConnectionProperty = new HashMap<String, String>();
        mConnectionProperty.put("Accept-Charset", "UTF-8");
        mConnectionProperty.put("content-type", "application/json");
    }

    /**
     * This method will create new API request and add it to queue
     *
     * @param url The API url to hit.
     */
    protected void getResponse(String url) {
        Logger.i(TAG, String.format("url: %s", url));
        StringRequest request2 = new StringRequest(url, this);
        request2.setHeaders(mConnectionProperty);
        mRDownloader.load(request2);
    }

    /**
     * Method to cancel the Network calls already triggered(in the queue)
     */
    protected void cancelRequest(String tag) {
        mRDownloader.cancel(tag);
    }
}
