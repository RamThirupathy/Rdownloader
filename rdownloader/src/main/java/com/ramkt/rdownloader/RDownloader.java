/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.ramkt.rdownloader;

import android.graphics.Bitmap;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.util.LruCache;


import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * RDownloader main handler class to process all request,cache
 * and cancel requests
 * Uses {@link LruCache} for caching and evict least used member
 * User {@link SoftReference} to reuse member if not removed by GC
 */
public class RDownloader<K> {
    private static final String TAG = RDownloader.class.getSimpleName();
    private LruCache<String, K> mLruCache;
    private Set<SoftReference<K>> mReuseSet;
    private Map<String, Request<K>> mRequestMap;
    private int mCacheSize;
    private static final Executor mThreadPoolExecutor = Executors.newCachedThreadPool();
    public static final int KB = 1024;

    /**
     * Constructor to inialize all the utils of the class
     * if cache size is zero then cache is ignored
     *
     * @param cacheSizeInKB
     */
    public RDownloader(final int cacheSizeInKB) {
        if (cacheSizeInKB > 0) {
            mCacheSize = cacheSizeInKB;
            mLruCache = new LruCache<String, K>(cacheSizeInKB) {
                @Override
                protected int sizeOf(String key, K value) {
                    if (value instanceof Bitmap) {
                        return ((Bitmap) value).getByteCount() / KB;
                    } else if (value instanceof String) {
                        return ((String) value).getBytes().length / KB;
                    }
                    return super.sizeOf(key, value);
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, K oldValue, K newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    RDownloadLog.i("%s: Cache evict-[%s]", TAG, key);
                    if (oldValue instanceof Bitmap)//any mutable
                        mReuseSet.add(new SoftReference<K>(oldValue));
                }
            };
            mReuseSet = Collections.synchronizedSet(new HashSet<SoftReference<K>>());
        }
        mRequestMap = Collections.synchronizedMap(new HashMap<String, Request<K>>());
    }

    /**
     * Method to Cache the response
     *
     * @param key
     * @param response
     */
    public void cacheResponse(String key, K response) {
        if (mLruCache != null) {
            synchronized (mLruCache) {
                RDownloadLog.i("%s: Cache : [%s]", TAG, key);
                mLruCache.put(key, response);
            }
        }
    }

    /**
     * Method to value is available in cached
     *
     * @param key
     * @return K
     */
    public K checkCache(String key) {
        if (mLruCache != null) {
            synchronized (mLruCache) {
                return mLruCache.get(key);
            }
        }
        return null;
    }

    /**
     * Method to empty the cache
     */
    public void clearCache() {
        if (mLruCache != null) {
            synchronized (mLruCache) {
                RDownloadLog.i("%s: Clear cache", TAG);
                mLruCache.evictAll();
            }
        }
    }

    /**
     * Method to get value from reuse set
     *
     * @return Soft reference of the value
     */
    public Set<SoftReference<K>> getReuseSet() {
        return mReuseSet;
    }

    /**
     * Method to add the request to process
     *
     * @param request (can be any custom request)
     */
    @UiThread
    public int load(Request<K> request) {
        synchronized (mRequestMap) {
            RDownloadLog.i("%s: Load Request - [%s]", TAG, request.getRequestKey());
            if (Looper.myLooper() == Looper.getMainLooper()) {
                int requestID = 0;
                if (!mRequestMap.containsKey(request.getRequestKey())) {
                    mRequestMap.put(request.getRequestKey(), request);
                    request.execute(mThreadPoolExecutor, this);
                } else {
                    RDownloadLog.i("%s add to queue", TAG);
                    requestID = mRequestMap.get(request.getRequestKey()).addRequest(request);
                }
                return requestID;
            } else {
                throw new IllegalStateException("RDownloader load should be called only on UI thread.");
            }
        }
    }

    /**
     * Method to remove the request
     *
     * @param key (request key)
     */
    public void removeRequest(String key) {
        synchronized (mRequestMap) {
            if (mRequestMap.get(key) != null) {
                mRequestMap.remove(key);
            }
            RDownloadLog.i("%s: Remove request with key %s", TAG, key);
        }
    }

    /**
     * Method to cancel all the request with the key
     *
     * @param key (request key)
     */
    public void cancel(String key) {
        synchronized (mRequestMap) {
            if (mRequestMap.get(key) != null) {
                mRequestMap.get(key).cancelAll();
                mRequestMap.remove(key);
            }
            RDownloadLog.i("%s: cancel all request with key: %s", TAG, key);
        }
    }

    /**
     * Method to cancel only a request with listenerID in the list of request
     *
     * @param key       (request key is use to get list of request with same key)
     * @param listenerID (listener with this id will be removed)
     */
    public void cancel(String key, int listenerID) {
        synchronized (mRequestMap) {
            if (mRequestMap.get(key) != null) {
                if (mRequestMap.get(key).cancel(listenerID))
                    mRequestMap.remove(key);
            }
            RDownloadLog.i("%s: Cancel-[%i] from the request list [%s]", TAG, listenerID, key);
        }
    }

    /**
     * Method to cancel all the request
     */
    public void cancelAll() {
        synchronized (mRequestMap) {
            mRequestMap.clear();
            RDownloadLog.i("%s: Cancel all request", TAG);
        }
    }

    /**
     * Method to call by the application at the end to remove RDownloader
     */
    public void finish() {
        cancelAll();
        clearCache();
        clearReuseSet();
        RDownloadLog.i("%s: Finish on [%i]", TAG, SystemClock.elapsedRealtime());
    }


    /**
     * Method to clear reuse set
     */
    private void clearReuseSet() {
        if (mReuseSet != null) {
            synchronized (mReuseSet) {
                mReuseSet.clear();
                RDownloadLog.i("%s: Clear reuse set", TAG);
            }
        }
    }


}
