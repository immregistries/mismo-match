package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;
import com.wcohen.ss.JaroWinkler;

/**
 * Tests to see how similar two fields are. This scorer returns a 0 score if
 * both fields are exactly the same. Otherwise it returns a percentage of how
 * similiar they are. Currently based on the JaroWinkler distance algorithm.
 * 
 * @author Nathan Bunker
 */
public class SimilarMatchNode extends ExactMatchNode {

  public SimilarMatchNode() {
    super();
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }
  public SimilarMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
    comparisonMatch = "~";
    comparisonNotMatch = "!~";
  }

  private JaroWinkler jaroWinkler = new JaroWinkler();

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (a.equalsIgnoreCase(b)) {
      return 0;
    }
    if (!a.equals("")) {
      if (!b.equals("")) {
        return ifTrue(jaroWinkler.score(a, b));
      }
    }
    return 0;
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
}
