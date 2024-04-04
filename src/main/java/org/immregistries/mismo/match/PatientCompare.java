package org.immregistries.mismo.match;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.model.Configuration;
import org.immregistries.mismo.match.model.MatchItem;
import org.immregistries.mismo.match.model.Patient;


/**
 * Holds all comparison nodes and provides support to give final result.
 * @author Nathan Bunker
 *
 */
public class PatientCompare {

  private String ancestry = "";
  private Date born = new Date();
  private AggregateMatchNode match;
  private AggregateMatchNode notMatch;
  private AggregateMatchNode twin;
  private AggregateMatchNode missing;
  private String version = "";
  private String lastModifiedBy = "";
  private Date lastModifiedDate = new Date();
  private List<String> changeLogList = new ArrayList<>();
  Configuration configuration = new Configuration();

  public Configuration getConfiguration()
  {
    return configuration;
  }

  private List<Double> getScoreList()
  {
    List<Double> scoreList = new ArrayList<Double>();
    match.populateScoreList(patientA, patientB, scoreList);
    notMatch.populateScoreList(patientA, patientB, scoreList);
    twin.populateScoreList(patientA, patientB, scoreList);
    missing.populateScoreList(patientA, patientB, scoreList);
    return scoreList;
  }

  public String getSignature()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(configuration.getHashForSignature());
    List<Double> scoreList = getScoreList();
    String signature1 = "";
    String signature2 = "";
    String signature3 = "";
    String signature4 = "";
    
