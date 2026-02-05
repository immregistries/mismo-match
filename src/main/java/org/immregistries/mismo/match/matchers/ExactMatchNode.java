package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This MatchNode compares two fields and indicates if they are exactly the
 * same. It also
 * has the ability to compare two different fields, such as when comparing first
 * name with
 * middle name. In this special case the exact match is found if if either
 * the primary field of A matches the other field of B or the primary field of B
 * matches the secondary field of A.
 * 
 * @author Nathan Bunker
 *
 */
public class ExactMatchNode extends MatchNode {

  protected String comparisonMatch = "==";
  protected String comparisonNotMatch = "!=";

  public ExactMatchNode() {
    super();
  }

  public ExactMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore);
    this.fieldName = fieldName;
  }

  public ExactMatchNode(String matchName, double minScore, double maxScore, String fieldName, String fieldNameOther) {
    super(matchName, minScore, maxScore);
    this.fieldName = fieldName;
    this.fieldNameOther = fieldNameOther;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (hasFieldNameOther()) {
      String aOther = patientA.getValue(fieldNameOther);
      String bOther = patientB.getValue(fieldNameOther);
      String description = "";
      if (hasTwoOrMoreCharacters(a)) {
        description += "'" + a.toUpperCase() + "' "
            + (a.equalsIgnoreCase(bOther) ? comparisonNotMatch : comparisonMatch) + "  '"
            + bOther.toUpperCase() + "' ";
      }
      if (hasTwoOrMoreCharacters(b)) {
        description += "'" + b.toUpperCase() + "' "
            + (b.equalsIgnoreCase(aOther) ? comparisonNotMatch : comparisonMatch) + "  '"
            + aOther.toUpperCase() + "'";
      }
      return description;
    }

    if (hasTwoOrMoreCharacters(a)) {
      return "'" + a.toUpperCase() + "' " + (a.equalsIgnoreCase(b) ? comparisonNotMatch : comparisonMatch) + "  '"
          + b.toUpperCase()
          + "'";
    }
    return "";
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (hasFieldNameOther()) {
      String aOther = patientA.getValue(fieldNameOther);
      String bOther = patientB.getValue(fieldNameOther);
      boolean matchFound = false;
      if (hasTwoOrMoreCharacters(a) && a.equalsIgnoreCase(bOther)) {
        matchFound = true;
      }
      if (hasTwoOrMoreCharacters(b) && b.equalsIgnoreCase(aOther)) {
        matchFound = true;
      }
      return ifTrueOrNot(matchFound, 1.0);
    }
    if (hasTwoOrMoreCharacters(a)) {
      return ifTrueOrNot(a.equalsIgnoreCase(b), 1);
    }
    return 0;
  }

  private static boolean hasTwoOrMoreCharacters(String value) {

    return value != null && value.length() >= 2;
  }

}
