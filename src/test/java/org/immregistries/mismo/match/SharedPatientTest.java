package org.immregistries.mismo.match;

import static org.junit.Assert.assertEquals;

import org.immregistries.mismo.match.model.MatchItem;
import org.immregistries.mismo.match.model.Patient;
import org.junit.Test;

public class SharedPatientTest {

  private static final String[][] PAIRS = {
      { "nameFirst=Fyodor,nameMiddle=Ridgeway,nameLast=Larssen,birthDate=20071225,gender=M,phone=(906)389-8576,"
          + "addressStreet1=52 Williamson Cir,addressCity=Lansing,addressState=MI,addressZip=48909,",
        "nameFirst=Fyodor,nameMiddle=Ridgeway,nameLast=Larssen,birthDate=20071225,gender=M,phone=(906)389-8576,"
            + "addressStreet1=369 Pierce St,addressCity=Hubbell,addressState=MI,addressZip=49934," },
      { "nameFirst=Ann,nameLast=Lee,birthDate=20200101,addressStreet1=PO Box 12,addressCity=Lansing,addressZip=48909,",
        "nameFirst=Anne,nameLast=Lee,birthDate=20200101,addressStreet1=P.O. Box 12,addressCity=Lansing,addressZip=48909," },
      { "nameFirst=Bo,nameLast=Kim,birthDate=20190305,", "nameFirst=Cy,nameLast=Kim,birthDate=20190305," } };

  @Test
  public void preparedSharedPatientsCompareLikeFreshlyParsedOnes() {
    PatientCompare fresh = new PatientCompare();
    PatientCompare shared = new PatientCompare();
    for (String[] pair : PAIRS) {
      MatchItem matchItem = new MatchItem();
      matchItem.setPatientDataA(pair[0]);
      matchItem.setPatientDataB(pair[1]);
      fresh.setMatchItem(matchItem);

      Patient a = new Patient(pair[0]).prepareForSharing();
      Patient b = new Patient(pair[1]).prepareForSharing();
      shared.setPatients(a, b);
      assertEquals(fresh.getResult(), shared.getResult());
      assertEquals(fresh.getSignature(), shared.getSignature());

      // Reusing the same prepared patients again gives the same answer.
      shared.setPatients(a, b);
      assertEquals(fresh.getResult(), shared.getResult());
    }
  }
}
