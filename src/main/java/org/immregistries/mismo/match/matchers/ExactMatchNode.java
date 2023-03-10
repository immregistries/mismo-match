package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.StringUtils;
import org.immregistries.mismo.match.model.Patient;

/**
 * This MatchNode compares two fields and indicates if they are exactly the same. It also
 * has the ability to compare two different fields, such as when comparing first name with
 * middle name. In this special case the exact match is found if if either
 * the primary field of A matches the other field of B or the primary field of B
 * matches the secondary field of A.   
 * @author Nathan Bunker
 *
 */
public class ExactMatchNode extends MatchNode {

  protected String fieldName = "";
  protected String fieldNameOther = null;
  protected String comparisonMatch = "==";
  protected String comparisonNotMatch = "!=";  

  @Override
  public String makeScript() {
    return makeBasicScript() + ":" + fieldName + ":" + fieldNameOther + ":" + comparisonMatch + ":" + comparisonNotMatch + ":";
  }

  @Override
  public int readScript(String script, int pos) {
    pos = readBasicScript(script, pos);
    if (pos < script.length() && script.charAt(pos) != '}') {
      int colPos = nextColon(script, pos);
      if (colPos < script.length() && script.charAt(colPos) != '}') {
        fieldName = script.substring(pos, colPos);
        pos = colPos + 1;
        if (pos < script.length()) {
          colPos = nextColon(script, pos);
          if (colPos < script.length() && script.charAt(colPos) != '}') {
            fieldNameOther = script.substring(pos, colPos);
            pos = colPos + 1;
            if (pos < script.length()) {
              colPos = nextColon(script, pos);
              if (colPos < script.length() && script.charAt(colPos) != '}') {
                comparisonMatch = script.substring(pos, colPos);
                pos = colPos + 1;
                if (pos < script.length()) {
                  colPos = nextColon(script, pos);
                  if (colPos < script.length() && script.charAt(colPos) != '}') {
                    comparisonNotMatch = script.substring(pos, colPos);
                    pos = colPos + 1;
                  }
                }
              }
            }
          }
        }
      }
    }
    return pos;
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
    if (StringUtils.isNotEmpty(fieldNameOther)) {
      String aOther = patientA.getValue(fieldNameOther);
      String bOther = patientB.getValue(fieldNameOther);
      String description = "";
      if (StringUtils.isNotEmpty(a) && a.equalsIgnoreCase(bOther)) {
        description += "'" + a.toUpperCase() + "' " + (not ? comparisonNotMatch : comparisonMatch) + "  '" + bOther.toUpperCase() + "' ";
      }
      if (StringUtils.isNotEmpty(b) && b.equalsIgnoreCase(aOther)) {
        description += "'" + b.toUpperCase() + "' " + (not ? comparisonNotMatch : comparisonMatch) + "  '" + aOther.toUpperCase() + "'";
      }
      return description;
    } 
    
    if (StringUtils.isNotEmpty(a)) {
      return "'" + a.toUpperCase() + "' " + (not ? comparisonNotMatch : comparisonMatch) + "  '" + b.toUpperCase() + "'";
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

  @Override
  public double score(Patient patientA, Patient patientB) {
    String a = patientA.getValue(fieldName);
    String b = patientB.getValue(fieldName);
    if (StringUtils.isNotEmpty(fieldNameOther)) {
      String aOther = patientA.getValue(fieldNameOther);
      String bOther = patientB.getValue(fieldNameOther);
      boolean matchFound = false;
      if (StringUtils.isNotEmpty(a) && a.equalsIgnoreCase(bOther)) {
        matchFound = true;
      }
      if (StringUtils.isNotEmpty(b) && b.equalsIgnoreCase(aOther)) {
        matchFound = true;
      }
      return ifTrueOrNot(matchFound, 1.0);
    }
    if (StringUtils.isNotEmpty(a)) {
      return ifTrueOrNot(a.equalsIgnoreCase(b), 1);
    }
    return 0;
  }

}
