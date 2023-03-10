package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This match node returns 1.0 if both patient records indicate that they
 * are not part of a multiple birth. 
 * @author Nathan Bunker
 *
 */
public class BirthNotIndicatedMatchNode extends MatchNode {
  public BirthNotIndicatedMatchNode(String matchName, double minScore, double maxScore) {
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
    if (birthStatusA.equals("N") && birthStatusB.equals("N")) {
      return 1;
    }
    return 0;
  }
  
  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "";
  }

}
