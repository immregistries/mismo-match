package org.immregistries.mismo.match.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a patient to be compared. The object is very simple and holds all
 * of its internal values in a hash map. This makes processing later on easier. 
 * @author Nathan Bunker
 *
 */
public class Patient {

  
  public Patient()
  {
    // default
  }
  
  /**
   * Easy constructor. Values can be passed in as a comma separated list of value pairs. 
   * Not good for production usage as the comma and equals character are not allowed inputs. 
   * @param values
   */
  public Patient(String values) {
    if (values != null) {
      String[] valueArray = values.split("\\,");
      for (String value : valueArray) {
        if (value != null) {
          value = value.trim();
          int pos = value.indexOf("=");
          if (value.length() > 0 && pos != -1) {
            valueMap.put(value.substring(0, pos), value.substring(pos + 1));
          }
        }
      }
    }
  }

  public Patient(Map<String, String> values) {
    if (values != null) {
      valueMap = new HashMap<>(values);
    }
  }
  
  private int patientId = 0;
  private Patient linkWith = null;
  
  /**
   * The patient id is used for internal purposes to identify test cases. Not related to any other patient
   * id the patient has within the registry or submitter system. 
   * @return
   */
  public int getPatientId() {
	return patientId;
  }

  public void setPatientId(int patientId) {
	this.patientId = patientId;
  }

  public Patient getLinkWith() {
	return linkWith;
  }

  public void setLinkWith(Patient linkWith) {
	this.linkWith = linkWith;
  }

  /**
   * @return a comma separated list of name-value pairs
   */
  public String getValues()
  {
    StringBuffer sbuf = new StringBuffer();
    for (String key : valueMap.keySet())
    {
      sbuf.append(key + "=" + valueMap.get(key) + ",");
    }
    return sbuf.toString();
  }

  private Map<String, String> valueMap = new HashMap<String, String>();
  private Address address1 = null;
  private Address address2 = null;
  private Address address3 = null;

  public Address getAddress1() {
    if (address1 == null)
    {
      address1 = new Address();
      address1.setLine1(getValue(ADDRESS_STREET1));
      address1.setLine2(getValue(ADDRESS_STREET2));
      address1.setCity(getValue(ADDRESS_CITY));
      address1.setState(getValue(ADDRESS_STATE));
      address1.setZip(getValue(ADDRESS_ZIP));
      address1.parseAddress();
    }
    return address1;
  }

  public Address getAddress2() {
    if (address2 == null)
    {
      address2 = new Address();
      address2.setLine1(getValue(ADDRESS_2_STREET1));
      address2.setLine2(getValue(ADDRESS_2_STREET2));
      address2.setCity(getValue(ADDRESS_2_CITY));
      address2.setState(getValue(ADDRESS_2_STATE));
      address2.setZip(getValue(ADDRESS_2_ZIP));
      address2.parseAddress();
    }
    return address2;
  }

  public Address getAddress3() {
    if (address3 == null)
    {
      address3 = new Address();
      address3.setLine1(getValue(ADDRESS_3_STREET1));
      address3.setLine2(getValue(ADDRESS_3_STREET2));
      address3.setCity(getValue(ADDRESS_3_CITY));
      address3.setState(getValue(ADDRESS_3_STATE));
      address3.setZip(getValue(ADDRESS_3_ZIP));
      address3.parseAddress();
    }
    return address3;
  }

  /**
   * Returns a value based on a given field name
   * @param fieldName
   * @return
   */
  public String getValue(String fieldName) {
    String s = valueMap.get(fieldName);
    if (s == null) {
      return "";
    }
    return s;
  }
  
  public void setValue(String fieldName, String value)
  {
    valueMap.put(fieldName, value);
  }
  
  /**
   * Return nicely formatted value description, in this 
   * format [fieldName]:[fieldValue]
   * @param fieldName
   * @return
   */
  public String getValueDescription(String fieldName)
  {
    return fieldName + ":" + getValue(fieldName);
  }

  public Map<String, String> getValueMap() {
    return valueMap;
  }


  public String getNameFirst() {
    return getValue(NAME_FIRST) ;
  }

  /**
   * @param nameFirst the first name of the patient
   */
  public void setNameFirst(String nameFirst) {
    valueMap.put(NAME_FIRST, nameFirst);
  }

  public String getNameAlias() {
    return getValue(NAME_ALIAS);
  }

  /**
   * @param nameAlias alternate name, alternate spelling, also-known-as, or nickname
   */
  public void setNameAlias(String nameAlias) {
    valueMap.put(NAME_ALIAS, nameAlias);
  }

