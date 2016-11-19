/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.ramkt.rdownloader;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.zip.GZIPInputStream;

/**
 * Request class to be used by the application to share download details
 * and load it to {@link RDownloader}
 */
public abstract class Request<K> {
    private final String mTag = getClass().getSimpleName();
    private long mRequestStartTime;
    private int mNoOfRetry;
    private final RDownloadLog.REventLog mEventLog = RDownloadLog.REventLog.ENABLED ? new RDownloadLog.REventLog() : null;
    private WorkerTask mWorkerTask;
    protected RDownloader<K> mRDownloader;
    protected LinkedList<ResponseListener<K>> mResponseListenerList;
    protected String mRequestUrl;
    protected String mRequestKey;

    /**
     * Method to implement by all sub class to get output in needed
     * data type
     *
     * @param stream
     */
    public abstract K parseResponse(InputStream stream) throws Exception;

    /**
     * Constructor of the class
     *
     * @param url      server url to hit
     * @param key      unique key
     * @param listener
     */
    public Request(String url, String key, ResponseListener<K> listener) {
        this.mRequestUrl = url;
        this.mRequestKey = key;
        mResponseListenerList = new LinkedList<ResponseListener<K>>();
        mResponseListenerList.add(listener);
    }

    /**
     * Method to implement by child class to pass custom header
     * data type
     *
     * @return Map
     */
    public Map<String, String> getHeaders() throws Exception {
        return Collections.emptyMap();
    }

    /**
     * Method to get the no of retries
     *
     * @return int
     */
    public int getNoOfRetries() {
        return this.mNoOfRetry;
    }

    /**
     * Method to set no of retries
     *
     * @param retry
     */
    public void setNoOfRetries(int retry) {
        this.mNoOfRetry = retry;
    }

    /**
     * Method to get unique request key
     *
     * @return String
     */
    public final String getRequestKey() {
        return this.mRequestKey;
    }

    /**
     * Method to get all the response listeners
     *
     * @return LinkedList
     */
    public final LinkedList<ResponseListener<K>> getResponseListener() {
        return mResponseListenerList;
    }

    /**
     * Method to add new request to the existing list
     *
     * @param request
     */
    protected int addRequest(Request<K> request) {
        this.mResponseListenerList.add(request.getCallBack());
        if (request.getNoOfRetries() > mNoOfRetry)
            setNoOfRetries(request.getNoOfRetries());
        return this.mResponseListenerList.size() - 1;
    }

    /**
     * Method to all the listener in the new request(for the same resource) to the existing list
     *
     * @return ResponseListener
     */
    protected final ResponseListener<K> getCallBack() {
        return mResponseListenerList.get(0);
    }

    /**
     * Method to start the download process
     *
     * @param executor   to do parallel download
     * @param downloader
     */
    protected void execute(Executor executor, RDownloader<K> downloader) {
        this.mRDownloader = downloader;
        mWorkerTask = new WorkerTask();
        mWorkerTask.executeOnExecutor(executor, mRequestKey);
    }

    /**
     * Method to remove a listener from the listener list, if it is the last one then
     * the whole process stops
     *
     * @param requestID
     * @return boolean true if process stops
     */
    protected final boolean cancel(int requestID) {
        if (!mResponseListenerList.isEmpty() && mResponseListenerList.size() >= requestID) {
            mResponseListenerList.remove(requestID);
        }
        if (mResponseListenerList.isEmpty()) {
            if (mWorkerTask != null) {
                mWorkerTask.cancel(true);
            }
            return true;
        }
        return false;
    }

    /**
     * Method to remove all listener and stop the process
     */
    protected final void cancelAll() {
        mResponseListenerList.clear();
        if (mWorkerTask != null)
            mWorkerTask.cancel(true);
    }

    /**
     * Method to send error detail to all listeners when some download
     * fails
     *
     * @param dowloaderError
     */
    protected void sendError(RDowloaderError dowloaderError) {
        if (mRDownloader != null) {
            if (!mResponseListenerList.isEmpty()) {
                for (ResponseListener<K> item : mResponseListenerList) {
                    item.onError(dowloaderError);
                }
            }
        }
    }


    /**
     * Method to send response to all listeners
     *
     * @param response
     * @param url
     */
    protected void sendResponse(K response, String url) {
        Log.e("test", "send res");
        if (!mResponseListenerList.isEmpty()) {
            for (ResponseListener<K> item : mResponseListenerList) {
                item.onResponse(response);
            }
        }
    }

