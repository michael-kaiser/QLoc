package com.example.michael.qloc;

import android.location.Location;
import com.example.qloc.model.WayPoint;
import junit.framework.TestCase;


/**
 * Created by michael on 13.04.15.
 */
public class JsonTest extends TestCase {
    WayPoint wp;
    protected void setUp() {
        wp = new WayPoint(new Location(""), "GoldenesDachl", "n nettes Dach", "Wer baute das Goldene Dachl", "Maximilan I.", "Max und Moritz", "Max Payne", "KlaxMAx");
        wp.setLatitude(47.268646d);
        wp.setLongitude(11.393268d);
    }
    public void testAdd() {
        assertEquals(wp.toJSON(),"Somethubg");
    }

    public static void main(String ... args) {
        TestCase test = new JsonTest() {
            public void runTest() {
                testAdd();
            }
        };
        test.run();
    }

}