  public String getNameMiddle() {
    return getValue(NAME_MIDDLE);
  }

  /**
   * @param nameMiddle middle name or initial
   */
  public void setNameMiddle(String nameMiddle) {
    valueMap.put(NAME_MIDDLE, nameMiddle);
  }

  public String getNameLast() {
    return getValue(NAME_LAST);
  }

  /**
   * @param nameLast
   */
  public void setNameLast(String nameLast) {
    valueMap.put(NAME_LAST, nameLast);
  }

  public String getNameLastHyph() {
    return getValue(NAME_LAST_HYPH);
  }

  /**
   * The nameLastHyph a temporary holding place for certain internal functions to hold a possible 
   * version of the name of the patient in hyphenated form. This to assist logic for generating
   * possible matches for testing. This is not to be used during production with real data. All
   * last names should be sent in the last name field and will be considered there. 
   * @param nameLastHyph 
   */
  public void setNameLastHyph(String nameLastHyph) {
    valueMap.put(NAME_LAST_HYPH, nameLastHyph);
  }

  public String getNameSuffix() {
    return getValue(NAME_SUFFIX);
  }

  /**
   * @param nameSuffix the suffix used by patient
   */
  public void setNameSuffix(String nameSuffix) {
    valueMap.put(NAME_SUFFIX, nameSuffix);
  }

  public String getBirthDate() {
    return getValue(BIRTH_DATE);
  }

  /**
   * @param birthDate the date of birth
   */
  public void setBirthDate(String birthDate) {
    valueMap.put(BIRTH_DATE, birthDate);
  }

  public String getGuardianNameFirst() {
    return getValue(GUARDIAN_NAME_FIRST);
  }

  /**
   * @param guardianNameFirst first name of responsible person associated with patient (usually child's mom or dad)
   */
  public void setGuardianNameFirst(String guardianNameFirst) {
    valueMap.put(GUARDIAN_NAME_FIRST, guardianNameFirst);
  }

  public String getGuardianNameLast() {
    return getValue(GUARDIAN_NAME_LAST);
  }

  /**
   * @param guardianNameLast last name of responsible person associated with patient (usually child's mom or dad)
   */
  public void setGuardianNameLast(String guardianNameLast) {
    valueMap.put(GUARDIAN_NAME_LAST, guardianNameLast);
  }

  public String getMotherNameFirst() {
    return getValue(MOTHER_NAME_FIRST);
  }

  /**
   * @param motherNameFirst first name of patient's mother
   */
  public void setMotherNameFirst(String motherNameFirst) {
    valueMap.put(MOTHER_NAME_FIRST, motherNameFirst);
  }

  public String getMotherNameLast() {
    return getValue(MOTHER_NAME_LAST);
  }

  /**
   * @param motherNameLast last name of patient's mother
   */
  public void setMotherNameLast(String motherNameLast) {
    valueMap.put(MOTHER_NAME_LAST, motherNameLast);
  }
  
  public String getMotherNameMiddle() {
	    return getValue(MOTHER_NAME_MIDDLE);
	  }

  /**
   * @param motherNameMiddle middel name of patient's mother
   */
  public void setMotherNameMiddle(String motherNameMiddle) {
	 valueMap.put(MOTHER_NAME_MIDDLE, motherNameMiddle);
  }
	  
 
  public String getFatherNameFirst() {
    return getValue(FATHER_NAME_FIRST);
  }

  /**
   * @param fatherNameFirst first name of patient's father
   */
  public void setFatherNameFirst(String fatherNameFirst) {
    valueMap.put(FATHER_NAME_FIRST, fatherNameFirst);
  }

  public String getFatherNameLast() {
    return getValue(FATHER_NAME_LAST);
  }

  /**
   * @param fatherNameLast last name of patient's father
   */
  public void setFatherNameLast(String fatherNameLast) {
    valueMap.put(FATHER_NAME_LAST, fatherNameLast);
  }

  public String getMotherMaidenName() {
    return getValue(MOTHER_MAIDEN_NAME);
  }

  /**
   * @param motherMaidenName madien name of patient's mother
   */
  public void setMotherMaidenName(String motherMaidenName) {
    valueMap.put(MOTHER_MAIDEN_NAME, motherMaidenName);
  }

  public String getPhone() {
    return getValue(PHONE);
  }

  /**
   * @param phone only the last 7 digits of phone number in standardized format (remove dashes)
   */
  public void setPhone(String phone) {
    valueMap.put(PHONE, phone);
  }

  public String getAddressStreet1() {
    return getValue(ADDRESS_STREET1);
  }

