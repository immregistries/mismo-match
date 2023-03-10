package org.immregistries.mismo.match.matchers;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mismo.match.model.Address;
import org.immregistries.mismo.match.model.Patient;
import com.wcohen.ss.JaroWinkler;

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
public class AddressMatchNode extends MatchNode
{
  public AddressMatchNode(String matchName, double minScore, double maxScore) {
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

  private JaroWinkler jaroWinkler = new JaroWinkler();

  @Override
  public double score(Patient patientA, Patient patientB) {

    List<Address> addressListA = buildAddressList(patientA);
    List<Address> addressListB = buildAddressList(patientB);

    double matchLevel = 0.0;
    boolean foundPotentialMatch = false;
    for (Address addressA : addressListA) {
      for (Address addressB : addressListB) {
        if (addressA.getAddressType() == addressB.getAddressType()) {
          foundPotentialMatch = true;
          if (addressA.getZip5Only().equals(addressB.getZip5Only())) {
            double ml = 0.30;
            if (addressA.getAddressType() == Address.AddressType.PO_BOX) {
              if (addressA.getNumber().equals(addressB.getNumber())) {
                ml = 1.0;
              } else {
                ml = 0.0;
              }
            } else {
              if (addressA.getNumberWithApartment().equals(addressB.getNumberWithApartment())) {
                ml += 0.40;
              }
              ml += jaroWinkler.score(addressA.getStreetName(), addressB.getStreetName()) * 0.30;
            }
            if (ml > matchLevel) {
              matchLevel = ml;
            }
          }
        }
      }
    }

    if (foundPotentialMatch) {
      return ifTrue(matchLevel);
    }
    return 0;
  }

  private List<Address> buildAddressList(Patient patient) {
    List<Address> addressList = new ArrayList<Address>();
    {
      Address address1 = patient.getAddress1();
      if (address1.getAddressType() == Address.AddressType.PHYSICAL) {
        addressList.add(address1);
      }
    }
    {
      Address address2 = patient.getAddress2();
      if (address2.getAddressType() == Address.AddressType.PHYSICAL) {
        addressList.add(address2);
      }
    }
    {
      Address address3 = patient.getAddress3();
      if (address3.getAddressType() == Address.AddressType.PHYSICAL) {
        addressList.add(address3);
      }
    }
    return addressList;
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
