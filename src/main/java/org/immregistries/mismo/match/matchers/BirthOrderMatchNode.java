package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This MatchNode returns 1.0 only if both records indicate a multiple birth,
 * they indicate the correct number of births and they indicate the same position
 * in that birth series. 
 * @author Nathan Bunker
 *
 */
public class BirthOrderMatchNode extends MatchNode {
  public BirthOrderMatchNode(String matchName, double minScore, double maxScore) {
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
    if (birthStatusA.equals("Y") || birthStatusA.equals("M") || birthStatusB.equals("Y") || birthStatusB.equals("M")) {
      if (birthStatusA.equals("Y") && birthStatusB.equals("Y")) {
        String birthTypeA = patientA.getValue(Patient.BIRTH_TYPE);
        String birthTypeB = patientB.getValue(Patient.BIRTH_TYPE);
        if (!birthTypeA.equals("") && !birthTypeA.equals("1") && birthTypeA.equals(birthTypeB)) {
          String birthOrderA = patientA.getValue(Patient.BIRTH_ORDER);
          String birthOrderB = patientB.getValue(Patient.BIRTH_ORDER);
          if (!birthOrderA.equals("") && birthOrderA.equals(birthOrderB)) {
            return ifTrueOrNot(true, 1.0);
          }
        }
      }
      return ifTrueOrNot(false, 0.0);
    }
    return 0;
  }
  
  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "";
  }
}
