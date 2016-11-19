/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.manager;

import android.content.Context;

import com.ramkt.example.utils.ExceptionHandler;
import com.ramkt.example.utils.GenericConstants;
import com.ramkt.example.utils.JsonParser;
import com.ramkt.example.utils.Logger;


/**
 * Application is child class of {@link android.app.Application}
 * and handles all application level events
 */
public class Application extends android.app.Application {
    private static final String TAG = Application.class.getSimpleName();
    private static Context mAppContext;

    @Override
    public void onCreate() {
        Logger.i(TAG, "Application on create");
        super.onCreate();
        mAppContext = this;
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        GenericConstants.getInstance();
        JsonParser.getJsonParserInstance();
    }


    /**
     * Method to get the current context
     *
     * @return current context of the application
     */
    public static Context getAppContext() {
        return mAppContext;
    }


    /**
     * Method to set the current context
     *
     * @param context {@link android.app.Activity} which is visible
     */
    public static void setAppContext(Context context) {
        mAppContext = context;
    }


}
