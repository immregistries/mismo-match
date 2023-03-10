package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * Compares to strings, if either string is empty or if neither string is one
 * character long a score of 0.0 is returned. Otherwise the first character of
 * each string is compared and if they match (without regards to case) a score
 * of 1.0 is returned, otherwise a 0.0.
 * 
 * @author nathan
 * 
 */
public class SameInitialMatchNode extends ExactMatchNode {
  public SameInitialMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.length() > 0 && b.length() > 0) {
      if (a.length() > 1 && b.length() > 1) {
        // both are not initials so this comparison can't be made
        return 0;
      }
      if (a.toUpperCase().charAt(0) == b.toUpperCase().charAt(0)) {
        return ifTrueOrNot(true, 1.0);
      }
    }
    return 0;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.length() > 0 && b.length() > 0) {
      if (a.length() > 1 && b.length() > 1) {
        // both are not initials so this comparison can't be made
        return "";
      }
      if (a.toUpperCase().charAt(0) == b.toUpperCase().charAt(0)) {
        return "'" + a.toUpperCase().charAt(0) + "' " + (not ? comparisonNotMatch : comparisonMatch) + "  '" + b.toUpperCase().charAt(0) + "'";
      }
    }
    return "";
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
