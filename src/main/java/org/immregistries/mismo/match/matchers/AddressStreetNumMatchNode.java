package org.immregistries.mismo.match.matchers;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mismo.match.model.Address;
import org.immregistries.mismo.match.model.Patient;

/**
 * Matches the street number and apartment number on a street address. The
 * street number is assumed to be the first token of street1 and the apartment
 * number is assumed to be on the street2 field. The matcher examines to find
 * the street number and if available concatenates the street2 value This entire
 * value is compared to give a match score of 1.0 or a not-match score of 0.0.
 * 
 * @author Nathan Bunker
 * 
 */
public class AddressStreetNumMatchNode extends MatchNode
{
  public AddressStreetNumMatchNode(String matchName, double minScore, double maxScore) {
    super(matchName, minScore, maxScore);
  }

  @Override
  public String getSignature(Patient patientA, Patient patientB) {
    double score = score(patientA, patientB);
    if (score >= 0.9) {
      return "A";
    }
    if (score >= 0.7) {
      return "B";
    }
    if (score >= 0.3) {
      return "C";
    }
    return "D";
  }

  @Override
  public String makeScript() {
    return makeBasicScript();
  }

  @Override
  public int readScript(String script, int pos) {
    pos = readBasicScript(script, pos);
    return pos;
  }

  @Override
  public double score(Patient patientA, Patient patientB) {

    List<Address> addressListA = buildAddressAList(patientA);
    List<Address> addressListB = buildAddressBList(patientB);

    boolean foundMatch = false;
    boolean foundPotentialMatch = false;
    for (Address addressA : addressListA) {
      for (Address addressB : addressListB) {
        if (addressA.getAddressType() == addressB.getAddressType()) {
          foundPotentialMatch = true;
          String sA = addressA.getNumberWithApartment();
          String sB = addressB.getNumberWithApartment();
          foundMatch = sA.equals(sB);
          if (foundMatch) {
            break;
          }
        }
        if (foundMatch) {
          break;
        }
      }
    }

    if (foundPotentialMatch) {
      return ifTrueOrNot(foundMatch, 1.0);
    }
    return 0;
  }

  private List<Address> buildAddressBList(Patient patientB) {
    List<Address> addressListB = new ArrayList<Address>();
    {
      Address addressB1 = patientB.getAddress1();
      if (addressB1.getAddressType() == Address.AddressType.PHYSICAL
          || addressB1.getAddressType() == Address.AddressType.PO_BOX) {
        addressListB.add(addressB1);
      }
    }
    {
      Address addressB2 = patientB.getAddress2();
      if (addressB2.getAddressType() == Address.AddressType.PHYSICAL
          || addressB2.getAddressType() == Address.AddressType.PO_BOX) {
        addressListB.add(addressB2);
      }
    }
    return addressListB;
  }

  private List<Address> buildAddressAList(Patient patientA) {
    List<Address> addressListA = buildAddressBList(patientA);
    return addressListA;
  }

  private static String getStreetNumApartment(Patient patient) {
    String street1 = patient.getValue("addressStreet1");
    String street2 = patient.getValue("addressStreet2");
    String streetNum = "";
    String[] parts = street1.split("\\s");
    if (parts.length > 0) {
      streetNum = parts[0];
    }
    if (street2.length() > 0) {
      streetNum += "-" + street2;
    }
    return streetNum;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "";
  }

}
