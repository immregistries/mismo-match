package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * MatchNode assigns score of 0.1 if both patients are know to be part of multiple birth
 * but one or both records does not indicate the actual position in the birth (birth order).
 * @author Nathan Bunker
 *
 */
public class BirthHasTwinMatchNode extends MatchNode {
  public BirthHasTwinMatchNode(String matchName, double minScore, double maxScore) {
    super(matchName, minScore, maxScore);
  }
  
  @Override
  public String makeScript() {
    return makeBasicScript();
  }
  
  @Override
  public int readScript(String script, int pos) {
    pos = readBasicScript(script, pos);
    return pos;
  }
  
  @Override
  public String getSignature(Patient patientA, Patient patientB) {
    double score = score(patientA, patientB);
    if (score >= 0.9)
    {
      return "A";
    }
    if (score >= 0.7)
    {
      return "B";
    }
    if (score >= 0.3)
    {
      return "C";
    }
    return "D";
  }
  
  @Override
  public double score(Patient patientA, Patient patientB) {
    String birthStatusA = patientA.getValue(Patient.BIRTH_STATUS);
    String birthStatusB = patientB.getValue(Patient.BIRTH_STATUS);
    if ((birthStatusA.equals("Y") || birthStatusB.equals("Y"))) {
      String birthOrderA = patientA.getValue(Patient.BIRTH_ORDER);
      String birthOrderB = patientB.getValue(Patient.BIRTH_ORDER);
      if (birthOrderA.equals("") || birthOrderB.equals("")) {
        return ifTrueOrNot(true, 1.0);
      }
    }
    return 0;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "'" + patientA.getValueDescription(Patient.BIRTH_ORDER) + "' or '" + patientB.getValueDescription(Patient.BIRTH_ORDER) + "' is blank";
  }
}
