/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.utils;


import com.ramkt.example.response.Pins;

import java.util.List;

/**
 * IResponseParser interface that all parser in the app should implement
 * and refer{@link JsonParser} for details
 */
public interface IResponseParser {

    /**
     * Init Method to initialize the parser
     */
     void init();

    /**
     * Method to parse the response
     *
     * @param content json in String
     */
    List<Pins> parsePins(String content);
}
