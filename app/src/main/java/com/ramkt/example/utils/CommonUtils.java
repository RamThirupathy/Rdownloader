/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.ramkt.rdownloader.RDowloaderError;
import com.ramkt.rdownloader.RDownloader;
import com.ramkt.rdownloader.RequestTool.ImageRequest;

/**
 * CommonUtils class handles util function of the application
 */
public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();
    RDownloader<Bitmap> mRDownloader;
    private static CommonUtils mCommonsUtil;

    /**
     * Singleton constructor of the class
     */
    private CommonUtils() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 10;
        mRDownloader = new RDownloader<Bitmap>(cacheSize);
    }

    /**
     * Method to get singleton isntance
     */
    public static CommonUtils getInstance() {
        if (mCommonsUtil == null) {
            mCommonsUtil = new CommonUtils();
        }
        return mCommonsUtil;
    }

    /**
     * Method to check the Network availability
     *
     * @param context Context of the App
     * @return boolean true if network available
     */
    public boolean isNetworkAvailable(Context context) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    /**
     * Method to change image layout parameter
     */
    public void changeImageLayout(ImageView view, int height, String color) {
        view.getLayoutParams().height = height;
        view.setBackgroundColor(Color.parseColor(color));
        view.requestLayout();
    }

    /**
     * Method to download image using any third party plugins
     * here{@link RDownloader}
     */
    public void downloadImage(final Context context, final ImageView imageView, final String url, int desiredWidth, int desiredHeight
            , int actualWidth, int actualHeight) {
        Logger.i(TAG, url);
        ImageRequest request = new ImageRequest(url, new ImageRequest.ImageResponseListener<Bitmap>(imageView) {

            @Override
            public void onError(RDowloaderError error) {

            }
        }, desiredWidth, desiredHeight, actualWidth, actualHeight);
        mRDownloader.load(request);
    }
}
