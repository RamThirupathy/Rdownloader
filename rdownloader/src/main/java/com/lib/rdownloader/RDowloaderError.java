/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.lib.rdownloader;

/**
 * RDowloaderError extends {@link Exception} to throw custom readable exception
 */
public class RDowloaderError extends Exception {
    private String mExceptionMessage;

    /**
     * Constructor of the class to log message
     *
     * @param exceptionMessage
     */
    public RDowloaderError(String exceptionMessage) {
        super(exceptionMessage);
        this.mExceptionMessage = exceptionMessage;
    }

    /**
     * Constructor of the class to log the stack track and message
     *
     * @param exceptionMessage
     * @param reason
     */
    public RDowloaderError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        this.mExceptionMessage = exceptionMessage;
    }

    /**
     * Constructor of the class to log the stack track
     *
     * @param reason
     */
    public RDowloaderError(Throwable reason) {
        super(reason);
    }

    /**
     * Method to get the message
     *
     * @return String
     */
    public String getMessage() {
        return this.mExceptionMessage;
    }
}
