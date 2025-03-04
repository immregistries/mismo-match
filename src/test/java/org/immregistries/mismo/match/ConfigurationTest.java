package org.immregistries.mismo.match;

import static org.junit.Assert.assertTrue;

import org.immregistries.mismo.match.model.Configuration;
import org.junit.Test;

public class ConfigurationTest {

    // test to verify that Configruation can be created
    @Test
    public void testInit() {
        try {

            Configuration configuration = new Configuration();
            configuration.setup();
            assertTrue(configuration != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
