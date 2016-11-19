/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

/**
 * ExceptionHandler is child class of {@link Thread.UncaughtExceptionHandler}
 * to catch the uncaught exception and handles it
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = ExceptionHandler.class.getSimpleName();
    private Thread.UncaughtExceptionHandler mExceptionHandler;

    /**
     * Constructor to assign exception handler with default handler
     * to catch the uncaught exception and handles it
     */
    public ExceptionHandler() {
        this.mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringBuilder exceptionMessage = new StringBuilder();
        exceptionMessage.append(e.toString());
        exceptionMessage.append(',');
        for (StackTraceElement element : e.getStackTrace()) {
            exceptionMessage.append(element.toString());
            exceptionMessage.append(",");
        }
        Logger.e(TAG, "Uncaught exception -" + exceptionMessage.toString(), e);
        mExceptionHandler.uncaughtException(t, e);
    }
}

