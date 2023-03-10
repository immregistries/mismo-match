package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.StringUtils;
import org.immregistries.mismo.match.model.Patient;

/**
 * Examines a list of tokens split by the defined splitParameter and returns a value of 1.0
 * if at least one of the tokens matches from both lists. 
 * @author Nathan Bunker
 *
 */
public class AtLeastOneExactMatchNode extends ExactMatchNode {

  private String splitParameter = ".";	
  public AtLeastOneExactMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
    super(matchName, minScore, maxScore, fieldName);
    comparisonMatch 	= "contains";
    comparisonNotMatch 	= "does not contain";
  }

  /**
   * @param matchName the name of the match, unique to the tree
   * @param minScore the minimum score that can be assigned
   * @param maxScore the maximum score that can be assigned
   * @param fieldName the name of the field to compare on patient record
   * @param splitParameter the parameter to split the field by in order to look for at least one exact matches
   */
  public AtLeastOneExactMatchNode(String matchName, double minScore, double maxScore, String fieldName, String splitParameter) {
	    super(matchName, minScore, maxScore, fieldName);
	    comparisonMatch 	= "contains";
	    comparisonNotMatch 	= "does not contain";
	    this.splitParameter = splitParameter;
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
	String[] partsA = patientA.getValue(fieldName).split("\\"+splitParameter);
    String[] partsB = patientB.getValue(fieldName).split("\\"+splitParameter);
    if (partsA.length > 0 && StringUtils.isNotEmpty(partsA[0]) && partsB.length > 0 && StringUtils.isNotEmpty(partsB[0])) {
      for (String partA : partsA) {
        for (String partB : partsB) {
          if (StringUtils.isNotEmpty(partA) && StringUtils.isNotEmpty(partB)) {
        	  if (partA.trim().equalsIgnoreCase(partB.trim())) {        		          		  
        		  return ifTrueOrNot(true, 1.0);        		  
        	  }
          	}
        }
      }      
    }
    return 0;
  }  
}
