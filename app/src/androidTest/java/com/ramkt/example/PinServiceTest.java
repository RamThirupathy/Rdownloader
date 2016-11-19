/**
 * Created by Ram_Thirupathy on 9/4/2016.
 */
package com.ramkt.example;

import com.ramkt.example.backend.PinService;
import com.ramkt.example.response.Pins;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * PinServiceTest is test class for {@link PinService}
 */
public class PinServiceTest extends TestCase {
    PinService mPinService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPinService = new PinService(null);
    }

    /**
     * Test method to test the  Pin service new pin addition
     */
    public void test_Pin_Service_addition() throws Exception {
        List<Pins> pins = new ArrayList<Pins>();
        pins.add(new Pins());
        pins.add(new Pins());
        mPinService.addNewPins(pins);
        assertEquals(2, mPinService.mPins.size());
        pins = new ArrayList<Pins>();
        pins.add(new Pins());
        pins.add(new Pins());
        mPinService.addNewPins(pins);
        assertEquals(4, mPinService.mPins.size());
    }

}