    for (Double score : scoreList)
    {
      int scoreInt = (int) (score * 15);
      // if scoreInt is even, then add 0 to signature1, else add 1
      signature1 += (scoreInt % 2 == 0) ? "0" : "1";
      // shift scoreInt by one bit to the right
      scoreInt = scoreInt >> 1;
      signature2 += (scoreInt % 2 == 0) ? "0" : "1";
      scoreInt = scoreInt >> 1;
      signature3 += (scoreInt % 2 == 0) ? "0" : "1";
      scoreInt = scoreInt >> 1;
      signature4 += (scoreInt % 2 == 0) ? "0" : "1";
    }
    sb.append(":" + collapse(signature1));
    sb.append(":" + collapse(signature2));
    sb.append(":" + collapse(signature3));
    sb.append(":" + collapse(signature4));
    return sb.toString();
  }

  private static Map<String, String> collapseCharacterMap = new HashMap<>();
  static {
    collapseCharacterMap.put("00000", "0");
    collapseCharacterMap.put("00001", "1");
    collapseCharacterMap.put("00010", "2");
    collapseCharacterMap.put("00011", "3");
    collapseCharacterMap.put("00100", "4");
    collapseCharacterMap.put("00101", "5");
    collapseCharacterMap.put("00110", "6");
    collapseCharacterMap.put("00111", "7");
    collapseCharacterMap.put("01000", "8");
    collapseCharacterMap.put("01001", "9");
    collapseCharacterMap.put("01010", "A");
    collapseCharacterMap.put("01011", "B");
    collapseCharacterMap.put("01100", "C");
    collapseCharacterMap.put("01101", "D");
    collapseCharacterMap.put("01110", "E");
    collapseCharacterMap.put("01111", "F");
    collapseCharacterMap.put("10000", "G");
    collapseCharacterMap.put("10001", "H");
    collapseCharacterMap.put("10010", "J");
    collapseCharacterMap.put("10011", "K");
    collapseCharacterMap.put("10100", "M");
    collapseCharacterMap.put("10101", "N");
    collapseCharacterMap.put("10110", "P");
    collapseCharacterMap.put("10111", "Q");
    collapseCharacterMap.put("11000", "R");
    collapseCharacterMap.put("11001", "S");
    collapseCharacterMap.put("11010", "T");
    collapseCharacterMap.put("11011", "U");
    collapseCharacterMap.put("11100", "V");
    collapseCharacterMap.put("11101", "X");
    collapseCharacterMap.put("11110", "Y");
    collapseCharacterMap.put("11111", "Z");
  
  }

  protected static String collapse(String s)
  {
    String collapse = "";
     while (s.length() > 0)
     {
      if (s.length() < 5)
      {
        s = s + "00000";
        s = s.substring(0, 5);
      }
      String replace = collapseCharacterMap.get(s.substring(0, 5));
      if (replace != null) {
        collapse += replace;
      } else {
        collapse += "!" + s.substring(0, 5) + "!";
      }
      s = s.substring(5);
     } 
     return collapse;
  }

  /**
   * Not yet used, but could be used for recording ancestry information.
   * @return
   */
  public String getAncestry() {
    return ancestry;
  }

  /**
   * Not yet used, but could be used for recording ancestry information.
   * @param ancestry
   */
  public void setAncestry(String ancestry) {
    this.ancestry = ancestry;
  }

  /**
   * Creates default patient compare based off of the default script.    
   */
  public PatientCompare() {
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public PatientCompare(InputStream in)
  {
    configuration = new Configuration(in);
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public PatientCompare(String configurationText) {
    configuration = new Configuration(configurationText);
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public void disableMatchNodes(Map<String, Boolean> matchNodeMap) {
    match.disableMatchNodes(matchNodeMap);
    notMatch.disableMatchNodes(matchNodeMap);
    twin.disableMatchNodes(matchNodeMap);
    missing.disableMatchNodes(matchNodeMap);
  }

  /**
   * Sets the matchTestCase that contains the patient data that will be compared.
   * @param matchItem
   */
  public void setMatchItem(MatchItem matchItem) {
    patientA = new Patient(matchItem.getPatientDataA());
    patientB = new Patient(matchItem.getPatientDataB());
    result = null;
  }

  /**
   * Clears previously calculated results and matchTestCase.   
   */
  public void clear() {
    patientA = null;
    patientB = null;
    result = null;
  }

  private Patient patientA = null;

  /**
   * @return base match node
   */
  public AggregateMatchNode getMatch() {
    return match;
  }

  /**
   * @param match the base match node
   */
  public void setMatch(AggregateMatchNode match) {
    this.match = match;
  }

  /**
   * @return base not match node
   */
  public AggregateMatchNode getNotMatch() {
    return notMatch;
  }

  /**
   * @param notMatch base not match node
   */
  public void setNotMatch(AggregateMatchNode notMatch) {
    this.notMatch = notMatch;
  }

  /**
   * 
   * @return base twin node
  */
  public AggregateMatchNode getTwin() {
    return twin;
  }

  /**
   * @param twin base twin node
   */
  public void setTwin(AggregateMatchNode twin) {
    this.twin = twin;
  }

  /**
   * Evaluates the match set and returns verdict. 
   * @return "Match", "Possible Match" or "Not a Match"
   */
  public String getResult() {
    if (result == null) {
      boolean matchSignal = match.hasSignal(patientA, patientB);
      boolean notMatchSignal = notMatch.hasSignal(patientA, patientB);
      boolean missingSignal = missing.hasSignal(patientA, patientB);
      boolean twinSignal = twin.hasSignal(patientA, patientB);
      if (matchSignal) {
        if (notMatchSignal || missingSignal || twinSignal) {
          result = "Possible Match";
        } else {
          result = "Match";
        }
      } else {
        result = "Not a Match";
      }
    }
    return result;
  }

  /**
   * @return missing the missing node
   */
  public AggregateMatchNode getMissing() {
    return missing;
  }

  /**
   * @param missing  the missing node
   */
  public void setMissing(AggregateMatchNode missing) {
    this.missing = missing;
  }

  private String result = null;

  /**
   * @return patient A from match set
   */
  public Patient getPatientA() {
    return patientA;
  }

  /**
   * @param patientA for matching
   */
  public void setPatientA(Patient patientA) {
    this.patientA = patientA;
  }

  /**
   * @return patient B from match set
   */
  public Patient getPatientB() {
    return patientB;
  }

  /**
   * @param patientB for matching
   */
  public void setPatientB(Patient patientB) {
    this.patientB = patientB;
  }

  private Patient patientB = null;

  public Date getBorn() {
    return born;
  }

  public void setBorn(Date born) {
    this.born = born;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public List<String> getChangeLogList() {
    return changeLogList;
  }

  public void setChangeLogList(List<String> changeLogList) {
    this.changeLogList = changeLogList;
  }
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }


}
