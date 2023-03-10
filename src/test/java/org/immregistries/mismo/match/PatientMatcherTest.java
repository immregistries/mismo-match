package org.immregistries.mismo.match;

import static org.junit.Assert.assertEquals;
import org.immregistries.mismo.match.model.Patient;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class PatientMatcherTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void verifyMatchingWorksByDefault()
    {
        PatientMatcher patientMatcher = new PatientMatcher();
        Patient patientA = new Patient();
        Patient patientB = new Patient();
        patientA.setNameFirst("Daniel");
        patientB.setNameFirst("Daniel");
        patientA.setNameLast("Smith");
        patientB.setNameLast("Smith");
        patientA.setNameMiddle("Henry");
        patientB.setNameMiddle("Henry");
        patientA.setBirthDate("20201010");
        patientB.setBirthDate("20201010");
        patientA.setMotherMaidenName("Jones");
        patientB.setMotherMaidenName("Jones");
        PatientMatchDetermination patientMatchDeterminiation = patientMatcher.match(patientA, patientB);
        assertEquals(PatientMatchDetermination.MATCH, patientMatchDeterminiation);
    }
}
