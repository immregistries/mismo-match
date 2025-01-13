package org.immregistries.mismo.match;

import java.io.InputStream;

import org.immregistries.mismo.match.model.Patient;

public class PatientMatcher {

  private PatientCompare patientCompare;

  public PatientMatcher() {
    patientCompare = new PatientCompare();
  }

  public PatientMatcher(InputStream in)
  {
    patientCompare = new PatientCompare(in);
  }

  public PatientMatchResult match(Patient patientA, Patient patientB) {
    PatientMatchResult patientMatchResult = new PatientMatchResult();
    patientCompare.setPatientA(patientA);
    patientCompare.setPatientB(patientB);
    String result = patientCompare.getResult();
    if (result.equals("Possible Match")) {
      patientMatchResult.setDetermination(PatientMatchDetermination.POSSIBLE_MATCH);
    } else if (result.equals("Match")) {
      patientMatchResult.setDetermination(PatientMatchDetermination.MATCH);
    } else {
      patientMatchResult.setDetermination(PatientMatchDetermination.NO_MATCH);
    }
    patientMatchResult.addMatchSignature(
        new MatchSignature(patientCompare.getSignature(), MatchSignatureType.PRIMARY));
    return patientMatchResult;
  }

  public String generateSignature(Patient patientA, Patient patientB) {
    patientCompare.setPatientA(patientA);
    patientCompare.setPatientB(patientB);
    return patientCompare.getSignature();
  }
}
