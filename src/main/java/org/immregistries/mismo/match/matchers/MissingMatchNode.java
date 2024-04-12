package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * Returns a score of 1.0 is either of the values is empty.
 * 
 * @author nathan
 * 
 */
public class MissingMatchNode extends ExactMatchNode
{
  public MissingMatchNode() {
    super();
  }

  public MissingMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
  }

  public MissingMatchNode(String matchName, double minScore, double maxScore, String fieldName, String fieldName2) {
    super(matchName, minScore, maxScore, fieldName);
    this.fieldName2 = fieldName2;
  }

  public MissingMatchNode(String matchName, double minScore, double maxScore, String fieldName, String fieldName2,
      String fieldName3) {
    super(matchName, minScore, maxScore, fieldName);
    this.fieldName2 = fieldName2;
    this.fieldName3 = fieldName3;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    String a = getValue(patientA);
    String b = getValue(patientB);
    return "'" + a.toUpperCase() + "' or '" + b.toUpperCase() + "' is empty";
  }

  private String getValue(Patient p) {
    String value = p.getValue(fieldName);
    if (value.equals("") && fieldName2 != null) {
      value = p.getValue(fieldName2);
    }
    if (value.equals("") && fieldName3 != null) {
      value = p.getValue(fieldName3);
    }
    return value;
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = getValue(patientA);
    String b = getValue(patientB);
    return (a.equals("") || b.equals("")) ? 1 : 0;
  }

}
