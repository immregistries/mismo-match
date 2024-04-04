package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * Asses two fields to indicate whether they have different values. If either has missing data then
 * the fields do not conflict. 
 * @author Nathan Bunker
 *
 */
public class DoNotConflictMatchNode extends ExactMatchNode {

  public DoNotConflictMatchNode() {
    super();
  }

  public DoNotConflictMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
  }
  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.equals("") || b.equals(""))
    {
      return 1;
    }
    if (a.equalsIgnoreCase(b))
    {
      return ifTrueOrNot(true, 1.0);
    }
    return 0;
  }
  
}
