/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.rdownloader;


import junit.framework.TestCase;

import java.io.InputStream;

/**
 * RDownloaderTest to unit test {@link RDownloader}
 */
public class RDownloaderTest extends TestCase {
    RDownloader mRDownloader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRDownloader = new RDownloader(10);
    }

    /**
     * Test method to check request load
     */
    public void test_RDownloader_load_on() throws Exception {
        mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        assertEquals(true, mRDownloader.checkCache("TESTURL") != null);
    }

    /**
     * Test method to check the cache
     */
    public void test_RDownloader_cache() throws Exception {
        mRDownloader = new RDownloader(10);
        mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        assertEquals(true, mRDownloader.checkCache("TESTURL") != null);
    }

    /**
     * Test method to check the cache is limit is not provided
     */
    public void test_RDownloader_zero_cache() throws Exception {
        mRDownloader = new RDownloader(0);
        mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        assertEquals(true, mRDownloader.checkCache("TESTURL") == null);
    }

    /**
     * Test method to check the cancel request
     */
    public void test_RDownloader_Cancel_Request_by_ID() throws Exception {
        int id = mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        mRDownloader.cancel("TESTURL", id);
        assertEquals(true, mRDownloader.checkCache("TESTURL") == null);
    }

    /**
     * Test method to check the cancel request
     */
    public void test_RDownloader_Cancel_request_by_key() throws Exception {
        int id = mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        mRDownloader.cancel("TESTURL");
        assertEquals(true, mRDownloader.checkCache("TESTURL") == null);
    }

    /**
     * Test method to check the cancel request
     */
    public void test_RDownloader_Cancel_All_Request() throws Exception {
        int id = mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        mRDownloader.cancelAll();
        assertEquals(true, mRDownloader.checkCache("TESTURL") == null);
    }

    /**
     * Test method to check the multiple request
     */
    public void test_RDownloader_Mulitiple_Request() throws Exception {
        int id = mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        int id1 = mRDownloader.load(new Request("TESTURL", "TESTURL", null) {
            @Override
            public Object parseResponse(InputStream stream) throws Exception {
                return null;
            }
        });
        mRDownloader.cancel("TESTURL", id);
        assertEquals(true, mRDownloader.checkCache("TESTURL") != null);
    }
}
