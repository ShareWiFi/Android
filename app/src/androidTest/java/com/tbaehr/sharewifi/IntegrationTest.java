package com.tbaehr.sharewifi;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by tbaehr on 13.02.16.
 */
public class IntegrationTest extends InstrumentationTestCase {

    private final static Integer MAX_WAIT_FOR_OBJECT = 200;   // milliseconds

    private UiDevice device;

    @Override
    public void setUp() throws Exception {
        device = UiDevice.getInstance(getInstrumentation());
    }

    @BeforeClass // Before works as BeforeClass (.... why ????)
    public void testHome() throws Exception {
        // only for demo purpose
        device.wakeUp();
        device.pressHome();
        delay(700);
        device.swipe(200, 1200, 1300, 1200, 20);   // steps=20 is 0.1 sec.
        delay(700);
    }

    @Test
    public void test_01_OpenShareWiFi() throws Exception {
        // enter home / enter menu / enter <applicationName>
        openApplication("ShareWiFi");
    }

    /**
     * 		openApplication
     * 	    find "Applications" by 'desc' and click
     * 	    find the specific App by 'desc' and click
     * 		@param pAppName : Name of the App to be opened
     */

    public void openApplication(String pAppName) throws Exception {
        boolean rc;
        // go to home launcher
        device.pressHome();
        delay(700);
        // enter menu (timeout=200ms)
        findByTypeAndClick("Anwendungen", "desc");
        // enter application (timeout=200ms)
        findByTypeAndClick(pAppName, "desc");
    }

    /**
     * 		findByTypeAndClick
     * 	    check if the object exists (Assertion: true)
     * 	    get the object (Assertion: notNull) and click
     * 		@param pObjVal : value assoziated with the specific criteria
     * 	    @param pType : criteria to find the Object
     */
    public void findByTypeAndClick(String pObjVal, String pType) throws Exception {
        // just implemented types : "text", "desc", "class" (this list may extended)
        boolean rc;
        UiObject2 testAppButton;

        switch (pType) {
            case "desc":
                rc = device.wait(Until.hasObject(By.desc((pObjVal))), 200);
                assertTrue(rc);
                testAppButton = device.findObject(By.desc((pObjVal)));
                assertNotNull(testAppButton);
                testAppButton.click();
                break;
            case "text":
                rc = device.wait(Until.hasObject(By.text((pObjVal))), 200);
                assertTrue(rc);
                testAppButton = device.findObject(By.text((pObjVal)));
                assertNotNull(testAppButton);
                testAppButton.click();
                break;
            case "class":
                rc = device.wait(Until.hasObject(By.clazz((pObjVal))), 200);
                assertTrue(rc);
                testAppButton = device.findObject(By.clazz((pObjVal)));
                assertNotNull(testAppButton);
                testAppButton.click();
                break;
            default:
                throw new IllegalArgumentException("Invalid 'by Type' given: " + pType);
        }

    }

    public void delay(long millis) throws InterruptedException {
        synchronized (device) {
            device.wait(millis);
        }
    }

}
