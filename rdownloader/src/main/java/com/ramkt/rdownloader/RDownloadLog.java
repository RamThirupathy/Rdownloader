/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.rdownloader;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RDownloadLog custom log class for debug and check the applications
 * uses the {@link RDownloader}
 */
public class RDownloadLog {
    public static String TAG = RDownloadLog.class.getSimpleName();

    public static boolean INFO = Log.isLoggable(TAG, Log.INFO);

    /**
     * Method to change the tag name(can be set as application name)
     *
     * @param tag
     */
    public static void setTag(String tag) {
        i("Changing log tag to %s", tag);
        TAG = tag;
        INFO = Log.isLoggable(TAG, Log.VERBOSE);
    }

    /**
     * Logger Method to log information events
     *
     * @param format
     * @param args   optional parameter
     */
    public static void i(String format, Object... args) {
        Log.i(TAG, buildMessage(format, args));
    }

    /**
     * Logger Method to log errors
     *
     * @param format
     * @param args   optional parameter
     */
    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
    }

    /**
     * Logger Method to log error and stack trace
     *
     * @param format
     * @param args   optional parameter
     */
    public static void e(Throwable tr, String format, Object... args) {
        Log.e(TAG, buildMessage(format, args), tr);
    }


    /**
     * Method to format the messages to log
     *
     * @param format
     * @param args   optional parameter
     * @return formatted string
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        return String.format(Locale.US, "[%d] %s",
                Thread.currentThread().getId(), msg);
    }


    /**
     * Class to catch all the event, only if log level is enabled for the tag
     */
    protected static class REventLog {
        public static final boolean ENABLED = RDownloadLog.INFO;

        /**
         * Log Event class
         */
        private static class Event {
            public final String name;
            public final long thread;
            public final long time;

            public Event(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        private final List<Event> mEvents = new ArrayList<Event>();
        private boolean mFinished = false;

        /**
         * Adds a marker to this log with the specified name.
         */
        public synchronized void add(String name, long threadId) {
            if (mFinished) {
                throw new IllegalStateException("Event added to finished log");
            }

            mEvents.add(new Event(name, threadId, SystemClock.elapsedRealtime()));
        }

        /**
         * Closes the log, dumping it to logcat if the time difference between
         *
         * @param tag Tag string to print above the marker log.
         */
        public synchronized void finish(String tag) {
            mFinished = true;
            if(!mEvents.isEmpty()) {
                long duration = getTotalDuration();

                long prevTime = mEvents.get(0).time;
                i("(%-4d ms) %s", duration, tag);
                for (Event marker : mEvents) {
                    long thisTime = marker.time;
                    i("(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                    prevTime = thisTime;
                }
            }
        }

        /**
         * Override finalize method to log all the events available before being collected by GC
         */
        @Override
        protected void finalize() throws Throwable {
            if (!mFinished) {
                finish("Request on the loose");
                e("Log finalized without finish() - uncaught exit point for request");
            }
        }

        /**
         * Returns the time difference between the first and last events in this log.
         */
        private long getTotalDuration() {
            if (mEvents.isEmpty()) {
                return 0;
            }

            long first = mEvents.get(0).time;
            long last = mEvents.get(mEvents.size() - 1).time;
            return last - first;
        }
    }

}

