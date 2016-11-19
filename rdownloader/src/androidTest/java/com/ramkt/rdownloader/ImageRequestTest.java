/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.rdownloader;

import com.ramkt.rdownloader.RequestTool.ImageRequest;

import junit.framework.TestCase;

/**
 * ImageRequestTest to unit test {@link ImageRequest}
 */
public class ImageRequestTest extends TestCase {

    ImageRequest mImageRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Test method to check the key when aspect ratio is passed
     */
    public void test_Image_Request_key_based_on_aspect_ratio() throws Exception {
        int width = 10, heigth = 20;
        mImageRequest = new ImageRequest("TESTURL", null, width, heigth, width, heigth);
        assertEquals(mImageRequest.getRequestKey(), String.format("TESTURL%d%d", 10, 10));
    }
}
