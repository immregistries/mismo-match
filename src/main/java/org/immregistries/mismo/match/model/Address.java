package org.immregistries.mismo.match.model;

public class Address
{

  private static final String[] SECOND_FLOOR_TOKEN = new String[] { " 2ND FLOOR", " 2ND FL.", " 2ND FL",
      " SECOND FLOOR" };
  private static final String[] FIRST_FLOOR_TOKEN = new String[] { " 1ST FLOOR", " 1ST FL.", " 1ST FL", " FIRST FLOOR" };
  private static final String[] PO_BOX_TOKEN = new String[] { "P.O. BOX", "PO BOX" };
  private static final String[] APARTMENT_TOKENS = new String[] { " APARTMENT ", " APT/ ", " APT. #", " APT.#",
      " APT. NUM ", " APT. ", " APT #", " APT#", " APT NUM ", " APT ", "APT.", " UNIT NUM ", " UNIT #", " UNIT ",
      " BLDG ", " NBR ", " NUM", " #", "#" };

  public enum AddressType {
    PHYSICAL, PO_BOX, UNREADABLE, POSSIBLE_GROUP_ADDRESS
  }

  private static class Token
  {
    String matchWith = null;
    int matchWithPosStart = 0;
    int matchWithPosEnd = 0;

    public Token(String matchWith, int matchWithPosStart, int matchWithPosEnd) {
      this.matchWith = matchWith;
      this.matchWithPosStart = matchWithPosStart;
      this.matchWithPosEnd = matchWithPosEnd;
    }
  }

  private String line1 = "";
  private String line2 = "";
  private String state = "";
  private String city = "";
  private String zip = "";
  private String zip5Only = "";
  private AddressType addressType = null;
  private String number = "";
  private String streetName = "";
  private String apartment = "";

  public String getNumberWithApartment() {
    if (apartment.equals("")) {
      return number;
    }
    return number + " APT " + apartment;
  }

  public String getZip5Only() {
    return zip5Only;
  }

