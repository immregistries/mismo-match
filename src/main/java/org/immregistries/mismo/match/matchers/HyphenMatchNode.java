package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This matcher is used for examining names that may be hyphenated. For example, 
 * a child may have a name as Smith-Jones, but may go by Smith or Jones. While
 * not an exact match there is good value in noting that part of a hyphenated
 * name does match. 
 * <p>
 * If neither of the names contains a hyphen, a score of 0.0 is assigned. Otherwise
 * the all hyphenated names are split by the hyphens and all name parts are compared
 * from one name with all the name parts with the other. If one of them matches, 
 * ignoring case, then a score of 1.0 is assigned. If no match is found then
 * a score of 0.0 is returned.  
 * @author Nathan Bunker
 *
 */
public class HyphenMatchNode extends ExactMatchNode {

  public HyphenMatchNode () {
    super();
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }

  public HyphenMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.contains("-") || b.contains("-")) {
      String[] partsA = a.split("\\-");
      String[] partsB = a.split("\\-");
      for (String partA : partsA) {
        for (String partB : partsB) {
          if (partB != null && !partB.equals("") && partA != null && !partA.equals("")) {
            if (partB.equalsIgnoreCase(partA)) {
              return ifTrueOrNot(true, 1);
            }
          }
        }
      }
      return ifTrueOrNot(false, 0);
    }
    return 0.0;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (!a.contains("-") && !b.contains("-")) {
      return "";
    }
    return super.getDescription(patientA, patientB);
  }
  
}