    /**
     * Worker task class to download data from server
     */
    private class WorkerTask extends AsyncTask<String, Void, K> {
        String key;

        @Override
        protected K doInBackground(String... arg) {
            try {
                addEvent("Request Start");
                key = arg[0];
                K response = getResponse(mRequestUrl);
                return response;
            } catch (RDowloaderError downloaderError) {
                addEvent("Request Error" + downloaderError.getMessage());
                if (!isCancelled())
                    sendError(downloaderError);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            addEvent("Request Cancelled");
            complete();
        }

        @Override
        protected void onPostExecute(K response) {
            super.onPostExecute(response);
            if (!isCancelled()) {
                sendResponse(response, key);
                complete();
            }
        }
    }

    /**
     * Method uses by worker task
     *
     * @param url
     * @return response
     */
    private K getResponse(String url) throws RDowloaderError {
        K response = null;
        if (mRDownloader != null) {
            response = mRDownloader.checkCache(mRequestKey);
        }
        if (response == null) {
            try {
                addEvent(String.format("Download from server %s", url));
                InputStream stream = download(url, 1);
                if (stream == null) {
                    throw new RDowloaderError("Unable to download data from server.");
                } else {
                    addEvent("Request download complete");
                    response = parseResponse(stream);
                    stream.close();
                    cacheResponse(mRequestKey, response);
                }
            } catch (IOException e) {
                throw new RDowloaderError("Unable to read data from server.", e);
            } catch (Exception e) {
                throw new RDowloaderError("Exception occurred while download: " + e.getMessage(), e);
            }
        } else {
            addEvent("Request Processed from cache");
        }
        return response;
    }

    /**
     * Method to download data from server
     *
     * @param url
     * @param downloadAttempt
     */
    private InputStream download(String url, int downloadAttempt) throws Exception {
        try {
            HttpURLConnection conn = null;
            URL connectUrl = new URL(url);
            conn = (HttpURLConnection) connectUrl.openConnection();
            for (Map.Entry<String, String> header : getHeaders().entrySet()) {
                RDownloadLog.i("%s Add conn property %s %s", mTag, header.getKey(), header.getValue());
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
            InputStream stream = conn.getInputStream();
            if (conn.getContentEncoding() != null && conn.getContentEncoding().contains("gzip")) {
                RDownloadLog.i("%s Response GZIP", mTag);
                stream = new GZIPInputStream(stream);
            }
            if (stream == null) {
                if (downloadAttempt < mNoOfRetry) {
                    return download(url, downloadAttempt + 1);
                }
            }
            return stream;
        } catch (Exception e) {
            if (downloadAttempt < mNoOfRetry) {
                return download(url, downloadAttempt + 1);
            } else {
                throw e;
            }
        }
    }

    /**
     * Method to cache response after download
     *
     * @param key
     * @param response
     */
    private void cacheResponse(String key, K response) {
        if (mRDownloader != null && response != null) {
            mRDownloader.cacheResponse(key, response);
            addEvent("Request cached");
        }
    }

    /**
     * Method to pass events to logger class
     *
     * @param tag
     */
    private void addEvent(String tag) {
        if (RDownloadLog.REventLog.ENABLED) {
            mEventLog.add(tag, Thread.currentThread().getId());
        } else if (mRequestStartTime == 0) {
            mRequestStartTime = SystemClock.elapsedRealtime();
            RDownloadLog.i("Request start %i ms: %s", mRequestStartTime, this.toString());
        }
    }

    /**
     * Method executes as a final step of request processing
     */
    private void complete() {
        if (RDownloadLog.REventLog.ENABLED) {
            addEvent("Request complete");
            mEventLog.finish(mTag);
        } else {
            long requestTime = SystemClock.elapsedRealtime() - mRequestStartTime;
            RDownloadLog.i("Request completes in [%i] ms: %s", requestTime, this.toString());
        }
        if (mResponseListenerList != null) {
            mResponseListenerList.clear();
            mResponseListenerList = null;
        }
        if (mRDownloader != null) {
            mRDownloader.removeRequest(mRequestKey);
            mRDownloader = null;
        } else {
            RDownloadLog.i("weak ref not available");
        }
        mRequestKey = null;
        mWorkerTask = null;
        mRequestUrl = null;
    }


}
