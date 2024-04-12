package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This match node returns 1.0 if both patient records indicate that they
 * are not part of a multiple birth. 
 * @author Nathan Bunker
 *
 */
public class BirthNotIndicatedMatchNode extends MatchNode {

  public BirthNotIndicatedMatchNode() {
    super();
  }

  public BirthNotIndicatedMatchNode(String matchName, double minScore, double maxScore) {
    super(matchName, minScore, maxScore);
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
