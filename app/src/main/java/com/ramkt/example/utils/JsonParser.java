/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramkt.example.response.Pins;

import java.util.List;

/**
 * JsonParser implement {@link IResponseParser}interface that handles Json parsing
 * in the app and it uses {@link JsonParser}
 */
public class JsonParser implements IResponseParser {
    private static final String TAG = JsonParser.class.getSimpleName();
    private static JsonParser mInstance;
    private ObjectMapper mMapper = null;


    /**
     * private constructor to achieve singleton
     */
    private JsonParser() {
        init();
    }

    /**
     * private constructor to achieve singleton
     */
    public static JsonParser getJsonParserInstance() {
        if (mInstance == null) {
            mInstance = new JsonParser();
        }
        return mInstance;
    }

    /**
     * Method to initialize the {@link ObjectMapper}
     */
    public synchronized void init() {
        if (mMapper == null) {
            mMapper = new ObjectMapper();
            mMapper.configure(
                    DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
            mMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        }
    }

    /**
     * Method to parse the string to {@link Pins}
     */
    public List<Pins> parsePins(String content) {
        List<Pins> pins = null;
        try {
            if (mMapper == null) {
                init();
            }
            pins = mMapper.readValue(content, mMapper.getTypeFactory().constructCollectionType(List.class,Pins.class));
            Logger.i(TAG, "Response " + pins);
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage(), ex);
        }
        return pins;
    }

}
