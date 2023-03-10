package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * Matches two street address names. This class is a sub class of AddressStreetNumMatchNode
 * and first checks to see if the street number matches. If it doesn't, it assigns a score of 0.0.
 *  Otherwise this matcher examines the first line of a street 
 * address, which includes the house number and possibly an apartment number, and
 * indicates whether the street name matches. If the street matches then a value of 1.0 is assigned
 * otherwise a value of 0.0 is assigned.  
 * @author Nathan Bunker
 *
 */
public class AddressStreetNameMatchNode extends AddressStreetNumMatchNode {
  public AddressStreetNameMatchNode(String matchName, double minScore, double maxScore) {
    super(matchName, minScore, maxScore);
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    double s = super.score(patientA, patientB);
    if (s == 1.0) {
      // TODO, this is literal right now, needs to be less sensitive than it is currently 
      String streetAName = getStreetName(patientA);
      String streetBName = getStreetName(patientB);
      if (!streetAName.equals("")) {
        return ifTrueOrNot(streetAName.equals(streetBName), 1);
      }
    }
    return 0;
  }

  private static String getStreetName(Patient patient) {
    String street1 = patient.getValue("addressStreet1");
    String streetName = "";
    String[] parts = street1.split("\\s");
    if (parts.length > 1) {
      streetName = parts[1];
      for (int i = 2; i < parts.length; i++) {
        streetName += " " + parts[i];
      }
    }
    return streetName;
  }
}
