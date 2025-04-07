package org.immregistries.mismo.match;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;

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

    @Test
    public void testOriginal() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/mismo-matcher.original.yml");
            Configuration configuration = new Configuration(inputStream);
            configuration.setup();
            assertTrue(configuration != null);
            assertEquals("Boston19", configuration.getIslandName());
            assertEquals(0.5976539440304252, configuration.getMatch().getMaxScore(), 0.0001);
            assertEquals("Household", configuration.getMatch().getMatchNodeList().get(0).getMatchName());
            assertEquals(0.5340047021220341, configuration.getMatch().getMatchNodeList().get(0).getMaxScore(), 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
