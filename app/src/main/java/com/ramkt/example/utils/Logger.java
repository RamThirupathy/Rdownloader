/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

import android.util.Log;

/**
 * Logger abstract class logs all the message comes through out the application
 * can be implemented by other class to store the logs in file/server or email it
 */
public abstract class Logger {

    /**
     * Method to log the debug information
     *
     * @param tag name of the Class from which this method is called.
     * @param msg message to be logged.
     */
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    /**
     * Method to log the general information
     *
     * @param tag name of the Class from which this method is called.
     * @param msg message to be logged.
     */
    public static void i(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * Method to log the exception information
     *
     * @param tag name of the Class from which this method is called.
     * @param msg message to be logged.
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * Method to log the exception information
     *
     * @param tag name of the Class from which this method is called.
     * @param msg message to be logged.
     * @param tr  exception to be logged.
     */
    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    abstract void fileLogging(String message);


    abstract void sendEmail();
}
