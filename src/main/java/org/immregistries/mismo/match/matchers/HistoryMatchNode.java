package org.immregistries.mismo.match.matchers;

import java.util.HashSet;
import java.util.Set;
import org.immregistries.mismo.match.model.Patient;

/**
 * This MatchNode examines the vaccination history to generate a score that indicates 
 * how closely the two histories match. 
 * @author Nathan Bunker
 *
 */
public class HistoryMatchNode extends ExactMatchNode {

  public HistoryMatchNode() {
    super();
  }

  public HistoryMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String[] partsA = patientA.getValue(fieldName).split("\\s");
    String[] partsB = patientA.getValue(fieldName).split("\\s");
    Set<String> setA = new HashSet<String>();
    for (String partA : partsA) {
      if (partA != null && partA.length() > 0) {
        if (partA.length() >= 10 && partA.indexOf("-") != -1)
        {
          setA.add(partA);          
        }
      }
    }
    Set<String> setB = new HashSet<String>();
    for (String partB : partsB) {
      if (partB != null && partB.length() > 0) {
        if (partB.length() >= 10 && partB.indexOf("-") != -1)
        {
          setB.add(partB);          
        }
      }
    }
    if (setA.size() == 0 || setB.size() == 0)
    {
      return 0;
    }
    Set<String> dates = new HashSet<String>();
    for (String partA : setA)
    {
      if (setB.contains(partA))
      {
        // same vaccination given on same day
       dates.add(partA.substring(0, 8));
      }
    }
    double uncertainty = 1.6;
    for (int i = 0; i < dates.size(); i++)
    {
      uncertainty = uncertainty * 0.8;
    }
    if (uncertainty > 1)
    {
      uncertainty = 1;
    }
    return ifTrueOrNot(true, 1 - uncertainty);
  }

}
