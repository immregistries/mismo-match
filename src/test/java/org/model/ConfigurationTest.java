package org.model;

import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.matchers.ExactMatchNode;
import org.immregistries.mismo.match.matchers.MissingMatchNode;
import org.immregistries.mismo.match.model.Configuration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationTest {

    @Test
    public void testInit() {
        Configuration configuration = new Configuration();
        configuration.setup();
        assertEquals("Alma", configuration.getWorldName());
        assertEquals("Alpha", configuration.getIslandName());
        assertEquals(1, configuration.getGeneration());
        assertEquals(0.0, configuration.getGenerationScore(), 0.01);
        assertNotNull(configuration.getScoringWeights());
        assertEquals(12, configuration.getScoringWeights()[0][0]);
        assertEquals(10, configuration.getScoringWeights()[0][1]);
        assertEquals(-10, configuration.getScoringWeights()[0][2]);
        assertEquals(8, configuration.getScoringWeights()[1][0]);
        assertEquals(10, configuration.getScoringWeights()[1][1]);
        assertEquals(-8, configuration.getScoringWeights()[1][2]);
        assertEquals(-10, configuration.getScoringWeights()[2][0]);
        assertEquals(-8, configuration.getScoringWeights()[2][1]);
        assertEquals(10, configuration.getScoringWeights()[2][2]);
        assertNotNull(configuration.getMatch());
        assertNotNull(configuration.getMissing());
        assertNotNull(configuration.getNotMatch());
        assertNotNull(configuration.getTwin());
        assertEquals(true, configuration.getMatch().isEnabled());
        assertEquals(0.0, configuration.getMissing().getMinScore(), 0.01);
        assertEquals(1.0, configuration.getMissing().getMaxScore(), 0.01);
        assertEquals("Match", configuration.getMatch().getMatchName());
        assertEquals("Match", configuration.getMatch().getMatchLabel());
        assertEquals(2, configuration.getMatch().getMatchNodeList().size());
        AggregateMatchNode householdMatchNode = (AggregateMatchNode) configuration.getMatch().getMatchNodeList().get(0);
        assertEquals("Household", householdMatchNode.getMatchName());
        assertEquals(true, householdMatchNode.isEnabled());
        assertEquals(0.0, householdMatchNode.getMinScore(), 0.01);
        assertEquals(0.4, householdMatchNode.getMaxScore(), 0.01);
        AggregateMatchNode personMatchNode = (AggregateMatchNode) configuration.getMatch().getMatchNodeList().get(1);
        assertEquals("Person", personMatchNode.getMatchName());
        assertEquals(3, householdMatchNode.getMatchNodeList().size());
        AggregateMatchNode lastNameMatchNode = (AggregateMatchNode) householdMatchNode.getMatchNodeList().get(0);
        assertEquals("Last Name", lastNameMatchNode.getMatchName());
        assertEquals(true, lastNameMatchNode.isEnabled());
        assertEquals(0.0, lastNameMatchNode.getMinScore(), 0.01);
        assertEquals(0.4, lastNameMatchNode.getMaxScore(), 0.01);
        assertEquals(5, lastNameMatchNode.getMatchNodeList().size());
        ExactMatchNode lMatchNode = (ExactMatchNode) lastNameMatchNode.getMatchNodeList().get(0);
        assertEquals("L-match", lMatchNode.getMatchName());
        assertEquals(0.0, lMatchNode.getMinScore(), 0.01);
        assertEquals(1.0, lMatchNode.getMaxScore(), 0.01);
        ExactMatchNode lSimilarNode = (ExactMatchNode) lastNameMatchNode.getMatchNodeList().get(1);
        assertEquals("L-similar", lSimilarNode.getMatchName());
        assertEquals(0.0, lSimilarNode.getMinScore(), 0.01);
        assertEquals(0.4, lSimilarNode.getMaxScore(), 0.01);
        ExactMatchNode lHyphenatedNode = (ExactMatchNode) lastNameMatchNode.getMatchNodeList().get(2);
        assertEquals("L-hyphenated", lHyphenatedNode.getMatchName());
        assertEquals(0.0, lHyphenatedNode.getMinScore(), 0.01);
        assertEquals(0.6, lHyphenatedNode.getMaxScore(), 0.01);
        assertEquals("Missing", configuration.getMissing().getMatchName());
        AggregateMatchNode householdMissingNode = (AggregateMatchNode) configuration.getMissing().getMatchNodeList()
                .get(0);
        assertNotNull(householdMissingNode);
        assertEquals("Household", householdMissingNode.getMatchName());
        assertEquals(3, householdMissingNode.getMatchNodeList().size());
        AggregateMatchNode locationMissingNode = (AggregateMatchNode) householdMissingNode.getMatchNodeList().get(2);
        assertEquals("Location", locationMissingNode.getMatchName());
        assertEquals(6, locationMissingNode.getMatchNodeList().size());
        MissingMatchNode missingMatchNode = (MissingMatchNode) locationMissingNode.getMatchNodeList().get(1);
        assertNotNull(missingMatchNode);
        assertEquals("AS1-missing", missingMatchNode.getMatchName());
        assertEquals(0.0, missingMatchNode.getMinScore(), 0.01);
        assertEquals("address2Street1", missingMatchNode.getFieldName2());
    }

}
