package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * MatchNode indicates that neither records is listed as definitely 
 * part of a multiple birth (birth status = Y) but that at least one record is listed
 * as maybe being a part of a multiple birth record (birth status = M). 
 * @author Nathan Bunker
 *
 */
public class BirthMayBeTwinMatchNode extends MatchNode {
  public BirthMayBeTwinMatchNode(String matchName, double minScore, double maxScore) {
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
  public double score(Patient patientA, Patient patientB) {
    String birthStatusA = patientA.getValue(Patient.BIRTH_STATUS);
    String birthStatusB = patientB.getValue(Patient.BIRTH_STATUS);
    if (!birthStatusA.equals("Y") && !birthStatusB.equals("Y") && (birthStatusB.equals("M") || birthStatusA.equals("M"))) {
      return ifTrueOrNot(true, 1.0);
    }
    return 0;
  }
  
  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "'" + patientA.getValueDescription(Patient.BIRTH_STATUS) + "' or '" + patientB.getValueDescription(Patient.BIRTH_STATUS) + "' == 'M'";
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
}
