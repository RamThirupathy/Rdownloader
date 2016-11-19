/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;

import com.ramkt.example.manager.Application;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * GenericConstants hold all the configuration value read from configuration value
 */
public class GenericConstants {
    private static final String TAG = GenericConstants.class.getSimpleName();
    private static GenericConstants mConstants;
    private String mAPIUrl;


    /**
     * private constructor to achieve singleton
     */
    private GenericConstants() {

    }

    public String getAPIUrl() {
        return mAPIUrl;
    }

    public void setAPIUrl(String mAPIUrl) {
        this.mAPIUrl = mAPIUrl;
    }


    /**
     * Method to initialize the class if not initialized
     */
    public static GenericConstants getInstance() {
        if (mConstants == null) {
            mConstants = new GenericConstants();
            parseConfigFile("ConfigurationValues.xml");
        }
        return mConstants;
    }


    /**
     * Method to read the value from configuration value
     *
     * @param fileName name of file in asset folder.
     */
    private static void parseConfigFile(String fileName) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream inputStream = Application.getAppContext().getAssets()
                    .open(fileName);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parseXML(parser);
        } catch (XmlPullParserException | IOException e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
        }
    }


    /**
     * Method to parse value from configuration value and store
     * it in local variables
     *
     * @param parser xml parser.
     */
    private static void parseXML(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (!name.trim().equalsIgnoreCase("ConfigurationValues"))
                        populateConstants(name, parser.nextText());
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    /**
     * Method to parse value from configuration value and store
     * it in local variables
     *
     * @param name  xml tag name.
     * @param value xml tag value of the name tag.
     */
    private static void populateConstants(String name, String value) {
        if ("APIUrl".equals(name)) {
            mConstants.mAPIUrl = value;
        }
    }

}