  public String getAddressStreet1Alt() {
	    return getValue(ADDRESS_STREET1_ALT);
	  }

  /**
   * @param addressStreet1 first line of patient's address
   */
  public void setAddressStreet1(String addressStreet1) {
    valueMap.put(ADDRESS_STREET1, addressStreet1);
  }

  public void setAddress2Street1(String address2Street1) {
    valueMap.put(ADDRESS_2_STREET1, address2Street1);
  }

  public void setAddressStreet1Alt(String addressStreet1Alt) {
	    valueMap.put(ADDRESS_STREET1_ALT, addressStreet1Alt);
	  }

  public String getAddressStreet2() {
    return getValue(ADDRESS_STREET2);
  }

  public String getAddress2Street2() {
    return getValue(ADDRESS_2_STREET2);
  }

  /**
   * @param addressStreet2 second line of patient's address
   */
  public void setAddressStreet2(String addressStreet2) {
    valueMap.put(ADDRESS_STREET2, addressStreet2);
  }

  public void setAddress2Street2(String address2Street2) {
    valueMap.put(ADDRESS_2_STREET2, address2Street2);
  }

  public String getAddressCity() {
    return getValue(ADDRESS_CITY);
  }

  public String getAddress2City() {
    return getValue(ADDRESS_2_CITY);
  }

  /**
   * @param addressCity address city
   */
  public void setAddressCity(String addressCity) {
    valueMap.put(ADDRESS_CITY, addressCity);
  }

  public void setAddress2City(String address2City) {
    valueMap.put(ADDRESS_2_CITY, address2City);
  }

  public String getAddressState() {
    return getValue(ADDRESS_STATE);
  }

  public String getAddress2State() {
    return getValue(ADDRESS_2_STATE);
  }

  /**
   * @param addressState address state
   */
  public void setAddressState(String addressState) {
    valueMap.put(ADDRESS_STATE, addressState);
  }

  public void setAddress2State(String address2State) {
    valueMap.put(ADDRESS_2_STATE, address2State);
  }

  public String getAddressZip() {
    return getValue(ADDRESS_ZIP);
  }

  public String getAddress2Zip() {
    return getValue(ADDRESS_2_ZIP);
  }

  /**
   * @param addressZip address zip
   */
  public void setAddressZip(String addressZip) {
    valueMap.put(ADDRESS_ZIP, addressZip);
  }

  public void setAddress2Zip(String address2Zip) {
    valueMap.put(ADDRESS_2_ZIP, address2Zip);
  }

  public String getGender() {
    return getValue(GENDER);
  }

  /**
   * @param gender gender or sex
   */
  public void setGender(String gender) {
    valueMap.put(GENDER, gender);
  }

  public String getBirthStatus() {
    return getValue(BIRTH_STATUS);
  }

  /**
 * @param birthStatus indicates if patient is part of multiple birth or not, Y = Yes, N = No, M = Maybe
 */
public void setBirthStatus(String birthStatus) {
    valueMap.put(BIRTH_STATUS, birthStatus);
  }

  public String getBirthType() {
    return getValue(BIRTH_TYPE);
  }

  /**
   * @param birthType indicates how many were part of multiple birth, 1 = single, 2 = twins, 3 = triplets, etc.
   */
  public void setBirthType(String birthType) {
    valueMap.put(BIRTH_TYPE, birthType);
  }

  public String getBirthOrder() {
    return getValue(BIRTH_ORDER);
  }

  /**
   * @param birthOrder indicates which position patient was born in, 1 = first, 2 = second, 3 = third, etc.
   */
  public void setBirthOrder(String birthOrder) {
    valueMap.put(BIRTH_ORDER, birthOrder);
  }

  public String getShotHistory() {
    return getValue(SHOT_HISTORY);
  }

  /**
   * The shot history should be encoded in this field as [VAC CODE]-[VAC DATE(YYYYMMDD)].[VAC CODE]-[VAC DATE(YYYYMMDD)]...
   * <p>
   * For example: <code>03-20120102.12-20120102.03-20111131</code>
 * @param shotHistory indicates a list of vaccinations given and when
 */
public void setShotHistory(String shotHistory) {
    valueMap.put(SHOT_HISTORY, shotHistory);
  }

  public String getMrns() {
    return getValue(MRNS);
  }

  /**
   * @param mrns a common separated list of provider ids concatenated with their associated MRNs 
   */
  public void setMrns(String mrns) {
    valueMap.put(MRNS, mrns);
  }

  public String getSsn() {
    return getValue(SSN);
  }

  /**
   * @param ssn social security number
   */
  public void setSsn(String ssn) {
    valueMap.put(SSN, ssn);
  }
  
