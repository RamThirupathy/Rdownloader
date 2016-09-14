/**
 * Created by Ram_Thirupathy on 9/2/2016.
 */
package com.lib.rdownloader.RequestTool;

import com.lib.rdownloader.RDownloadLog;
import com.lib.rdownloader.ResponseListener;
import com.lib.rdownloader.Request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

/**
 * StringRequest extends {@link Request} get response in string
 */
public class StringRequest extends Request<String> {
    private static final String TAG = StringRequest.class.getSimpleName();
    private String mDefaultCharset = "UTF-8";
    private Map<String, String> mHeader = Collections.emptyMap();


    /**
     * Constructor of the class and if used then default encoding will be
     * considered
     *
     * @param url      url of the image to download
     * @param listener to wait for the response
     */
    public StringRequest(String url, ResponseListener<String> listener) {
        super(url, url, listener);
    }

    /**
     * Constructor of the class to change the encoding charset
     *
     * @param url      url of the image to download
     * @param listener to wait for the response
     * @param encoding charset
     */
    public StringRequest(String url, ResponseListener<String> listener, String encoding) {
        super(url, url, listener);
        this.mDefaultCharset = encoding;
    }

    /**
     * Method to get connection properties
     *
     * @return Map
     */
    public Map<String, String> getHeaders() throws Exception {
        return mHeader;
    }


    /**
     * Method to set custom connection properties
     *
     * @param map
     */
    public void setHeaders(Map<String, String> map) {
        mHeader = map;
    }


    /**
     * Method to set encoding charset
     *
     * @param charset("UTF-32")
     */
    public void setEncoding(String charset) {
        mDefaultCharset = charset;
    }

    /**
     * Method converts the stream to String
     *
     * @param stream stream downloaded from server
     * @return String from stream
     */
    @Override
    public String parseResponse(InputStream stream) throws Exception {
        RDownloadLog.i("%s: Parse response %s", TAG, stream.toString());
        if (stream != null) {
            String str = null;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, mDefaultCharset));
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        }
        return null;
    }
}
