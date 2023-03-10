package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * Compares two dates and returns a score indicating how similar they are.
 * This returns 0 if both dates are exactly the same. The similarity 
 * score is calculated as follows:
 * <ul>
 *   <li>If month and day are switched and the year matches, a score of 0.7 is assigned.</li>
 *   <li>If the month and day match but the year is off by one, a score of 0.7 is assigned.</li>
 * </ul>
 * 
 * @author nathan
 *
 */
public class SimilarDateMatchNode extends ExactMatchNode {
  public SimilarDateMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }
  
  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.equalsIgnoreCase(b)) {
      return "";
    }
    return super.getDescription(patientA, patientB);
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.equalsIgnoreCase(b)) {
      return 0;
    }
    if (!a.equals("")) {
      if (a.length() == 8 && b.length() == 8) {
        try {
          int yearA = Integer.parseInt(a.substring(0, 4));
          int yearB = Integer.parseInt(b.substring(0, 4));
          int monthA = Integer.parseInt(a.substring(4, 6));
          int monthB = Integer.parseInt(b.substring(4, 6));
          int dayA = Integer.parseInt(a.substring(6, 8));
          int dayB = Integer.parseInt(b.substring(6, 8));
          // month/day switch
          if (yearA == yearB) {
            if (monthA == dayB && dayA == monthB) {
              return ifTrueOrNot(true, 0.7);
            }
          }
          // off by one year
          if (Math.abs(yearA - yearB) == 1) {
            if (monthA == monthB && dayA == dayB) {
              return ifTrueOrNot(true, 0.7);
            }
          }
        } catch (NumberFormatException e) {
          return 0;
        }
      }
      return ifTrueOrNot(false, 0.0);
    }
    return 0;
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