  public void setMedicaid(String medicaid)
  {
    valueMap.put(MEDICAID, medicaid);
  }
  
  /**
   * @return medicaid id
   */
  public String getMedicaid()
  {
    return getValue(MEDICAID);
  }

  public void setVacMfr(String vacMfr)
  {
    valueMap.put(VAC_MFR, vacMfr);
  }
  
  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @return 
   */
  public String getVacMfr()
  {
    return getValue(VAC_MFR);
  }

  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @param vacName
   */
  public void setVacName(String vacName)
  {
    valueMap.put(VAC_NAME, vacName);
  }
  
  public String getVacName()
  {
    return getValue(VAC_NAME);
  }

  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @param vacCode
   */
  public void setVacCode(String vacCode)
  {
    valueMap.put(VAC_CODE, vacCode);
  }
  
  public String getVacCode()
  {
    return getValue(VAC_CODE);
  }
  
  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @param vacDate
   */
  public void setVacDate(String vacDate)
  {
    valueMap.put(VAC_DATE, vacDate);
  }
  
  public String getVacDate()
  {
    return getValue(VAC_DATE);
  }
  
  public String getEthnicity() {
    return getValue(ETHNICITY);
  }

  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @param ethnicity
   */
  public void setEthnicity(String ethnicity) {
    valueMap.put(ETHNICITY, ethnicity);
  }

  public String getRace() {
    return getValue(RACE);
  }

  /**
   * Added for support of CDC efforts, not supported by matching algorithm.
   * @param race
   */
  public void setRace(String race) {
    valueMap.put(RACE, race);
  }

  public static final String NAME_FIRST = "nameFirst";
  public static final String NAME_ALIAS = "nameAlias";
  public static final String NAME_MIDDLE = "nameMiddle";
  public static final String NAME_LAST = "nameLast";
  public static final String NAME_LAST_HYPH = "nameLastHyph";
  public static final String NAME_SUFFIX = "nameSuffix";
  public static final String BIRTH_DATE = "birthDate";
  public static final String GUARDIAN_NAME_FIRST = "guardianNameFirst";
  public static final String GUARDIAN_NAME_LAST = "guardianNameLast";
  public static final String MOTHER_NAME_FIRST = "guardianNameFirst";
  public static final String MOTHER_NAME_LAST = "guardianNameLast";
  public static final String MOTHER_NAME_MIDDLE = "guardianNameMiddle";
  public static final String FATHER_NAME_FIRST = "guardianNameFirst";
  public static final String FATHER_NAME_LAST = "guardianNameLast";
  public static final String MOTHER_MAIDEN_NAME = "motherMaidenName";
  public static final String PHONE = "phone";
  public static final String RACE = "race";
  public static final String ETHNICITY = "ethnicity";
  public static final String ADDRESS_STREET1 = "addressStreet1";
  public static final String ADDRESS_STREET1_ALT = "addressStreet1Alt";
  public static final String ADDRESS_STREET2 = "addressStreet2";
  public static final String ADDRESS_CITY = "addressCity";
  public static final String ADDRESS_STATE = "addressState";
  public static final String ADDRESS_ZIP = "addressZip";
  public static final String ADDRESS_2_STREET1 = "address2Street1";
  public static final String ADDRESS_2_STREET1_ALT = "address2Street1Alt";
  public static final String ADDRESS_2_STREET2 = "address2Street2";
  public static final String ADDRESS_2_CITY = "address2City";
  public static final String ADDRESS_2_STATE = "address2State";
  public static final String ADDRESS_2_ZIP = "address2Zip";
  public static final String ADDRESS_3_STREET1 = "address3Street1";
  public static final String ADDRESS_3_STREET1_ALT = "address3Street1Alt";
  public static final String ADDRESS_3_STREET2 = "address3Street2";
  public static final String ADDRESS_3_CITY = "address3City";
  public static final String ADDRESS_3_STATE = "address3State";
  public static final String ADDRESS_3_ZIP = "address3Zip";
  public static final String GENDER = "gender";
  public static final String BIRTH_STATUS = "birthStatus";
  public static final String BIRTH_TYPE = "birthType";
  public static final String BIRTH_ORDER = "birthOrder";
  public static final String SHOT_HISTORY = "shotHistory";
  public static final String MRNS = "mrns";
  public static final String SSN = "ssn";
  public static final String MEDICAID = "medicaid";
  public static final String VAC_NAME = "vacName";
  public static final String VAC_DATE = "vacDate";
  public static final String VAC_CODE = "vacCode";
  public static final String VAC_MFR = "vacMfr";
}
