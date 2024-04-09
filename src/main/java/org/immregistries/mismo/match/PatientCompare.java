package org.immregistries.mismo.match;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.matchers.MatchNode;
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

  public List<Double> getScoreList()
  {
    if (patientA == null || patientB == null) {
      throw new IllegalArgumentException("Unable to get list of match notes and score map because patient A and patient B were not set");
    }
    List<Double> scoreList = new ArrayList<Double>();
    match.populateScoreList(patientA, patientB, scoreList);
    notMatch.populateScoreList(patientA, patientB, scoreList);
    twin.populateScoreList(patientA, patientB, scoreList);
    missing.populateScoreList(patientA, patientB, scoreList);
    return scoreList;
  }

  public void populateMatchNodeListAndScoreMap(List<MatchNode> matchNodeList, Map<MatchNode, Double> scoreMap) {
    if (patientA == null || patientB == null) {
      throw new IllegalArgumentException("Unable to get list of match notes and score map because patient A and patient B were not set");
    }
    match.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    notMatch.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    twin.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    missing.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
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
    
    for (Double score : scoreList) {
      int scoreInt = (int) (score * 15);
      signature4 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature3 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature2 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature1 += scoreInt % 2;
    }
    sb.append(":" + collapse(signature1));
    sb.append(":" + collapse(signature2));
    sb.append(":" + collapse(signature3));
    sb.append(":" + collapse(signature4));
    return sb.toString();
  }

  private static Map<String, String> collapseCharacterMap = new HashMap<>();
  static {
    collapseCharacterMap.put("000000", "A");
    collapseCharacterMap.put("000001", "B");
    collapseCharacterMap.put("000010", "C");
    collapseCharacterMap.put("000011", "D");
    collapseCharacterMap.put("000100", "E");
    collapseCharacterMap.put("000101", "F");
    collapseCharacterMap.put("000110", "G");
    collapseCharacterMap.put("000111", "H");
    collapseCharacterMap.put("001000", "I");
    collapseCharacterMap.put("001001", "J");
    collapseCharacterMap.put("001010", "K");
    collapseCharacterMap.put("001011", "L");
    collapseCharacterMap.put("001100", "M");
    collapseCharacterMap.put("001101", "N");
    collapseCharacterMap.put("001110", "O");
    collapseCharacterMap.put("001111", "P");
    collapseCharacterMap.put("010000", "Q");
    collapseCharacterMap.put("010001", "R");
    collapseCharacterMap.put("010010", "S");
    collapseCharacterMap.put("010011", "T");
    collapseCharacterMap.put("010100", "U");
    collapseCharacterMap.put("010101", "V");
    collapseCharacterMap.put("010110", "W");
    collapseCharacterMap.put("010111", "X");
    collapseCharacterMap.put("011000", "Y");
    collapseCharacterMap.put("011001", "Z");
    collapseCharacterMap.put("011010", "a");
    collapseCharacterMap.put("011011", "b");
    collapseCharacterMap.put("011100", "c");
    collapseCharacterMap.put("011101", "d");
    collapseCharacterMap.put("011110", "e");
    collapseCharacterMap.put("011111", "f");
    collapseCharacterMap.put("100000", "g");
    collapseCharacterMap.put("100001", "h");
    collapseCharacterMap.put("100010", "i");
    collapseCharacterMap.put("100011", "j");
    collapseCharacterMap.put("100100", "k");
    collapseCharacterMap.put("100101", "l");
    collapseCharacterMap.put("100110", "m");
    collapseCharacterMap.put("100111", "n");
    collapseCharacterMap.put("101000", "o");
    collapseCharacterMap.put("101001", "p");
    collapseCharacterMap.put("101010", "q");
    collapseCharacterMap.put("101011", "r");
    collapseCharacterMap.put("101100", "s");
    collapseCharacterMap.put("101101", "t");
    collapseCharacterMap.put("101110", "u");
    collapseCharacterMap.put("101111", "v");
    collapseCharacterMap.put("110000", "w");
    collapseCharacterMap.put("110001", "x");
    collapseCharacterMap.put("110010", "y");
    collapseCharacterMap.put("110011", "z");
    collapseCharacterMap.put("110100", "0");
    collapseCharacterMap.put("110101", "1");
    collapseCharacterMap.put("110110", "2");
    collapseCharacterMap.put("110111", "3");
    collapseCharacterMap.put("111000", "4");
    collapseCharacterMap.put("111001", "5");
    collapseCharacterMap.put("111010", "6");
    collapseCharacterMap.put("111011", "7");
    collapseCharacterMap.put("111100", "8");
    collapseCharacterMap.put("111101", "9");
    collapseCharacterMap.put("111110", "+");
    collapseCharacterMap.put("111111", "/");
  }

  protected static String collapse(String s)
  {
    String collapse = "";
     while (s.length() > 0)
     {
      if (s.length() < 6)
      {
        s = s + "000000";
        s = s.substring(0, 6);
      }
      String replace = collapseCharacterMap.get(s.substring(0, 6));
      if (replace != null) {
        collapse += replace;
      } else {
        collapse += "!" + s.substring(0, 6) + "!";
      }
      s = s.substring(6);
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
