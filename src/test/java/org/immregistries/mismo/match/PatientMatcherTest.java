package org.immregistries.mismo.match;

import static org.junit.Assert.assertEquals;
import org.immregistries.mismo.match.model.Patient;
import org.junit.Test;

public class PatientMatcherTest {

  @Test
  public void verifyMatchingWorksByDefault() {
    long start = System.currentTimeMillis();
    int count = 1; // 100000

    for (int i = 0; i < count; i++) {
      PatientMatcher patientMatcher = new PatientMatcher();
      Patient daniel = new Patient();
      daniel.setNameFirst("Daniel");
      daniel.setNameLast("Smith");
      daniel.setNameMiddle("Henry");
      daniel.setBirthDate("20201010");
      daniel.setMotherMaidenName("Jones");

      Patient michael = new Patient();
      michael.setNameFirst("Michael");
      michael.setNameLast("Smith");
      michael.setNameMiddle("Henry");
      michael.setBirthDate("20201010");
      michael.setMotherMaidenName("Jones");

      Patient michelle = new Patient();
      michelle.setNameFirst("Michelle");
      michelle.setNameLast("Smith");
      michelle.setNameMiddle("Henry");
      michelle.setBirthDate("20201010");
      michelle.setMotherMaidenName("Jones");

      Patient mike = new Patient();
      mike.setNameFirst("Mike");
      mike.setNameLast("Smith");
      mike.setNameMiddle("Henry");
      mike.setBirthDate("20201010");
      mike.setMotherMaidenName("Jones");

      Patient daniel2 = new Patient();
      daniel2.setNameFirst("Daniel");
      daniel2.setNameLast("Smith");
      daniel2.setNameMiddle("H");
      daniel2.setBirthDate("20201010");
      daniel2.setMotherMaidenName("Jones");

      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(daniel, daniel).getDetermination());

      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(michael, michael).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel, michael).getDetermination());

      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(michelle, michelle).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel, michelle).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(michelle, michael).getDetermination());

      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(mike, mike).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel, mike).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(mike, michelle).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(mike, michael).getDetermination());

      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(daniel2, daniel2).getDetermination());
      assertEquals(PatientMatchDetermination.MATCH, patientMatcher.match(daniel, daniel2).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel2, mike).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel2, michelle).getDetermination());
      assertEquals(PatientMatchDetermination.NO_MATCH, patientMatcher.match(daniel2, michael).getDetermination());
      
//      System.out.println(patientMatcher.match(daniel, daniel).getMatchSignature(MatchSignatureType.PRIMARY));
//      System.out.println(patientMatcher.match(daniel, michael).getMatchSignature(MatchSignatureType.PRIMARY));
//      System.out.println(patientMatcher.match(daniel, daniel).getMatchSignature(MatchSignatureType.SECONDARY));
//      System.out.println(patientMatcher.match(daniel, michael).getMatchSignature(MatchSignatureType.SECONDARY));
//      System.out.println(patientMatcher.match(daniel, daniel).getMatchSignature(MatchSignatureType.TERTIARY));
//      System.out.println(patientMatcher.match(daniel, michael).getMatchSignature(MatchSignatureType.TERTIARY));

      if (i % 1000 == 0) {
        System.out.println(i);
      }
    }

    int matchesPerLoop = 15; //how many times we call match()
    int totalCount = count * matchesPerLoop;

    long end = System.currentTimeMillis();
    long time = end - start;
    System.out.println("time = " + time + " ms; total count = " + totalCount + "; "
        + (totalCount * 1.0 / time) + " comparisons per ms");

    // 3/26/2023 on greg's 3.5+ ghz laptop
    // time = 97493 ms; total count = 1500000; 15.385720000410286 comparisons per ms
  }
}