  public void parseAddress() {
    if (line1.length() == 0) {
      addressType = AddressType.UNREADABLE;
    } else {
      String line1Upper = line1.toUpperCase();
      String line2Upper = line2.toUpperCase();
      if (line2Upper.equals(state.toUpperCase()) || line2Upper.equals(zip.toUpperCase())
          || line2Upper.equals(city.toUpperCase())) {
        line2Upper = "";
      }
      char line1Char = line1.charAt(0);

      Token poBoxToken = findToken(line1Upper, PO_BOX_TOKEN);
      {
        boolean startsWithApartment = startsWith(" " + line1Upper, APARTMENT_TOKENS);
        if (line2Upper.length() > 0 && (poBoxToken != null || startsWithApartment)) {
          // if PO Box is the first position and there is something in the
          // second position then this is probably the physical addres
          line1Upper = line2.toUpperCase();
          line2Upper = line1.toUpperCase();
          poBoxToken = findToken(line1Upper, PO_BOX_TOKEN);
        }
      }
      if (poBoxToken != null) {
        addressType = AddressType.PO_BOX;
        number = line1Upper.substring(poBoxToken.matchWithPosEnd).trim();
      } else if (contains(line1Upper, new String[] { "NO ADDRESS" })
          || startsWith(line1Upper, new String[] { "ADDRESS" }) || (line1Char < 0 && line1Char > 9)) {
        addressType = AddressType.UNREADABLE;
      } else {
        addressType = AddressType.PHYSICAL;

        // first find the apartment
        String lineAll = line1Upper;
        if (!line2Upper.equals("")) {
          Token apartmentToken = findToken(" " + line2Upper, APARTMENT_TOKENS);
          Token secondFloorToken = findToken(" " + line2Upper, SECOND_FLOOR_TOKEN);
          Token firstFloorToken = findToken(" " + line2Upper, FIRST_FLOOR_TOKEN);
          if (apartmentToken != null || secondFloorToken != null || firstFloorToken != null) {
            if (apartmentToken != null) {
              if (line1Upper.indexOf(line2Upper) > line1Upper.indexOf(' ')) {
                // don't add, it's already in Line 1
              } else {
                lineAll += " " + line2Upper;
              }
            } else {
              lineAll += " " + line2Upper;
            }
          } else {
            poBoxToken = findToken(line2Upper, PO_BOX_TOKEN);
            if (poBoxToken != null) {
              // ignore
            } else {
              addressType = AddressType.POSSIBLE_GROUP_ADDRESS;
            }
          }
        }
        int pos = lineAll.indexOf(' ');
        if (pos == -1) {
          addressType = AddressType.UNREADABLE;
        } else {
          number = lineAll.substring(0, pos);
          lineAll = lineAll.substring(pos).trim();
          if (lineAll.startsWith("1/2 ")) {
            pos = 3;
            number += " " + lineAll.substring(0, pos);
            lineAll = lineAll.substring(pos).trim();
          } else if (lineAll.length() > 1 && lineAll.charAt(1) == ' ') {
            char possibleApartmentNumber = lineAll.charAt(0);
            if (possibleApartmentNumber != 'N' && possibleApartmentNumber != 'E' && possibleApartmentNumber != 'S'
                && possibleApartmentNumber != 'W') {
              number += possibleApartmentNumber;
              lineAll = lineAll.substring(1).trim();
            }
          }
          Token apartmentToken = findToken(lineAll, APARTMENT_TOKENS);
          if (apartmentToken != null) {
            apartment = lineAll.substring(apartmentToken.matchWithPosEnd).trim();
            lineAll = lineAll.substring(0, apartmentToken.matchWithPosStart).trim();
          } else {
            // 2ND FL could begin a street name, so only look for it after there
            // has been at least two tokens from street name and type
            String sfLine = lineAll;
            int sfPos = lineAll.indexOf(' ');
            if (sfPos > 0) {
              sfLine = lineAll.substring(sfPos + 1);
              Token secondFloorToken = findToken(sfLine, SECOND_FLOOR_TOKEN);
              if (secondFloorToken != null) {
                apartment = "2ND FLOOR";
                lineAll = lineAll.substring(0, sfPos + secondFloorToken.matchWithPosStart).trim();
              }
              Token firstFloorToken = findToken(sfLine, FIRST_FLOOR_TOKEN);
              if (firstFloorToken != null) {
                apartment = "1ST FLOOR";
                lineAll = lineAll.substring(0, sfPos + firstFloorToken.matchWithPosStart).trim();
              }
            }
          }
          streetName = lineAll;
        }
      }
    }

    if (!zip.equals("")) {
      zip5Only = zip;
      int pos = zip5Only.indexOf('-');
      if (pos > 0) {
        zip5Only.substring(0, pos);
      }
      pos = zip5Only.indexOf(' ');
      if (pos > 0) {
        zip5Only.substring(0, pos);
      }

      if (zip.length() < 5) {
        zip5Only = "0000" + zip5Only;
        zip5Only = zip5Only.substring(zip5Only.length() - 5);
      } else if (zip.length() > 5) {
        zip5Only = zip.substring(0, 5);
      }
      boolean looksGood = true;
      for (char c: zip5Only.toCharArray())
      {
        if (c < '0' || c > '9')
        {
          looksGood = false;
          break;
        }
      }
      if (looksGood)
      {
        for (String invalidZip : INVALID_ZIP_CODES)
        {
          if (zip5Only.equals(invalidZip))
          {
            looksGood = false;
          }
        }
      }
      if (!looksGood)
      {
        zip5Only = "";
      }
    }
  }
  
  private static String[] INVALID_ZIP_CODES = new String[] {"00000", "99999"};

  private static boolean startsWith(String s, String[] matchWith) {
    for (String mw : matchWith) {
      if (s.startsWith(mw)) {
        return true;
      }
    }
    return false;
  }

  private static boolean contains(String s, String[] matchWith) {
    for (String mw : matchWith) {
      if (s.indexOf(mw) != -1) {
        return true;
      }
    }
    return false;
  }

  private static Token findToken(String s, String[] matchWith) {
    for (String mw : matchWith) {
      int pos = s.indexOf(mw);
      if (pos != -1) {
        return new Token(mw, pos, pos + mw.length());
      }
    }
    return null;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public void setAddressType(AddressType addressType) {
    this.addressType = addressType;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getApartment() {
    return apartment;
  }

  public void setApartment(String apartment) {
    this.apartment = apartment;
  }

  public String getLine1() {
    return line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getLine2() {
    return line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }
}
